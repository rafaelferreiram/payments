package com.payment.controller.reques;

import java.util.List;

import com.payment.entity.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

	private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
	
}
