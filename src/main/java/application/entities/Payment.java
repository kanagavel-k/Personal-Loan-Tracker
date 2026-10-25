package application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payment")
@Data
public class Payment {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    private double amount;
	    private String paymentDate;

	    @ManyToOne
	    private Loan loan;

}
