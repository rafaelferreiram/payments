package com.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.controller.reques.PaymentRequest;
import com.payment.service.PaymentService;

@RestController
@RequestMapping("api/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity getAll() {
		return ResponseEntity.ok(paymentService.findAll());
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/{userId}/{accountId}")
	public ResponseEntity createPayment(@PathVariable Integer userId,@PathVariable Integer accountId, @RequestBody PaymentRequest paymentRequest) {
			String paymentHash = paymentService.createPayment(userId, accountId, paymentRequest);
			return ResponseEntity.ok(paymentHash);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/{paymentHash}")
	public ResponseEntity confirmPayment(@PathVariable String paymentHash) {
		return ResponseEntity.ok(paymentService.confirmPayment(paymentHash));
	}
}
