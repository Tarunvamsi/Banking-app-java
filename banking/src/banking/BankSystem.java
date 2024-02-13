package banking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class BankSystem implements Serializable {
 
	private static final long serialVersionUID = 1L;
	private List<BankAccount> accounts;
	private int lastAssignedAccountNumber;

    public BankSystem() {
        this.accounts = new ArrayList<>();
        this.lastAssignedAccountNumber = 0;
    }
    public int generateAccountNumber() {
        // Increment the last assigned account number and return it
        return ++lastAssignedAccountNumber;
    }
    public void setLastAssignedAccountNumber(int lastAssignedAccountNumber) {
        this.lastAssignedAccountNumber = lastAssignedAccountNumber;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }
    public void removeAccount(BankAccount account) {
        accounts.remove(account);
        System.gc();
    }
}