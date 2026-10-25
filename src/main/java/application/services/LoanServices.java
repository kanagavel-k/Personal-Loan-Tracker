package application.services;


import java.time.LocalDate;
import java.util.Scanner;
import org.hibernate.Session;
import application.dao.LoanDao;
import application.entities.Installment;
import application.entities.Loan;
import application.entities.Payment;
import application.entities.Person;
import application.utility.DateUtil;


public class LoanServices implements LoanDao {
	
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
	        System.out.print("Enter person ID: ");
	        int personId = sc.nextInt();
	        sc.nextLine();

	        Person person = session.get(Person.class, personId);
	        if (person == null) {
	            System.out.println("Person not found.");
	            return;
	        }

	        Loan loan = new Loan();
	        loan.setPerson(person);

	        System.out.print("Enter loan name: ");
	        loan.setLoanName(sc.nextLine());
	        System.out.print("Enter loan amount: ");
	        loan.setAmount(sc.nextDouble());
	        sc.nextLine();
	        System.out.print("Enter period in months: ");
	        loan.setPeriodMonths(sc.nextInt());
	        sc.nextLine();
	        System.out.print("Enter start date (yyyy-MM-dd): ");
	        loan.setStartDate(sc.nextLine());
	        System.out.print("Enter final due date (yyyy-MM-dd): ");
	        loan.setDueDate(sc.nextLine());
	        System.out.print("Enter fine per overdue installment: ");
	        loan.setFineAmount(sc.nextDouble());
	        sc.nextLine();
	        if (loan.getAmount() <= 0 || loan.getPeriodMonths() <= 0
	                || loan.getFineAmount() < 0
	                || !DateUtil.isValid(loan.getStartDate())
	                || !DateUtil.isValid(loan.getDueDate())
	                || loan.getDueDate().compareTo(loan.getStartDate()) < 0) {
	            System.out.println("Invalid loan details.");
	            return;
	        }
	        
	        int newId = 1;
	        while (session.get(Loan.class, newId) != null) {
	            newId++;
	        }
	        loan.setId(newId);
	        
	        session.persist(loan);
	        session.flush();

	        // generate installments automatically
	        LocalDate start = LocalDate.parse(loan.getStartDate());
	        double regular = Math.round(loan.getAmount() / loan.getPeriodMonths() * 100.0) / 100.0;
	        double total = 0;
	        int instId = 1;
	        
	        for (int n = 1; n <= loan.getPeriodMonths(); n++) {
	            double amt = regular;
	            String due = start.plusMonths(n).toString();

	            if (n == loan.getPeriodMonths()) {
	                amt = Math.round((loan.getAmount() - total) * 100.0) / 100.0;
	                due = loan.getDueDate();
	            }
	            total = total + amt;

	            Installment i = new Installment();
	            i.setInstallmentNumber(n);
	            i.setAmount(amt);
	            i.setPaidAmount(0);
	            i.setDueDate(due);
	            i.setPaid(false);
	            i.setLoan(loan);
	            
	            while (session.get(Installment.class, instId) != null) {
	                instId++;
	            }
	            i.setId(instId);
	            
	            session.persist(i);
	        }

	        System.out.println("Loan added. ID: " + loan.getId()
	                + " | " + loan.getPeriodMonths() + " installments generated.");
	    }

	    @Override
	    public void retrieve() {
	        System.out.print("Enter loan ID: ");
	        int id = sc.nextInt();
	        sc.nextLine();

	        Loan l = session.get(Loan.class, id);
	        if (l == null) {
	            System.out.println("Loan not found.");
	            return;
	        }

	        System.out.println("ID: " + l.getId()
	                + " | " + l.getLoanName()
	                + " | Person: " + l.getPerson().getName()
	                + " | Amount: " + l.getAmount()
	                + " | Months: " + l.getPeriodMonths()
	                + " | Start: " + l.getStartDate()
	                + " | Due: " + l.getDueDate()
	                + " | Fine: " + l.getFineAmount());

	        int overdue = 0;
	        LocalDate today = LocalDate.now();

	        System.out.println("Installments:");
	        for (Installment i : l.getInstallments()) {
	            System.out.println("  ID: " + i.getId()
	                    + " | No: " + i.getInstallmentNumber()
	                    + " | Amount: " + i.getAmount()
	                    + " | Paid: " + i.getPaidAmount()
	                    + " | Due: " + i.getDueDate()
	                    + " | Status: " + (i.isPaid() ? "PAID" : "UNPAID"));

	            if (!i.isPaid() && LocalDate.parse(i.getDueDate()).isBefore(today)) {
	                overdue++;
	            }
	        }

	        System.out.println("Payments:");
	        for (Payment p : l.getPayments()) {
	            System.out.println("  ID: " + p.getId()
	                    + " | Amount: " + p.getAmount()
	                    + " | Date: " + p.getPaymentDate());
	        }

	        System.out.println("Overdue installments: " + overdue
	                + " | Total fine: " + (overdue * l.getFineAmount()));
	    }

	    @Override
	    public void update() {
	        System.out.print("Enter loan ID: ");
	        int id = sc.nextInt();
	        sc.nextLine();

	        Loan l = session.get(Loan.class, id);
	        if (l == null) {
	            System.out.println("Loan not found.");
	            return;
	        }

	        System.out.print("Enter new loan name: ");
	        l.setLoanName(sc.nextLine());

	        System.out.print("Enter new fine amount: ");
	        double fine = sc.nextDouble();
	        sc.nextLine();

	        if (fine >= 0) {
	            l.setFineAmount(fine);
	            System.out.println("Loan updated.");
	        } else {
	            System.out.println("Fine cannot be negative.");
	        }
	    }

	    @Override
	    public void delete() {
	        System.out.print("Enter loan ID: ");
	        int id = sc.nextInt();
	        sc.nextLine();

	        Loan l = session.get(Loan.class, id);
	        if (l == null) {
	            System.out.println("Loan not found.");
	            return;
	        }

	        boolean hasPayments = !l.getPayments().isEmpty();
	        boolean hasUnpaid = false;

	        for (Installment i : l.getInstallments()) {
	            if (!i.isPaid()) {
	                hasUnpaid = true;
	            }
	        }

	        if (hasPayments && hasUnpaid) {
	            System.out.println("Loan is in progress. Cannot delete until all installments are paid.");
	            return;
	        }

	        for (Payment p : l.getPayments()) {
	            session.remove(p);
	        }
	        for (Installment i : l.getInstallments()) {
	            session.remove(i);
	        }
	        session.remove(l);

	        System.out.println("Loan deleted.");
	    }
}
