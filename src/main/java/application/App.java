package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import application.services.InstallmentServices;
import application.services.LoanServices;
import application.services.PaymentServices;
import application.services.PersonServices;
import application.utility.HibernateUtil;

public class App {
	
	public static void main(String[] args) {
		SessionFactory sf = HibernateUtil.connect();
		 Scanner sc = new Scanner(System.in);

	        while (true) {
	            System.out.println("\n===== PERSONAL LOAN SYSTEM =====");
	            System.out.println("1. Person");
	            System.out.println("2. Loan");
	            System.out.println("3. Installment");
	            System.out.println("4. Payment");
	            System.out.println("0. Exit");
	            System.out.print("Choice: ");
	            int entity = readInt(sc);

	            if (entity == 0) break;
	            if (entity < 1 || entity > 4) {
	                System.out.println("Invalid choice.");
	                continue;
	            }

	            System.out.println("\n1. Insert");
	            System.out.println("2. Retrieve");
	            System.out.println("3. Update");
	            System.out.println("4. Delete");
	            System.out.println("0. Back");
	            System.out.print("Choice: ");
	            int action = readInt(sc);

	            if (action == 0) continue;
	            if (action < 1 || action > 4) {
	                System.out.println("Invalid choice.");
	                continue;
	            }

	            Session s = sf.openSession();
	            Transaction t = s.beginTransaction();
	            try {
	                run(entity, action, s, sc);
	                t.commit();
	            } catch (InputMismatchException e) {
	                t.rollback();
	                sc.nextLine(); 
	                System.out.println("Invalid input. Enter a number.");
	            } catch (Exception e) {
	                t.rollback();
	                System.out.println("Error: " + e.getMessage());
	            } finally {
	                s.close();
	            }
	        }
	        sc.close();
	        sf.close();
	    }

	private static int readInt(Scanner sc) {
	    try {
	        int n = sc.nextInt();
	        sc.nextLine();
	        return n;
	    } catch (InputMismatchException e) {
	        sc.nextLine();
	        return -1;
	    }
	}

	    private static void run(int entity, int action, Session s, Scanner sc) {
	        if (entity == 1) {
	            PersonServices x = new PersonServices();
	            x.setSession(s);
	            x.setScanner(sc);
	            if (action == 1) {
	            	x.insert();
	            }
	            else if (action == 2) {
	            	x.retrieve();
	            }
	            else if (action == 3) {
	            	x.update();
	            }
	            else {
	            	x.delete();
	            }
	        } 
	        else if (entity == 2) {
	            LoanServices x = new LoanServices();
	            x.setSession(s);
	            x.setScanner(sc);
	            if (action == 1) {
	            	x.insert();
	            }
	            else if (action == 2) {
	            	x.retrieve();
	            }
	            else if (action == 3) {
	            	x.update();
	            }
	            else {
	            	x.delete();
	            }
	        } 
	        else if (entity == 3) {
	            InstallmentServices x = new InstallmentServices();
	            x.setSession(s);
	            x.setScanner(sc);
	            if (action == 1) {
	            	x.insert();
	            }
	            else if (action == 2) {
	            	x.retrieve();
	            }
	            else if (action == 3) {
	            	x.update();
	            }
	            else {
	            	x.delete();
	            }
	        } 
	        else {
	            PaymentServices x = new PaymentServices();
	            x.setSession(s);
	            x.setScanner(sc);
	            if (action == 1) {
	            	x.insert();
	            }
	            else if (action == 2) {
	            	x.retrieve();
	            }
	            else if (action == 3) {
	            	x.update();
	            }
	            else {
	            	x.delete();
	            }
	        }
	    }
	}
