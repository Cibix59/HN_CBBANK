package tests;
import components.Account;
//1.1.2 Creation of main class for tests
import components.Client;
import components.CurrentAccount;
import components.SavingsAccount;

import java.util.Arrays; 
//1.1.2 Creation of main class for tests

public class Main {
    public static void main(String[] args) {
        Client[] tableauClients;
        tableauClients = generateClients(3);
        displayClients(tableauClients);
      //1.2.3 Creation of the table account
        Account[] monTableauDeComptes = generateAccounts(tableauClients);
        displayAccounts(monTableauDeComptes);
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
     * genere un jeu d'essai d'accounts, à partir d'une liste de clients
     */
    public static Account[] generateAccounts(Client[] clients) {
        //les comptes Current et les comptes savings sont dans le meme tableau
    	// 2 comptes par clients, donc taille x2
        Account[] accounts = new Account[clients.length * 2];
        for (int i = 0; i < clients.length; i++) {
            accounts[i * 2] = new CurrentAccount("Compte courant", clients[i]);
            accounts[i * 2 + 1] = new SavingsAccount("Compte Epargne", clients[i]);
        }
        
        return accounts;
    }

    /**
     * affiche les clients en utilisant Stream
     */
    public static void displayClients(Client[] clients) {
        System.out.println("--- liste des clients ---");

        Arrays.stream(clients).forEach(client -> System.out.println(client.toString()));
    }
    /**
     * affiche les accounts en utilisant Stream
     */
    public static void displayAccounts(Account[] accounts) {
        System.out.println("\n--- Liste comptes ---");
        Arrays.stream(accounts).forEach(account -> System.out.println(account.toString()));
    }
    
    
}