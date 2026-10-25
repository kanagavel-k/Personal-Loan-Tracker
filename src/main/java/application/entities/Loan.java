package application.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "loan")
@Data
public class Loan {
	
	 @Id
	    private int id;

	    private String loanName;
	    private double amount;
	    private int periodMonths;
	    private String startDate;
	    private String dueDate;
	    private double fineAmount;

	    @ManyToOne
	    private Person person;

	    @OneToMany(mappedBy = "loan")
	    @OrderBy("installmentNumber")
	    private List<Installment> installments = new ArrayList<>();

	    @OneToMany(mappedBy = "loan")
	    private List<Payment> payments = new ArrayList<>();
}
