package tn.projetdemo.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import tn.projetdemo.demo.entities.CheckoutPayment;
import tn.projetdemo.demo.entities.Voiture;
import tn.projetdemo.demo.repository.CheckoutPaymentRepository;
import tn.projetdemo.demo.repository.VoitureRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class StripeController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CheckoutPaymentRepository checkoutPaymentRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    @PostMapping("/payment")
    public String paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException, JsonProcessingException {
        init();

        // Retrieve the associated Voiture
        Voiture voiture = voitureRepository.findById(payment.getVoiture().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Voiture ID"));

        payment.setVoiture(voiture);
        
        // Save the payment information to the database
        checkoutPaymentRepository.save(payment);

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(payment.getSuccessUrl())
                .setCancelUrl(payment.getCancelUrl())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(payment.getQuantity())
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(payment.getCurrency())
                                .setUnitAmount(payment.getAmount())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(payment.getName())
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", session.getId());

        return objectMapper.writeValueAsString(responseData);
    }

    private static void init() {
        Stripe.apiKey = "sk_test_51PJ1GR073jX2jThEahHp4KbjYu71t4JWmDaN9ZgYi2ZPfIpRNwJG11Ux84vg1CTjLNQOz7DTfMohfCvEKv8gE8se00xP5pOojE";
    }
}