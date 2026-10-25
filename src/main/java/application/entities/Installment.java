package application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "installment")
@Data
public class Installment {
	
	@Id
    private int id;

    private int installmentNumber;
    private double amount;
    private double paidAmount;
    private String dueDate;
    private boolean paid;

    @ManyToOne
    private Loan loan;
    
}
