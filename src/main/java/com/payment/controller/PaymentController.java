package com.payment.controller;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.controller.reques.PaymentRequest;
import com.payment.entity.Account;
import com.payment.entity.Payment;
import com.payment.entity.Product;
import com.payment.entity.User;
import com.payment.repository.AccountRepository;
import com.payment.repository.PaymentRepository;
import com.payment.repository.ProductRepository;
import com.payment.repository.UserRepository;

@RestController
@RequestMapping("api/payments")
public class PaymentController {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping
	public List<Payment> getAll() {
		return (List<Payment>) paymentRepository.findAll();
	}
	
	@PostMapping("/{userId}/{accountId}")
	public String createPayment(@PathVariable Integer userId,@PathVariable Integer accountId, @RequestBody PaymentRequest paymentRequest) {
		User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException());
		Account account = accountRepository.findById(accountId).orElseThrow(()-> new RuntimeException());
		List<Product> products = paymentRequest
				.getProducts()
				.stream()
				.map(p -> productRepository.findById(p.getId()).orElseThrow(()-> new RuntimeException()))
				.collect(Collectors.toList());
		BigDecimal totalPrice = BigDecimal.ZERO;
		for(Product p : products) {
			totalPrice = totalPrice.add(p.getPrice());
		}
		Payment payment = new Payment();
		payment.setUser(user);
		payment.setAccount(account);
		payment.setProducts(products);
		payment.setTotalPrice(totalPrice);
		payment = paymentRepository.save(payment);
		byte[] paymentInBytes = String.valueOf(payment.getId()).getBytes();
		return new StringBuilder("http://localhost:8080/")
				.append("/api/v1/payments/")
				.append(Base64.getEncoder().encodeToString(paymentInBytes))
				.toString();
	}
	
	@PostMapping("/{paymentHash}")
	public Payment confirmPayment(@PathVariable String paymentHash) {
		String decoded = new String(Base64.getDecoder().decode(paymentHash));
		Payment payment = paymentRepository.findById(Integer.parseInt(decoded)).orElseThrow(()-> new RuntimeException());
		payment.setConfirmed(true);
		return payment;
	}
}
