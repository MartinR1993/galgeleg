/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.soap.Brugeradmin;
import utils.Connector;

/**
 * dette er en prøve 2
 * @author Martin
 */
public class GalgelegKlient {

	private static boolean aktiv = true;
	public static String bruger, firstGuess;
	public static GalgelegI g;
	public static Scanner scanner = new Scanner(System.in);
	public static Brugeradmin ba;
	public static Bruger user;
	public static Galgelogik logik;
	public static Scanner scan = new Scanner(System.in);
	
	
	// HER FRANK------------------------- erstat bruger med hvemend der vandt
//	Connector connector = new Connector();
//    public void updateScore() {
//        try {
//            connector.doUpdate("INSERT INTO highscores (studentID, score) VALUES ('"+bruger+"', 1) ON DUPLICATE KEY UPDATE score=score+1;");
//        } catch (SQLException ex) {}
//    }
//	
	
	
	
	public static void main(String[] args) throws MalformedURLException{
	
		logik = new Galgelogik();
		 

		//URL url = new URL("http://localhost:3043/galgelegtjeneste?wsdl");

		URL url = new URL("http://ubuntu4.javabog.dk:3043/galgelegtjeneste?wsdl");
		QName qname = new QName("http://galgeleg/", "GalgelegImplService");
		Service service = Service.create(url, qname);
		g = service.getPort(GalgelegI.class);


		welcomeMenu();
	}
	public static void welcomeMenu () throws MalformedURLException {
		//Intro tekst
		System.out.println("Velkommen til Galgeleg");
		System.out.println("Du skal logge ind for at kunne spille spillet");
		System.out.println("----------");
		loginMenu();
	}

	public static void loginMenu () throws MalformedURLException {

		URL url1 = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
		QName qname1 = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
		Service service = Service.create(url1, qname1);
		ba = service.getPort(Brugeradmin.class);

		//Login
		Scanner loginscanner = new Scanner(System.in);
		while (true) {
			System.out.println("Indtast brugernavn: ");
			bruger = loginscanner.nextLine();

			System.out.println("Indtast password: ");
			String password = loginscanner.nextLine();
			try {
				user=g.login(bruger, password);
				if( user==null) throw new SecurityException();
				g.playerCheck(bruger);
			} catch(Exception e) {
				System.out.println("Forkert login - prøv igen");
				loginMenu();
			}
			break;

		}
		System.out.println("----------");
		System.out.println("Du er nu logget ind som " + user.brugernavn);
		g.clearLobby(bruger);
		hovedmenu();
	}

	public static void hovedmenu () throws MalformedURLException {
		System.out.println("----------");
		System.out.println("Du har nu følgende 3 muligheder:");
		System.out.println("1. Singleplayer");
		System.out.println("2. Multiplayer");
		System.out.println("3. Log ud");

		boolean startLoop = true;
		String id = scanner.nextLine();

		while (startLoop) {

			switch(id) {
			case "1":
				// Singleplayer
				startLoop = false;
				singlePlayer();
				break;
			case "2":
				// Multiplayer
				startLoop = false;
				multiPlayer();
				break;
			case "3":
				//Log ud
				System.out.println("Du er nu logget ud");
				startLoop = false;
				welcomeMenu();
				break;
			default:
				System.out.println("Du kan kun taste 1, 2 eller 3 - Prøv igen");
				System.out.println("----------");
				id = scanner.nextLine();
			}
		}
	}

	public static void singlePlayer() throws MalformedURLException{
		System.out.println("------");
		if (g.isContinueAvailable(bruger) == true){
			System.out.println("Du har nu følgende 3 muligheder:");
			System.out.println("1. Fortsæt gammelt spil");
			System.out.println("2. Start nyt spil");
			System.out.println("3. Tilbage");
		}
		else{
			System.out.println("Da du ikke har et spil, har du nu følgende 2 muligheder:");
			System.out.println("1. Start nyt spil");
			System.out.println("2. Tilbage");
		}
		String id = scanner.nextLine();
		boolean startLoop = true;

		while(startLoop){
			if (g.isContinueAvailable(bruger) == true){
				switch (id) {
				case "1":
					//Start gammelt spil, hvis der er et
					System.out.println("Gammelt spil startet");
					startLoop = false;
					spillet();
					break;
				case "2":
					//Start nyt spil
					g.nulstil(bruger);
					System.out.println("----------");
					System.out.println("Nyt spil startet");
					System.out.println("Du har nu 7 forsøg til at gætte ordet " + g.synligtOrd(bruger));
					startLoop = false;
					spillet();
					break;
				case "3":
					//tilbage
					startLoop = false;
					hovedmenu();
					break;
				default:
					System.out.println("Du kan kun taste 1, 2 eller 3 - Prøv igen");
					System.out.println("----------");
					id = scanner.nextLine();
					break;
				}
			}
			else{
				switch (id) {
				case "1":
					//Start nyt spil
					g.nulstil(bruger);
					System.out.println("----------");
					System.out.println("Nyt spil startet");
					System.out.println("Du har nu 7 forsøg til at gætte ordet " + g.synligtOrd(bruger));
					startLoop = false;
					spillet();
					break;
				case "2":
					//tilbage
					startLoop = false;
					hovedmenu();
					break;
				default:
					System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
					System.out.println("----------");
					id = scanner.nextLine();
					break;
				}
			}
		}
	}

	public static void multiPlayer() throws MalformedURLException{
		System.out.println("------");
		System.out.println("Du har nu følgende 3 muligheder:");
		System.out.println("1. Opret spil");
		System.out.println("2. Se lobbys");
		System.out.println("3. Se highscores");
		System.out.println("4. Tilbage");

		boolean startLoop = true;
		String id = scanner.nextLine();

		while (startLoop) {

			switch(id) {
			case "1":
				g.newMulti(bruger);
				System.out.println("Du har nu oprettet et spil");
				startLoop = false;
				lobbyMenu(0);
				break;
			case "2":
				System.out.println("------");
				System.out.println("Her er en liste over alle lobbys: ");
				for (int i = 1; i < g.getMultiListNames().size()+1; i++) {
					System.out.println(i + ". " + g.getMultiListNames().get(i-1));
				}
				startLoop = false;
				spilListe();
				break;
			case "3":
				highscore();
				startLoop = false;
				break;
			case "4":
				//tilbage
				startLoop = false;
				hovedmenu();
				break;
			default:
				System.out.println("Du kan kun taste 1, 2 eller 3 - Prøv igen");
				System.out.println("----------");
				id = scanner.nextLine();
			}
		}
	}
	
	private static void highscore() throws MalformedURLException {
		System.out.println("------");
		System.out.println("Du har nu 1 mulighed:");
		System.out.println("1. Tilbage");

		boolean startLoop = true;

		while (startLoop) {

			String id = scanner.nextLine();
			
			if (id.equals("1")) {
				multiPlayer();
			}else{
				System.out.println("Du kan kun trykke 1");
			}	
		}
	}

	public static void spilListe() throws MalformedURLException {
		System.out.println("------");
		if(g.getMultiListNames().size() > 0){
			System.out.println("Du har nu følgende " + (g.getMultiListNames().size()+1) + " muligheder: ");
		}
		else{
			System.out.println("Du har nu følgende mulighed: ");
		}
		switch (g.getMultiListNames().size()) {
		case 1:
			System.out.println("1. Vælg det tilgængelige spil");
			break;
		case 0:
			System.out.println("Ingen oprettede lobbys");
			break;
		default:
			System.out.println("1. - " + g.getMultiListNames().size() +". Vælg et spil");
			break;
		}
		System.out.println((g.getMultiListNames().size()+1) + ". Tilbage");

		boolean startLoop = true;

		while (startLoop) {
			String id = scanner.nextLine();


			if( id.matches(".*\\d+.*"))
				if (Integer.parseInt(id) >= 1 && Integer.parseInt(id) <= g.getMultiListNames().size()){
					System.out.println("------");
					System.out.println("Du har nu joinet " + g.getMultiListNames().get(Integer.parseInt(id)-1));
					ArrayList<String> navne =g.joinMulti(g.getMultiListNames().get(Integer.parseInt(id)-1), bruger);


					//skal slettes
					System.out.println("Deltagere: ");
					for (int j = 0; j < navne.size(); j++) {
						System.out.println(navne.get(j));
					}
					lobbyMenu(1);
					startLoop = false;
				}

				else if (Integer.parseInt(id) == (g.getMultiListNames().size()+1)){
					startLoop = false;
					multiPlayer();
				}
				else {
					System.out.println("Du kan kun taste et tal mellem 1 og " + (g.getMultiListNames().size()+1) + " - Prøv igen");
					System.out.println("----------");
					id = scanner.nextLine();
				}
		}
	}

	public static void lobbyMenu(int i) throws MalformedURLException{
		if(i == 0){
			System.out.println("------");
			System.out.println("Du er host og har nu følgende 2 muligheder:");
			System.out.println("1. Start spil");
			System.out.println("2. Slet lobby");
		}
		else if(i == 1){
			System.out.println("------");
			System.out.println("Du er gæst og har nu følgende mulighed:");
			System.out.println("1. Forlad lobby");
		}


		Thread gameCheckThread = new Thread(){
			@Override
			public void run(){

				System.out.println("Venter på host...");
				while (g.isGameStarted(bruger) == false) {
				}

				System.out.println("Spillet er startet!");

				multiplayerGame();
			}
		};

		if (i == 1) {
			gameCheckThread.start();
		}

		boolean startLoop = true;


		int id =99;

		try {
			firstGuess = scan.nextLine();
			try {
				id = Integer.parseInt(firstGuess);	
			} catch (Exception e) {
			}
			System.out.println(g.gætBogstavMultiOgLog(firstGuess, bruger));	

		} catch (Exception e) {
		}

		if(!g.isGameStarted(bruger))
			while (startLoop) {
				if(i == 0){
					switch(id) {
					case 1:
						g.startGame(bruger);
						g.multiLog(bruger);
						multiplayerGame();
						startLoop = false;
						break;
					case 2:
						System.out.println("Du har nu slettet din lobby");
						g.clearLobby(bruger);
						hovedmenu();
						startLoop = false;
						break;
					default:
						System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
						System.out.println("----------");
						id = scan.nextInt();
					}
				}
				else if (id == 1){
					switch(id) {
					case 1:
						System.out.println("Du har nu forladt lobbyen");
						startLoop = false;
						g.leaveLobby(bruger);
						multiPlayer();
						break;
					default:
						System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
						System.out.println("----------");
						id = scan.nextInt();
					}
				}
			}
	}

	public static void spillet() {
		//Spillet
		while (aktiv) try {

			Scanner spilscanner = new Scanner(System.in);
			System.out.println("Indtast et bogstav!");
			String input = spilscanner.nextLine();
			//Input validering længere en 1... virker ike med æ,ø,å...
			if(input.length() == 1){

				String c = input.toLowerCase();

				//Hvis bogstavet er brugt, bliver man informeret. Virker ikke endnu..
				if(logik.getBrugteBogstaver().contains(c)){
					System.out.println("----------");
					System.out.println("Bogstavet er allerede brugt");
				}
				else{
					g.gætBogstav(c, bruger);
					System.out.println(g.log(bruger));
				}
			}
			else if (input.length() != 1){
				System.out.println("----------");
				System.out.println("Du kan kun skrive ét bogstav");
			}

			if (g.spilSlut()) {
				System.out.println("Spillet er slut! \nDu har nu to muligheder: \n1. Nyt spil \n2. Tilbage");
				int id = spilscanner.nextInt();

				boolean startLoop = true;

				while (startLoop) {

					switch(id) {
					case 1:
						// Start nyt spil
						g.nulstil(bruger);
						System.out.println("Nyt spil startet");
						System.out.println(g.log(bruger) + "\n");
						startLoop = false;
						break;
					case 2:
						// Tilbage
						startLoop = false;
						hovedmenu();
						break;
					default:
						System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
						System.out.println("----------");
						id = spilscanner.nextInt();
					}
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static void multiplayerGame() {

		System.out.println(g.multiLog(bruger));

		Scanner multiScan = new Scanner(System.in);


		while(g.isMyMultiActive(bruger)){  
			System.out.println(g.gætBogstavMultiOgLog(multiScan.nextLine(),bruger));
		}


		Thread waitMultiThread = new Thread(){
			@Override
			public void run(){

				while(true){
					System.out.println(g.isMyMultiOver(bruger));
					if (g.isMyMultiOver(bruger).contains("slut")) {
						try {
							Thread.sleep(10000);

							g.clearLobby(bruger);
							hovedmenu();
						} catch (MalformedURLException | InterruptedException e) {
						}
						stop();
					}

					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {

					}

				}
			}
		};

		waitMultiThread.start();   
	}
	

	
}


