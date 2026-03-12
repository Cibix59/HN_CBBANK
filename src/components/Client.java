package components;
//1.1.1 Creation of the client class

class Client {

    private static int cpt = 1; 

    private String name;
    private String firstName;
    private int clientNumber;

    public Client(String name, String firstName) {
        this.name = name;
        this.firstName = firstName;
        this.clientNumber = cpt++; 
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public int getClientNumber() { return clientNumber; }
    public void setClientNumber(int clientNumber) { this.clientNumber = clientNumber; }
    
    public String toString() {
        return "numero :" + clientNumber + " \nprenom :" + firstName + "\nom : " + name;
    }
}
