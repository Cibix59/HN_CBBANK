package components;
// 1.2.1 Creation of the account class


public abstract class Account {
    
    private static int counter = 1;

    protected String label;
    protected double balance;
    protected int accountNumber;
    protected Client client; 


    public Account(String label, Client client) {
        this.label = label;
        this.client = client;
        this.accountNumber = counter++;
        this.balance = 0.0; 
    }

    // --- getter et setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public double getBalance() { return balance; }
    
    //TODO : utiliser Flow type au lieu de double
    public void setBalance(double amount) { 
        this.balance = amount; 
    }

    public int getAccountNumber() { return accountNumber; }
    public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    
    public String toString() {
        return "Compte n°" + accountNumber + " [" + label + "] - Solde : " + balance + "€\n    -> Propriétaire : " + client.toString();
    }
}