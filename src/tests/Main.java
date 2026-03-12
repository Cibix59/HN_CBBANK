package tests;
//1.1.2 Creation of main class for tests
import components.Client; 
import java.util.Arrays; 


public class Main {
    public static void main(String[] args) {
        Client[] tableauClients;
        tableauClients = generateClients(3);
        displayClients(tableauClients);
    }

    /**
     * genere un jeu d'essai de clients
     */
    public static Client[] generateClients(int numberOfClients) {
        // tableau vide
        Client[] clients = new Client[numberOfClients];
        
        // remplissage
        for (int i = 0; i < numberOfClients; i++) {
            int numero = i + 1; //besoin de décaler de 1, car le numero client commence à 1
            clients[i] = new Client("name" + numero, "firstname" + numero);
        }
        
        return clients;
    }

    /**
     * affiche les clients en utilisant Stream
     */
    public static void displayClients(Client[] clients) {
        System.out.println("--- liste des clients ---");

        Arrays.stream(clients).forEach(client -> System.out.println(client.toString()));
    }
}