import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from './api.service';


interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  image: string;
  tag?: string;
}

interface CartItem extends Product {
  quantity: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  constructor(
  private apiService: ApiService
) {}
  

ngOnInit(): void {
  this.apiService.getProducts().subscribe({
    next: (products) => {
      console.log('Products received from backend:', products);
    },
    error: (error) => {
      console.error('Backend connection failed:', error);
    }
  });
}

  // =========================================
  // SHOP SETTINGS
  // =========================================

  shopName = 'Maxx Toy Shop';

  cartOpen = false;

  checkoutOpen = false;

  orderPlaced = false;

  selectedCategory = 'All';


  // =========================================
  // PRODUCTS
  // =========================================

  products: Product[] = [

    {
      id: 1,
      name: 'LEGO Set STAR WARS',
      category: 'Educational',
      price: 799,
      image: 'assets/LEGO STAR WARS.jpg',
      tag: 'Popular'
    },

    {
      id: 2,
      name: 'HotWheels',
      category: 'Remote Control',
      price: 299,
      image: 'assets/Hotwheels.jpg',
      tag: 'New'
    },

    {
      id: 3,
      name: 'Barbie',
      category: 'Dolls',
      price: 599,
      image: 'assets/Barbie.jpg'
    },

    {
      id: 4,
      name: 'Batman',
      category: 'Action Figures',
      price: 999,
      image: 'assets/Batman.jpg',
      tag: 'Hot'
    },

    {
      id: 5,
      name: 'RC Helicopter',
      category: 'Educational',
      price: 1199,
      image: 'assets/Rc helicopter.jpg'
    },

    {
      id: 6,
      name: 'Teddy Bear',
      category: 'Soft Toys',
      price: 399,
      image: 'assets/Teddybear.jpg'
    },

    {
      id: 7,
      name: 'MONOPOLY',
      category: 'Board Games',
      price: 699,
      image: 'assets/Monopoly.avif'
    },

    {
      id: 8,
      name: 'Cricket Bat',
      category: 'Outdoor Toys',
      price: 1099,
      image: 'assets/Cricket bat.jpg'
    },

    {
      id: 9,
      name: 'Mini Kitchen Set',
      category: 'Pretend Play',
      price: 849,
      image: 'assets/mini kitchen set.jpg'
    },

    {
      id: 10,
      name: 'Robot Buddy',
      category: 'Electronic Toys',
      price: 1899,
      image: 'assets/robot buddy.jpg',
      tag: 'Featured'
    }

  ];


  // =========================================
  // CART
  // =========================================

  cart: CartItem[] = [];


  // =========================================
  // CUSTOMER / CHECKOUT
  // =========================================

  customer = {

    firstName: '',

    lastName: '',

    email: '',

    phone: '',

    address: '',

    city: '',

    state: '',

    pincode: '',

    payment: 'COD'

  };


  // =========================================
  // GET ALL CATEGORIES
  // =========================================

  get categories(): string[] {

    return [
      'All',
      ...Array.from(
        new Set(
          this.products.map(product => product.category)
        )
      )
    ];

  }


  // =========================================
  // FILTER PRODUCTS
  // =========================================

  get filteredProducts(): Product[] {

    if (this.selectedCategory === 'All') {

      return this.products;

    }

    return this.products.filter(
      product =>
        product.category === this.selectedCategory
    );

  }


  // =========================================
  // CART ITEM COUNT
  // =========================================

  get cartCount(): number {

    return this.cart.reduce(
      (total, item) =>
        total + item.quantity,
      0
    );

  }


  // =========================================
  // CART SUBTOTAL
  // =========================================

  get subtotal(): number {

    return this.cart.reduce(
      (total, item) =>
        total + (item.price * item.quantity),
      0
    );

  }


  // =========================================
  // DELIVERY CHARGE
  // =========================================

  get delivery(): number {

    // Free delivery above ₹2000

    if (this.subtotal >= 2000) {

      return 0;

    }

    // No delivery charge for empty cart

    if (this.subtotal === 0) {

      return 0;

    }

    // Standard delivery

    return 99;

  }


  // =========================================
  // FINAL TOTAL
  // =========================================

  get total(): number {

    return this.subtotal + this.delivery;

  }


  // =========================================
  // FORMAT MONEY
  // =========================================

  money(value: number): string {

    return new Intl.NumberFormat(
      'en-IN',
      {
        style: 'currency',

        currency: 'INR',

        maximumFractionDigits: 0
      }
    ).format(value);

  }


  // =========================================
  // ADD PRODUCT TO CART
  // =========================================

  addToCart(product: Product): void {

    const existingItem =
      this.cart.find(
        item => item.id === product.id
      );


    // Product already exists in cart

    if (existingItem) {

      existingItem.quantity++;

      return;

    }


    // Add new product

    this.cart.push({

      ...product,

      quantity: 1

    });

  }


  // =========================================
  // INCREASE / DECREASE QUANTITY
  // =========================================

  changeQuantity(
    item: CartItem,
    amount: number
  ): void {

    item.quantity += amount;


    // Remove product when quantity reaches zero

    if (item.quantity <= 0) {

      this.cart =
        this.cart.filter(
          cartItem =>
            cartItem.id !== item.id
        );

    }

  }


  // =========================================
  // REMOVE PRODUCT FROM CART
  // =========================================

  removeFromCart(
    item: CartItem
  ): void {

    this.cart =
      this.cart.filter(
        cartItem =>
          cartItem.id !== item.id
      );

  }


  // =========================================
  // OPEN CHECKOUT
  // =========================================

  openCheckout(): void {

    // Don't open checkout if cart is empty

    if (!this.cart.length) {

      return;

    }


    this.cartOpen = false;

    this.checkoutOpen = true;

    this.orderPlaced = false;


    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }


  // =========================================
  // PLACE ORDER
  // =========================================

  placeOrder() {
  const order = {
    customerEmail: this.customer.email,
    phone: this.customer.phone,
    shippingAddress: this.customer.address,
    city: this.customer.city,
    state: this.customer.state,
    pincode: this.customer.pincode,
    paymentMethod: 'DEMO',
    items: this.cart.map(item => ({
      productId: item.id,
      quantity: item.quantity
    }))
  };

  console.log('Sending order to backend:', order);

  this.apiService.createOrder(order).subscribe({
    next: (response) => {
      console.log('Order saved successfully:', response);
      this.orderPlaced = true;
      this.cart = [];
    },
    error: (error) => {
      console.error('Order save failed:', error);
      alert('Order could not be saved. Check the backend.');
    }
  });
}

  // =========================================
  // CONTINUE SHOPPING
  // =========================================

  continueShopping(): void {

    this.checkoutOpen = false;

    this.orderPlaced = false;


    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }

}