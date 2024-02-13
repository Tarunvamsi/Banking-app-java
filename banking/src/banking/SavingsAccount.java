package banking;
import java.io.*;
public class SavingsAccount extends BankAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	private double interestRate;

    public SavingsAccount(String owner, double initialBalance, double interestRate, BankSystem bankSystem) {
        super(owner, initialBalance,bankSystem);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = getBalance() * interestRate / 100;
        deposit(interest);
        System.out.println("Added interest to account " + getAccountNumber() + ": $" + interest);
    }
    @Override
    protected void finalize() throws Throwable {
        try {
            System.out.println("Finalizing SavingsAccount with account number: " + getAccountNumber());
            // Add any cleanup operations specific to SavingsAccount, if necessary
        } finally {
            super.finalize();
        }
    }
}
