package components;

import java.util.Date;

//1.3.2 Creation of the Flow class
public abstract class Flow {
    private String comment;
    private int identifier;
    private double amount;
    private int targetAccountNumber;
    private boolean effect;
    private Date date;
    
    private static int counter = 1;

    //Constructeur
    public Flow(String comment, double amount, int targetAccountNumber, boolean effect) {
        this.identifier = counter++;
        this.comment = comment;
        this.amount = amount;
        this.targetAccountNumber = targetAccountNumber;
        this.effect = effect;
        this.date = new Date();
    }

    // Getters & Setters
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getIdentifier() { return identifier; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(int targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }

    public boolean isEffect() { return effect; }
    public void setEffect(boolean effect) { this.effect = effect; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }


}
