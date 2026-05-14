import java.util.Scanner;
import java.util.InputMismatchException;

// 1. Custom Checked Exception Class
class InsufficientBalanceException extends Exception { // Extends Exception to make it checked 
    private double balance;
    private double amount;

    public InsufficientBalanceException(double balance, double amount) {
        super("Insufficient balance. Current: $" + balance + ", Requested: $" + amount);
        this.balance = balance;
        this.amount = amount;
    }

    public double getBalance() { return balance; }
    public double getAmount() { return amount; }
}

// 2. Core Bank Class containing the execution entry point (No separate Main class)
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    // Business logic for deposit (Throws Unchecked Exception) 
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        System.out.println("$" + amount + " successfully deposited.");
    }

    // Business logic for withdrawal (Throws Checked Exception) [cite: 94, 98]
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(balance, amount);
        }
        balance -= amount;
        System.out.println("$" + amount + " successfully withdrawn.");
    }

    // Interactive main method inside the same class
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); // Initialize Scanner for user input 
        BankAccount account = new BankAccount(500.0); // Create account with initial balance $500

        System.out.println("=== Welcome to the Interactive Banking System ===");
        System.out.println("Initial Balance: $500.0");

        try {
            // Step 1: Interactive Deposit Test
            System.out.print("\nEnter the amount to DEPOSIT: ");
            double depositAmount = input.nextDouble(); // May throw InputMismatchException 
            account.deposit(depositAmount);

        } catch (InputMismatchException e) { // Catch block for incorrect data types [cite: 44]
            System.out.println("Input Error: Invalid format. A numeric value is required.");
            input.nextLine(); // Discard the invalid input buffer 
        } catch (IllegalArgumentException e) { // Catch block for business rule violation [cite: 114]
            System.out.println("Business Rule Violation: " + e.getMessage());
        } finally {
            // Always executes to show the current state 
            System.out.println("[Current Balance Status] $" + account.getBalance());
        }

        try {
            // Step 2: Interactive Withdrawal Test
            System.out.print("\nEnter the amount to WITHDRAW: ");
            double withdrawAmount = input.nextDouble(); 
            account.withdraw(withdrawAmount);

        } catch (InputMismatchException e) {
            System.out.println("Input Error: Invalid format. A numeric value is required.");
            input.nextLine(); 
        } catch (InsufficientBalanceException e) { // Catch block for custom checked exception [cite: 114]
            System.out.println("Transaction Denied: " + e.getMessage());
        } finally {
            // Always executes regardless of exceptions 
            System.out.println("[Current Balance Status] $" + account.getBalance());
            System.out.println("\n=== Thank you for using our service ===");
            input.close(); // Close the resource
        }
    }
}
