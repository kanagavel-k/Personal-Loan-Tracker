package application.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "person")
@Data
public class Person {
	
	 @Id
	    private int id;

	    private String name;
	    private String phone;
	    private String address;
 
	    @OneToMany(mappedBy = "person")
	    private List<Loan> loans = new ArrayList<>();
}
