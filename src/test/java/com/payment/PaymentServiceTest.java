package com.payment;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.payment.controller.reques.PaymentRequest;
import com.payment.entity.Account;
import com.payment.entity.Payment;
import com.payment.entity.Product;
import com.payment.entity.User;
import com.payment.repository.AccountRepository;
import com.payment.repository.PaymentRepository;
import com.payment.repository.ProductRepository;
import com.payment.repository.UserRepository;
import com.payment.service.PaymentService;

public class PaymentServiceTest {
	
	@Mock
	private PaymentRepository paymentRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	private PaymentService paymentService;
	
	@BeforeEach
	public void onInit() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void shouldCreatePayment() {
		List<Product> products = new ArrayList<>();
		products.add(Product.builder().id(1).name("teste").build());
		PaymentRequest request = PaymentRequest.builder().products(products).build();
		
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(User.builder().id(1).build()));
		when(accountRepository.findById(anyInt())).thenReturn(Optional.of(Account.builder().id(1).build()));
		when(productRepository.findById(anyInt())).thenReturn(Optional.of(Product.builder().id(1).price(new BigDecimal(10)).build()));
		when(paymentRepository.save(any(Payment.class))).thenReturn(Payment.builder().id(1).build());
		
		String codedPaymentId = paymentService.createPayment(1, 1, request);
		
		byte[] paymentInBytes = String.valueOf(1).getBytes();
		assertTrue(codedPaymentId.contains(Base64.getEncoder().encodeToString(paymentInBytes)));
	}
	

	@Test
	public void shouldListPayment() {
		List<Payment> payments = new ArrayList<>();
		payments.add(Payment.builder().id(1).build());
		
		when(paymentRepository.findAll()).thenReturn(payments);
		
		List<Payment> response = paymentService.findAll();
		
		assertTrue(response.contains(Payment.builder().id(1).build()));
	}
	
	@Test
	public void shouldConfirmPayment() {
		
		when(paymentRepository.findById(anyInt())).thenReturn(Optional.of(Payment.builder().id(1).build()));
		
		Payment response = paymentService.confirmPayment("MQ==");
		
		assertTrue(response.isConfirmed());
	}

}
