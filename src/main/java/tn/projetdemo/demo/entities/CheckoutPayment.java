package tn.projetdemo.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.*;;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CheckoutPayment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
	private String currency;
	private String successUrl;
	private String cancelUrl;
	private long amount;
	private long quantity;
	private Date dateTransaction = new Date();
	@ManyToOne
    @JoinColumn(name = "voiture_id")
    private Voiture voiture;
	public CheckoutPayment(Long id, String name, String currency, String successUrl, String cancelUrl, long amount,
			long quantity, Date dateTransaction, Voiture voiture) {
		this.id = id;
		this.name = name;
		this.currency = currency;
		this.successUrl = successUrl;
		this.cancelUrl = cancelUrl;
		this.amount = amount;
		this.quantity = quantity;
		this.dateTransaction = dateTransaction;
		this.voiture = voiture;
	}

	

	
}
