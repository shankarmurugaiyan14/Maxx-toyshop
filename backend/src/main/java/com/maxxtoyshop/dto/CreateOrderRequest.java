package com.maxxtoyshop.dto; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import lombok.*; import java.util.*;
@Getter @Setter public class CreateOrderRequest {
 private Long userId; @NotBlank private String customerEmail; @NotBlank private String phone; @NotBlank private String shippingAddress;
 @NotBlank private String city; @NotBlank private String state; @NotBlank private String pincode; @NotBlank private String paymentMethod;
 @NotEmpty @Valid private List<OrderItemRequest> items;
 @Getter @Setter public static class OrderItemRequest { @NotNull private Long productId; @Min(1) private int quantity; }
}