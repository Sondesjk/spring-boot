package tn.projetdemo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.projetdemo.demo.entities.CheckoutPayment;

public interface CheckoutPaymentRepository extends JpaRepository<CheckoutPayment, Long>{

}
