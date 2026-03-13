package tests;
import components.Account;
//1.1.2 Creation of main class for tests
import components.Client;
import components.Credit;
import components.CurrentAccount;
import components.Debit;
import components.Flow;
import components.SavingsAccount;
import components.Transfert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Main {
	public static void main(String[] args) {
		Client[] tableauClients;
		tableauClients = generateClients(3);
		displayClients(tableauClients);
		//1.2.3 Creation of the table account
		//Account[] tableauDeComptes = generateAccounts(tableauClients);
		Account[] tableauDeComptes = loadAccountsFromXml("src/accounts.xml");

		displayAccounts(tableauDeComptes);

		//1.3.1 Adaptation of the table of accounts
		Hashtable<Integer, Account> hashtableAccounts = generateHashtable(tableauDeComptes);
		displayHashtable(hashtableAccounts);
		//1.3.4 Creation of the flow array
		//Flow[] tableauDeFlux = generateFlows(tableauDeComptes);
		//2.1 JSON file of flows
		Flow[] tableauDeFlux = loadFlowsFromJson("src/flows.json");
		applyFlows(tableauDeFlux, hashtableAccounts);
		// Affichage final de la hashtable après application des Flows
		displayHashtable(hashtableAccounts);
	}

	//1.3.5 Updating accounts
	public static void applyFlows(Flow[] flows, Hashtable<Integer, Account> hashtableAccounts) {

		for (Flow flow : flows) {
			if (flow instanceof Transfert) {
				// Transfert : maj des 2 comptes (source et cible)
				Transfert transfert = (Transfert) flow;

				Account target = hashtableAccounts.get(transfert.getTargetAccountNumber());
				Account source = hashtableAccounts.get(transfert.getSourceAccountNumber());

				if (target != null) target.setBalance(transfert);
				if (source != null) source.setBalance(transfert);

			} else {
				// Credit ou Debit : maj du compte cible uniquement
				Account account = hashtableAccounts.get(flow.getTargetAccountNumber());
				if (account != null) account.setBalance(flow);
			}
		}

		// Vérification des soldes négatifs avec Optional, Predicate et Stream
		Predicate<Account> isNegative = account -> account.getBalance() < 0;

		hashtableAccounts.values().stream()
		.filter(isNegative)
		.forEach(account -> Optional.of(account)
				.ifPresent(a -> System.out.println("Solde négatif détecté : " + a.toString()))
				);


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


	private static Hashtable<Integer, Account> generateHashtable(Account[] tableauDeComptes) {
		Hashtable<Integer, Account> hashtable = new Hashtable<>();
		for (Account account : tableauDeComptes) {
			hashtable.put(account.getAccountNumber(), account);
		}
		return hashtable;
	}

	public static Flow[] generateFlows(Account[] accounts) {

		//add 2 days to the current date
		LocalDate localDate = LocalDate.now().plusDays(2);
		Date effectDate= Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		int nbCurrentAccounts = 0;
		int nbSavingsAccounts = 0;
		for (Account account : accounts) {
			if (account instanceof CurrentAccount) nbCurrentAccounts++;
			else if (account instanceof SavingsAccount) nbSavingsAccounts++;
		}

		// 1 débit + crédits courants + crédits épargne + 1 transfert
		Flow[] flows = new Flow[1 + nbCurrentAccounts + nbSavingsAccounts + 1];
		int index = 0;

		// debit 50€ compte 1
		Debit debit = new Debit("Débit compte n°1", 50.0, 1, false);
		debit.setDate(effectDate);
		flows[index++] = debit;

		// credit 100.50€ sur tous les comptes courants
		for (Account account : accounts) {
			if (account instanceof CurrentAccount) {
				Credit credit = new Credit("Crédit compte courant", 100.50, account.getAccountNumber(), false);
				credit.setDate(effectDate);
				flows[index++] = credit;
			}
		}

		// credit 1500€ sur tous les comptes épargne
		for (Account account : accounts) {
			if (account instanceof SavingsAccount) {
				Credit credit = new Credit("Crédit compte épargne", 1500.0, account.getAccountNumber(), false);
				credit.setDate(effectDate);
				flows[index++] = credit;
			}
		}

		// Transfert 50€ compte n°1 -> compte n°2
		Transfert transfert = new Transfert("Transfert n°1 vers n°2", 50.0, 2, false, 1);
		transfert.setDate(effectDate);
		flows[index] = transfert;

		return flows;
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
	/**
	 * affiche les hashtableAccounts en utilisant Stream
	 */
	private static void displayHashtable(Hashtable<Integer, Account> hashtableAccounts) {
		System.out.println("\n--- Liste comptes (Hashtable, solde croissant) ---");
		hashtableAccounts.entrySet().stream()
		.sorted(Map.Entry.comparingByValue(
				(a1, a2) -> Double.compare(a1.getBalance(), a2.getBalance())
				))
		.forEach(entry -> System.out.println("Clé : " + entry.getKey() + " -> " + entry.getValue().toString()));
	}


	//2.1 JSON file of flows
	public static Flow[] loadFlowsFromJson(String filePath) {

		Path path = Paths.get(filePath);
		StringBuilder jsonContent = new StringBuilder();

		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonContent.append(line.trim());
			}
		} catch (Exception e) {
			System.out.println("probleme lecture fichier : " + e.getMessage());
			return new Flow[0];
		}

		// Parsing JSON 
		String json = jsonContent.toString();

		// découpe par objets JSON
		java.util.List<Flow> flowList = new java.util.ArrayList<>();
		int start = 0;
		while ((start = json.indexOf('{', start)) != -1) {
			int end = json.indexOf('}', start);
			if (end == -1) break;
			String obj = json.substring(start, end + 1);
			start = end + 1;

			String type= extract(obj, "type");
			String comment= extract(obj, "comment");
			double amount = Double.parseDouble(extract(obj, "amount"));
			int targetAccountNumber = Integer.parseInt(extract(obj, "targetAccountNumber"));
			boolean effect= Boolean.parseBoolean(extract(obj, "effect"));
			String sourceRaw= extract(obj, "sourceAccountNumber");

			// +2 jours comme dans generateFlows
			LocalDate localDate = LocalDate.now().plusDays(2);
			Date effectDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

			Flow flow = null;
			switch (type) {
			case "Debit":
				flow = new Debit(comment, amount, targetAccountNumber, effect);
				break;
			case "Credit":
				flow = new Credit(comment, amount, targetAccountNumber, effect);
				break;
			case "Transfert":
				int sourceAccountNumber = Integer.parseInt(sourceRaw);
				flow = new Transfert(comment, amount, targetAccountNumber, effect, sourceAccountNumber);
				break;
			default:
				System.out.println("Type de flux inconnu : " + type);
			}

			if (flow != null) {
				flow.setDate(effectDate);
				flowList.add(flow);
			}
		}

		return flowList.toArray(new Flow[0]);
	}

	// extracteur du json
	private static String extract(String obj, String key) {
		String search = "\"" + key + "\"";
		int keyIndex = obj.indexOf(search);
		if (keyIndex == -1) return "null";

		int colon = obj.indexOf(':', keyIndex) + 1;

		// Saute les espaces
		while (colon < obj.length() && obj.charAt(colon) == ' ') colon++;

		// recupere les strings
		if (obj.charAt(colon) == '"') {
			int end = obj.indexOf('"', colon + 1);
			return obj.substring(colon + 1, end);
		}

		// recupere les primitives
		int end = colon;
		while (end < obj.length() && obj.charAt(end) != ',' && obj.charAt(end) != '}') end++;
		return obj.substring(colon, end).trim();
	}

	//2.2 XML file of account 
	public static Account[] loadAccountsFromXml(String filePath) {

		Path path = Paths.get(filePath);
		java.util.List<Account> accountList = new java.util.ArrayList<>();

		// try-with-resources 
		try (var inputStream = Files.newInputStream(path)) {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);

			document.getDocumentElement().normalize();

			NodeList nodeList = document.getElementsByTagName("account");

			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);

				String type   = element.getElementsByTagName("type").item(0).getTextContent();
				String label = element.getElementsByTagName("label").item(0).getTextContent();
				String clientName  = element.getElementsByTagName("clientName").item(0).getTextContent();
				String clientFirst = element.getElementsByTagName("clientFirstName").item(0).getTextContent();
				Client client = new Client(clientName, clientFirst);

				Account account = null;
				switch (type) {
				case "CurrentAccount":
					account = new CurrentAccount(label, client);
					break;
				case "SavingsAccount":
					account = new SavingsAccount(label, client);
					break;
				default:
					System.out.println("Type de compte inconnu : " + type);
				}

				if (account != null) accountList.add(account);
			}

		} catch (Exception e) {
			System.out.println("Problème lecture fichier XML : " + e.getMessage());
			return new Account[0];
		}

		return accountList.toArray(new Account[0]);
	}


}