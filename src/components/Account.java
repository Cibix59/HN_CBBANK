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

	public void setBalance(Flow flow) {
		//Le mieux serais que cela soit géré directement par les enfants
		switch (flow) {
		case Credit c-> this.balance += c.getAmount();
		case Debit d-> this.balance -= d.getAmount();
		case Transfert t-> {
			if (this.accountNumber == t.getTargetAccountNumber())
				this.balance += t.getAmount();
			else if (this.accountNumber == t.getSourceAccountNumber())
				this.balance -= t.getAmount();
		}
		default -> System.out.println("Type de flux inconnu : " + flow.getClass().getSimpleName());
		}
	}

	public int getAccountNumber() { return accountNumber; }
	public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }

	public Client getClient() { return client; }
	public void setClient(Client client) { this.client = client; }


	public String toString() {
		return "Compte n°" + accountNumber + " [" + label + "] - Solde : " + balance + "€\n    -> Propriétaire : " + client.toString();
	}
}