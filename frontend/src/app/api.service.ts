import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getProducts() {
    return this.http.get<any[]>(`${this.apiUrl}/products`);
  }

  createOrder(order: any) {
    return this.http.post<any>(`${this.apiUrl}/orders`, order);
  }

  getOrders() {
    return this.http.get<any[]>(`${this.apiUrl}/orders`);
  }
}