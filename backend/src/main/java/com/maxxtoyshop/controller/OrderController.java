package com.maxxtoyshop.controller;

import com.maxxtoyshop.dto.CreateOrderRequest;
import com.maxxtoyshop.model.CustomerOrder;
import com.maxxtoyshop.model.OrderItem;
import com.maxxtoyshop.model.Product;
import com.maxxtoyshop.repository.OrderItemRepository;
import com.maxxtoyshop.repository.OrderRepository;
import com.maxxtoyshop.repository.ProductRepository;
import com.maxxtoyshop.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orders;
    private final OrderItemRepository orderItems;
    private final ProductRepository products;
    private final UserRepository users;

    @PostMapping
    @Transactional
    public Map<String, Object> create(@Valid @RequestBody CreateOrderRequest request) {

        CustomerOrder order = new CustomerOrder();

        if (request.getUserId() != null) {
            order.setUser(users.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "User not found: " + request.getUserId())));
        }

        order.setCustomerEmail(request.getCustomerEmail().trim());
        order.setPhone(request.getPhone().trim());
        order.setShippingAddress(request.getShippingAddress().trim());
        order.setCity(request.getCity().trim());
        order.setState(request.getState().trim());
        order.setPincode(request.getPincode().trim());
        order.setPaymentMethod(request.getPaymentMethod().trim());
        order.setSubtotal(BigDecimal.ZERO);
        order.setDeliveryCharge(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);

        // Save and flush the parent FIRST. This guarantees that
        // customer_orders.id exists before any order_items row is inserted.
        CustomerOrder savedOrder = orders.saveAndFlush(order);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CreateOrderRequest.OrderItemRequest requestItem : request.getItems()) {

            if (requestItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            Product product = products.findById(requestItem.getProductId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Product not found: " + requestItem.getProductId()));

            if (product.getStock() < requestItem.getQuantity()) {
                throw new IllegalArgumentException(
                    "Insufficient stock for " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(requestItem.getQuantity());
            item.setPrice(product.getPrice());

            // Save the child explicitly after the parent has an ID.
            orderItems.save(item);

            BigDecimal itemTotal = product.getPrice().multiply(
                BigDecimal.valueOf(requestItem.getQuantity()));
            subtotal = subtotal.add(itemTotal);

            product.setStock(product.getStock() - requestItem.getQuantity());
            products.save(product);
        }

        BigDecimal deliveryCharge = subtotal.compareTo(BigDecimal.valueOf(2000)) >= 0
            ? BigDecimal.ZERO
            : BigDecimal.valueOf(99);

        savedOrder.setSubtotal(subtotal);
        savedOrder.setDeliveryCharge(deliveryCharge);
        savedOrder.setTotalAmount(subtotal.add(deliveryCharge));

        orders.saveAndFlush(savedOrder);

        return Map.of(
            "success", true,
            "message", "Order placed successfully",
            "orderId", savedOrder.getId(),
            "subtotal", savedOrder.getSubtotal(),
            "deliveryCharge", savedOrder.getDeliveryCharge(),
            "totalAmount", savedOrder.getTotalAmount()
        );
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<CustomerOrder> all() {
        return orders.findAll();
    }

    @GetMapping("/customer")
    @Transactional(readOnly = true)
    public List<CustomerOrder> customer(@RequestParam String email) {
        return orders.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public CustomerOrder one(@PathVariable Long id) {
        return orders.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Order not found: " + id));
    }
}
