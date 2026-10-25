package application.services;

import java.util.Scanner;
import org.hibernate.Session;
import application.dao.PaymentDao;
import application.entities.Loan;
import application.entities.Payment;
import application.utility.DateUtil;


public class PaymentServices implements PaymentDao{
	
	private Session session;
    private Scanner sc;

    public void setSession(Session session) { this.session = session; }
    public void setScanner(Scanner sc) { this.sc = sc; }

    @Override
    public void insert() {
        System.out.println("Payments are recorded when an installment is paid (Installment -> Update).");
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

        if (loan.getPayments().isEmpty()) {
            System.out.println("No payments.");
        }
        for (Payment p : loan.getPayments()) {
            System.out.println("ID: " + p.getId()
                    + " | Amount: " + p.getAmount()
                    + " | Date: " + p.getPaymentDate());
        }
    }

    @Override
    public void update() {
        System.out.print("Enter payment ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Payment p = session.get(Payment.class, id);
        if (p == null) {
            System.out.println("Payment not found.");
            return;
        }

        System.out.print("Enter new payment date (yyyy-MM-dd): ");
        String date = sc.nextLine();

        if (DateUtil.isValid(date)) {
            p.setPaymentDate(date);
            System.out.println("Payment date updated.");
        } else {
            System.out.println("Invalid date.");
        }
    }

    @Override
    public void delete() {
        System.out.println("Payments are records and cannot be deleted.");
    }
}
