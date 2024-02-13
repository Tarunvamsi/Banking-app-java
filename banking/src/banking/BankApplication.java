package banking;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class BankApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankSystem bankSystem = loadBankSystem(); // Load existing data

        try {
            System.out.println("Welcome to the Bank Application!");

            int choice;
            do {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Create Account");
                System.out.println("2. Perform Operations on Existing Accounts");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        createAccount(scanner, bankSystem);
                        break;
                    case 2:
                        performOperations(scanner, bankSystem);
                        break;
                    case 0:
                        System.out.println("Exiting program. Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void createAccount(Scanner scanner, BankSystem bankSystem) {
        System.out.print("Enter your name: ");
        String owner = scanner.nextLine();

        double initialBalance;
        do {
            System.out.print("Enter initial balance: ");
            initialBalance = scanner.nextDouble();
            if (initialBalance < 0) {
                System.out.println("Invalid initial balance. Please enter a non-negative amount.");
            }
        } while (initialBalance < 0);

        System.out.print("Choose account type (1 for BankAccount, 2 for SavingsAccount): ");
        int accountType = scanner.nextInt();

        BankAccount account;
        if (accountType == 1) {
            account = new BankAccount(owner, initialBalance,bankSystem);
        } else if (accountType == 2) {
            System.out.print("Enter interest rate for SavingsAccount: ");
            double interestRate = scanner.nextDouble();
            account = new SavingsAccount(owner, initialBalance, interestRate, bankSystem);
        } else {
            System.out.println("Invalid account type.");
            return;
        }

        bankSystem.addAccount(account);
        saveBankSystem(bankSystem); // Save updated data

        System.out.println("Account created successfully!");
        printAccountDetails(account);
    }

    private static void performOperations(Scanner scanner, BankSystem bankSystem) {
        try {
            int choice;
            do {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Transfer Amount");
                System.out.println("4. Print Account Details");
                System.out.println("5. Add Interest (SavingsAccount only)");
                System.out.println("6. Terminate Account");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        performDeposit(scanner, bankSystem);
                        break;
                    case 2:
                        performWithdrawal(scanner, bankSystem);
                        break;
                    case 3:
                        performTransfer(scanner, bankSystem);
                        break;
                    case 4:
                        performPrintDetails(bankSystem);
                        break;
                    case 5:
                        performAddInterest(scanner, bankSystem);
                        break;
                    case 6:
                        terminateAccount(scanner, bankSystem);
                        break;    
                    case 0:
                        System.out.println("Exiting operations. Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    private static void terminateAccount(Scanner scanner, BankSystem bankSystem) {
        try {
            System.out.print("Enter account number to terminate: ");
            int accountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            BankAccount account = findAccountByNumber(accountNumber, bankSystem);
            if (account != null) {
                account.terminateAccount();
                bankSystem.removeAccount(account);
                saveBankSystem(bankSystem); // Save updated data
                System.out.println("Account terminated successfully!");
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void performTransfer(Scanner scanner, BankSystem bankSystem) {
        try {
            System.out.print("Enter sender's account number: ");
            int senderAccountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            BankAccount senderAccount = findAccountByNumber(senderAccountNumber, bankSystem);
            if (senderAccount != null) {
                System.out.print("Enter recipient's account number: ");
                int recipientAccountNumber = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                BankAccount recipientAccount = findAccountByNumber(recipientAccountNumber, bankSystem);
                if (recipientAccount != null) {
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    if (transferAmount > 0 && transferAmount <= senderAccount.getBalance()) {
                        senderAccount.withdraw(transferAmount);
                        recipientAccount.deposit(transferAmount);
                        saveBankSystem(bankSystem); // Save updated data
                        System.out.println("Transfer successful!");
                    } else {
                        System.out.println("Invalid transfer amount or insufficient funds.");
                    }
                } else {
                    System.out.println("Recipient account not found.");
                }
            } else {
                System.out.println("Sender account not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    private static void performDeposit(Scanner scanner, BankSystem bankSystem) {
        try {
            System.out.print("Enter account number to deposit into: ");
            int accountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            BankAccount selectedAccount = findAccountByNumber(accountNumber, bankSystem);
            if (selectedAccount != null) {
                System.out.print("Enter deposit amount: ");
                double depositAmount = scanner.nextDouble();
                selectedAccount.deposit(depositAmount);
                saveBankSystem(bankSystem); // Save updated data
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void performWithdrawal(Scanner scanner, BankSystem bankSystem) {
        try {
            System.out.print("Enter account number to withdraw from: ");
            int accountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            BankAccount selectedAccount = findAccountByNumber(accountNumber, bankSystem);
            if (selectedAccount != null) {
                System.out.print("Enter withdrawal amount: ");
                double withdrawalAmount = scanner.nextDouble();
                selectedAccount.withdraw(withdrawalAmount);
                saveBankSystem(bankSystem); // Save updated data
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void performPrintDetails(BankSystem bankSystem) {
        try {
            System.out.print("Enter account number to view details: ");
            Scanner scanner = new Scanner(System.in);
            int accountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            BankAccount selectedAccount = findAccountByNumber(accountNumber, bankSystem);
            if (selectedAccount != null) {
                printAccountDetails(selectedAccount);
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void performAddInterest(Scanner scanner, BankSystem bankSystem) {
        try {
            System.out.print("Enter account number to add interest to (SavingsAccount only): ");
            int accountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            BankAccount selectedAccount = findAccountByNumber(accountNumber, bankSystem);
            if (selectedAccount instanceof SavingsAccount) {
                ((SavingsAccount) selectedAccount).addInterest();
                saveBankSystem(bankSystem); // Save updated data
            } else {
                System.out.println("This operation is only applicable to SavingsAccount.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static BankAccount findAccountByNumber(int accountNumber, BankSystem bankSystem) {
        for (BankAccount account : bankSystem.getAccounts()) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    private static void printAccountDetails(BankAccount account) {
        System.out.println("\nAccount Details:");
        System.out.println("Owner: " + account.getOwner());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Balance: $" + account.getBalance());
    }

    private static void saveBankSystem(BankSystem bankSystem) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("bank_data.ser"))) {
            outputStream.writeObject(bankSystem);
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private static BankSystem loadBankSystem() {
    	try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("bank_data.ser"))) {
            BankSystem bankSystem = (BankSystem) inputStream.readObject();

            // Set the last assigned account number based on the existing account numbers
            int maxAccountNumber = bankSystem.getAccounts().stream()
                    .mapToInt(BankAccount::getAccountNumber)
                    .max()
                    .orElse(0);
            bankSystem.setLastAssignedAccountNumber(maxAccountNumber);

            return bankSystem;
        } catch (IOException | ClassNotFoundException e) {
            // If file not found or other errors, return a new BankSystem
            return new BankSystem();
        }
    }
}