package banking;
import java.io.Serializable;
public class BankAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int nextAccountNumber = 1;

    private String owner;
    private int accountNumber;
    private double balance;

    public BankAccount(String owner, double initialBalance, BankSystem bankSystem ) {
        this.owner = owner;
        this.accountNumber = bankSystem.generateAccountNumber();
        this.balance = initialBalance;
    }

    private static int getNextAccountNumber() {
        return nextAccountNumber++;
    }

    public String getOwner() {
        return owner;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid deposit amount. Amount must be greater than 0.");
        }
        this.balance += amount;
    }

    // Updated withdraw method with "throws" clause to propagate exceptions
    public void withdraw(double amount) throws InsufficientFundsException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid withdrawal amount. Amount must be greater than 0.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds to withdraw $" + amount);
        }
        this.balance -= amount;
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void finalize() throws Throwable 
    {
        try {
            System.out.println("Finalizing BankAccount with account number: " + getAccountNumber());
            // Add any cleanup operations, if necessary
        } finally {
            super.finalize();
        }
    }
    
    public void terminateAccount() {
        // Perform any necessary cleanup operations

        // Set all sensitive information to null or a default value
        this.owner = null;
        this.balance = 0;
        

      
    }
}
