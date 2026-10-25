package application.services;

import java.util.Scanner;
import org.hibernate.Session;
import application.dao.InstallmentDao;
import application.entities.Installment;
import application.entities.Loan;
import application.entities.Payment;
import application.utility.DateUtil;

public class InstallmentServices implements InstallmentDao {
	
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
	        System.out.println("Installments are generated automatically when a loan is added.");
	    }

	    @Override
	    public void retrieve() {
	        System.out.print("Enter loan ID: ");
	        int loanId = sc.nextInt();
	        sc.nextLine();

	        Loan loan = session.get(Loan.class, loanId);
	        if (loan == null) {
	            System.out.println("Loan not found.");
	            return;
	        }

	        for (Installment i : loan.getInstallments()) {
	            System.out.println("No: " + i.getInstallmentNumber()
	                    + " | Amount: " + i.getAmount()
	                    + " | Paid: " + i.getPaidAmount()
	                    + " | Due: " + i.getDueDate()
	                    + " | Status: " + (i.isPaid() ? "PAID" : "UNPAID"));
	        }
	    
	    }
	    
	    private Installment findInstallment(Loan loan, int number) {
	        for (Installment i : loan.getInstallments()) {
	            if (i.getInstallmentNumber() == number) {
	                return i;
	            }
	        }
	        return null;
	    }
	    
	    @Override
	    public void update() {
	    	System.out.print("Enter loan ID: ");
	    	int loanId = sc.nextInt();
	    	sc.nextLine();

	    	Loan loan = session.get(Loan.class, loanId);
	    	if (loan == null) {
	    	    System.out.println("Loan not found.");
	    	    return;
	    	}

	    	System.out.print("Enter installment number: ");
	    	int number = sc.nextInt();
	    	sc.nextLine();

	    	Installment i = findInstallment(loan, number);
	        if (i == null) {
	            System.out.println("Installment not found.");
	            return;
	        }
	        if (i.isPaid()) {
	            System.out.println("Installment already paid.");
	            return;
	        }

	        double remaining = Math.round((i.getAmount() - i.getPaidAmount()) * 100.0) / 100.0;
	        System.out.println("Remaining amount: " + remaining);

	        System.out.print("Enter payment amount: ");
	        double amount = sc.nextDouble();
	        sc.nextLine();
	        System.out.print("Enter payment date (yyyy-MM-dd): ");
	        String date = sc.nextLine();

	        if (amount <= 0 || amount > remaining || !DateUtil.isValid(date)) {
	            System.out.println("Invalid payment.");
	            return;
	        }

	        Payment p = new Payment();
	        p.setLoan(i.getLoan());
	        p.setAmount(amount);
	        p.setPaymentDate(date);
	        session.persist(p);

	        double newPaid = Math.round((i.getPaidAmount() + amount) * 100.0) / 100.0;
	        i.setPaidAmount(newPaid);
	        if (newPaid >= i.getAmount()) {
	            i.setPaid(true);
	        }

	        System.out.println("Installment payment recorded.");
	    }

	    @Override
	    public void delete() {
	    	System.out.print("Enter loan ID: ");
	    	int loanId = sc.nextInt();
	    	sc.nextLine();

	    	Loan loan = session.get(Loan.class, loanId);
	    	if (loan == null) {
	    	    System.out.println("Loan not found.");
	    	    return;
	    	}

	    	System.out.print("Enter installment number: ");
	    	int number = sc.nextInt();
	    	sc.nextLine();

	    	Installment i = findInstallment(loan, number);
	        if (i == null) {
	            System.out.println("Installment not found.");
	        } else if (i.getPaidAmount() > 0) {
	            System.out.println("Cannot delete an installment with payments.");
	        } else {
	            session.remove(i);
	            System.out.println("Installment deleted.");
	        }
	    }
}
