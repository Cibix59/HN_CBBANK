package components;
//1.3.3 Creation of the Transfert, Credit, Debit classes
public class Transfert extends Flow {
    private int sourceAccountNumber;

	public Transfert(String comment, double amount, int targetAccountNumber, boolean effect,int sourceAccountNumber) {
		super(comment, amount, targetAccountNumber, effect);
		this.sourceAccountNumber = sourceAccountNumber;
	}
	public int getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(int sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }

}
