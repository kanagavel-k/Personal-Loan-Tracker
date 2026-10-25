package application.utility;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import application.entities.Installment;
import application.entities.Loan;
import application.entities.Payment;
import application.entities.Person;



public class HibernateUtil {

	     static SessionFactory sf;
	         
	    public static SessionFactory connect() {
	    	Configuration c = new Configuration();
	    	
	    	c.configure();
	    	 c.addAnnotatedClass(Person.class);
	         c.addAnnotatedClass(Loan.class);
	         c.addAnnotatedClass(Installment.class);
	         c.addAnnotatedClass(Payment.class);
	    	
	    	sf = c.buildSessionFactory();
	  
	        return sf;
	    }
	    
	}

