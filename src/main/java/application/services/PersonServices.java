package application.services;

import java.util.Scanner;
import org.hibernate.Session;
import application.dao.PersonDao;
import application.entities.Loan;
import application.entities.Person;

public class PersonServices implements PersonDao{
	 private Session session;
	    private Scanner sc;

	    public void setSession(Session session) { 
	    	this.session = session; 
	    }
	    public void setScanner(Scanner sc) { 
	    	this.sc = sc;
	    }

	    @Override
	    public void insert() {
	        Person p = new Person();
	        System.out.print("Enter name: ");
	        p.setName(sc.nextLine());
	        System.out.print("Enter phone: ");
	        p.setPhone(sc.nextLine());
	        System.out.print("Enter address: ");
	        p.setAddress(sc.nextLine());
	        
	        int newId = 1;
	        while (session.get(Person.class, newId) != null) {
	            newId++;
	        }
	        p.setId(newId);
	        
	        session.persist(p);
	        session.flush();
	        System.out.println("Person added. ID: " + p.getId());
	    }

	    @Override
	    public void retrieve() {
	        System.out.print("Enter person ID: ");
	        int id = sc.nextInt();
	        sc.nextLine();
	        Person p = session.get(Person.class, id);
	        if (p == null) {
	            System.out.println("Person not found.");
	            return;
	        }

	        System.out.println(p.getId() + " | " + p.getName() + " | "
	                + p.getPhone() + " | " + p.getAddress());

	        if (p.getLoans().isEmpty()) {
	            System.out.println("No loans.");
	        }
	        for (Loan l : p.getLoans()) {
	            System.out.println("  Loan " + l.getId() + " | " + l.getLoanName()
	                    + " | " + l.getAmount());
	        }
	    }

	    @Override
	    public void update() {
	        System.out.print("Enter person ID: ");
	        int id = sc.nextInt();
	        sc.nextLine();

	        Person p = session.get(Person.class, id);
	        if (p == null) {
	            System.out.println("Person not found.");
	            return;
	        }

	        System.out.print("Enter new name: ");
	        p.setName(sc.nextLine());
	        System.out.print("Enter new phone: ");
	        p.setPhone(sc.nextLine());
	        System.out.print("Enter new address: ");
	        p.setAddress(sc.nextLine());
	        System.out.println("Person updated.");
	    }

	    @Override
	    public void delete() {
	        System.out.print("Enter person ID: ");
	        int id = sc.nextInt();
	        sc.nextLine();

	        Person p = session.get(Person.class, id);
	        if (p == null) {
	            System.out.println("Person not found.");
	        } else if (!p.getLoans().isEmpty()) {
	            System.out.println("Delete the person's loans first.");
	        } else {
	            session.remove(p);
	            System.out.println("Person deleted.");
	        }
	    }
}
