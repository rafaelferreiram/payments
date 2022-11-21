package com.payment.service;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.payment.controller.reques.PaymentRequest;
import com.payment.entity.Account;
import com.payment.entity.Payment;
import com.payment.entity.Product;
import com.payment.entity.User;
import com.payment.exception.AccountNotFoundException;
import com.payment.exception.PaymentNotFoundException;
import com.payment.exception.ProductNotFoundException;
import com.payment.exception.UserNotFoundException;
import com.payment.repository.AccountRepository;
import com.payment.repository.PaymentRepository;
import com.payment.repository.ProductRepository;
import com.payment.repository.UserRepository;

@Service
public class PaymentService {
	
	private static final String API_PREFIX = "/api/v1/payments/";

	private static final String URL = "http://localhost:8080/";

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public String createPayment( Integer userId, Integer accountId,  PaymentRequest paymentRequest) {
		User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(String.format("User ID [%s] not found.", userId),HttpStatus.NOT_FOUND));
		Account account = accountRepository.findById(accountId).orElseThrow(()-> new AccountNotFoundException(String.format("Account ID [%s] not found.", accountId),HttpStatus.NOT_FOUND));
		List<Product> products = paymentRequest
				.getProducts()
				.stream()
				.map(p -> productRepository.findById(p.getId()).orElseThrow(()-> new ProductNotFoundException(String.format("Product ID [%s] not found.", p.getId()),HttpStatus.NOT_FOUND)))
				.collect(Collectors.toList());
		BigDecimal totalPrice = BigDecimal.ZERO;
		for(Product p : products) {
			totalPrice = totalPrice.add(p.getPrice());
		}
		Payment paymentCreated = paymentRepository.save(Payment.builder()
				.user(user)
				.account(account)
				.products(products)
				.totalPrice(totalPrice)
				.build());
		
		byte[] paymentInBytes = String.valueOf(paymentCreated.getId()).getBytes();
		return new StringBuilder(URL)
				.append(API_PREFIX)
				.append(Base64.getEncoder().encodeToString(paymentInBytes))
				.toString();
	}

	public List<Payment> findAll() {
		return (List<Payment>) paymentRepository.findAll();
	}
	
	public Payment confirmPayment(@PathVariable String paymentHash) {
		String decoded = new String(Base64.getDecoder().decode(paymentHash));
		Payment payment = paymentRepository.findById(Integer.parseInt(decoded)).orElseThrow(()-> new PaymentNotFoundException(String.format("Payment Hash ID [%s] not found.", paymentHash),HttpStatus.NOT_FOUND));
		payment.setConfirmed(true);
		return payment;
	}
}
