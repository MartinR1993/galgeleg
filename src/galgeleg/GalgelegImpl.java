package galgeleg;

import brugerautorisation.data.Bruger;
import javax.jws.WebService;
import brugerautorisation.transport.soap.Brugeradmin;
import utils.Connector;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

@WebService(endpointInterface = "galgeleg.GalgelegI")
public class GalgelegImpl implements GalgelegI  {

	private Galgelogik logik;
	private Brugeradmin BI;
	public String brugernavn;
	ArrayList<String> nameList, availableGames;
	ArrayList<Galgelogik> gameList;
	ArrayList<ArrayList> deltagerListe, deltagerSpil;
	ArrayList<Boolean> isGameRunning;
	URL url1;
	Brugeradmin ba;


	public GalgelegImpl() throws MalformedURLException {

		url1 = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
		QName qname1 = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
		Service service = Service.create(url1, qname1);
		ba = service.getPort(Brugeradmin.class);

		// singleplayer
		nameList = new ArrayList();
		gameList = new ArrayList();

		// multiplayer
		// Navne på alle spil som ikke er startet endnu og som ligger i lobby
		availableGames = new ArrayList();
		// liste med lister over alle lobbyers deltagere.
		deltagerListe = new ArrayList();
		// liste med lister over alle lobbyers spil
		deltagerSpil = new ArrayList();
		// liste som holder øje med om de forskellige spil er startet
		isGameRunning = new ArrayList(); 



	}


	@Override
	public String newMulti(String brugernavn){

		String host = brugernavn;

		ArrayList<String> deltagere = new ArrayList();
		ArrayList<Galgelogik> deltagereSpil = new ArrayList();

		deltagere.add(host);


		try {
			logik.hentOrdFraDr();
			System.out.println("Hentede succesfuldt ord fra dr.dk's hjemmeside");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Mislykkedes med at hente ord fra dr.dk - anvender standard udvalg");
		}


		availableGames.add(host + "'s spil");
		deltagerListe.add(deltagere);
		deltagerSpil.add(deltagereSpil);
		isGameRunning.add(false);

		return brugernavn;
	}

	@Override
	public Boolean isGameStarted(String brugerID){

		for (int i = 0; i < deltagerListe.size(); i++) {
			if (deltagerListe.get(i).contains(brugerID)) {
				return isGameRunning.get(i);
			}
		}

		return false;


	}

	@Override
	public Boolean isContinueAvailable(String bruger){

		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(bruger)) {
				if (gameList.get(i).getBrugteBogstaver().isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public ArrayList<String> getMultiListNames(){
		return availableGames;
	}

	@Override
	public ArrayList joinMulti(String lobbyName, String brugerID){

		ArrayList<String> errorJoin = new ArrayList();
		errorJoin.add("Something went wrong");


		try {
			for (int i = 0; i < availableGames.size(); i++) {
				if(availableGames.get(i).equals(lobbyName)){
					deltagerListe.get(i).add(brugerID);

					return deltagerListe.get(i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return errorJoin;

		}
		return errorJoin;
	}

	@Override
	public String leaveLobby(String brugerID){

		for (int i = 0; i < deltagerListe.size(); i++) {

			if (deltagerListe.get(i).get(0).equals(brugerID)) {

				deltagerListe.remove(i);
				deltagerSpil.remove(i);
				isGameRunning.remove(i);   
			}

			else if (deltagerListe.get(i).contains(brugerID)) {
				deltagerListe.get(i).remove(brugerID);
			}
			if (availableGames.get(i).equals(brugerID + "'s spil")) {
				availableGames.remove(i);
			}
		}
		return brugerID+" har startet et spil";
	}


	@Override
	public String startGame(String brugerID){

		Galgelogik logik = new Galgelogik();
		for (int i = 0; i < availableGames.size(); i++) {
			if (availableGames.get(i).contains(brugerID)){              
				availableGames.remove(i);
			}
		}

		for (int j = 0; j < deltagerListe.size(); j++) {

			if (deltagerListe.get(j).contains(brugerID)) {
				for (int j2 = 0; j2 < deltagerListe.get(j).size(); j2++) {
					deltagerSpil.get(j).add(new Galgelogik());	
					isGameRunning.set(j, true);
				}
			}
		}	
		return brugerID+"har startet et spil";
	}



	public void newGame(){
		logik = new Galgelogik();

		try {
			logik.hentOrdFraDr();
			System.out.println("Hentede succesfuldt ord fra dr.dk's hjemmeside");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Mislykkedes med at hente ord fra dr.dk - anvender standard udvalg");
		}

		gameList.add(logik);
	}

	@Override
	public String synligtOrd(String brugerID) {

		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(brugerID)) {

				return  gameList.get(i).getSynligtOrd();
			}
		}
		return "Fejl i synligtOrd";
	}

	@Override
	public String gætBogstav(String ord, String brugernavn) {
		this.brugernavn = brugernavn;

		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(brugernavn)) {
				gameList.get(i).gætBogstav(ord);
			}
		}
		return synligtOrd(brugernavn);
	}


	@Override
	public String gætBogstavMultiOgLog(String ord, String brugernavn) {	

		for (int i = 0; i < deltagerListe.size(); i++) {
			for (int j = 0; j < deltagerListe.get(i).size(); j++) {

				if (deltagerListe.get(i).get(j).equals(brugernavn)) {
					Galgelogik spillet = (Galgelogik)deltagerSpil.get(i).get(j);
					spillet.gætBogstav(ord);

					String str = "";

					str += "---------- \n";
					str += "Synligt Ord = " + spillet.getSynligtOrd() + "\n";
					str += "Antal forkerte bogstaver = " + spillet.getAntalForkerteBogstaver() + "/7\n";
					str += "Brugte Bogstaver = " + spillet.getBrugteBogstaver() + "\n";
					if (spillet.erSpilletTabt())
						str += "SPILLET ER TABT! - Ordet var " + spillet.getOrdet() + "\n";
					if (spillet.erSpilletVundet())
						str += "SPILLET ER VUNDET!\n";
					str += "---------- ";

					return str;
				}
			}
		}
		return "noget gik galt med gætBogstavMultiOgLog";
	}

	@Override
	public boolean isMyMultiActive(String brugerID){

		for (int i = 0; i < deltagerListe.size(); i++) {
			for (int j = 0; j < deltagerListe.get(i).size(); j++) {

				if (deltagerListe.get(i).get(j).equals(brugerID)) {
					Galgelogik spillet = (Galgelogik)deltagerSpil.get(i).get(j);
					if (spillet.erSpilletSlut()) {
						return false;
					}else {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String isMyMultiOver(String brugerID){
		int count = 0;
		
		int vinderFejl = 7;
		String vinder = "";
		ArrayList<String> vindere = new ArrayList<>();


		for (int i = 0; i < deltagerListe.size(); i++) {

			if (deltagerListe.get(i).contains(brugerID)) {
				for (int j = 0; j < deltagerListe.get(i).size(); j++) {



					Galgelogik spillet = (Galgelogik)deltagerSpil.get(i).get(j);
					if (spillet.erSpilletSlut()) {
						count++;

						if (count == deltagerListe.get(i).size()) {

							for (int k = 0; k < deltagerListe.get(i).size(); k++) {
								spillet =  (Galgelogik)deltagerSpil.get(i).get(k);

								if(vinderFejl > spillet.getAntalForkerteBogstaver()){
									vinderFejl = spillet.getAntalForkerteBogstaver();
								}
							}

							for (int k = 0; k < deltagerListe.get(i).size(); k++) {
								spillet =  (Galgelogik)deltagerSpil.get(i).get(k);

								if(vinderFejl == spillet.getAntalForkerteBogstaver()){
									if (!vindere.contains(deltagerListe.get(i).get(k))) {
										vindere.add((String)deltagerListe.get(i).get(k));
									}
									vinder += deltagerListe.get(i).get(k) + " ";
								}
							}

							if (deltagerListe.get(i).get(0).equals(brugerID)) {
								Connector connector = new Connector();
								for (int k = 0; k < vindere.size(); k++) {
									try {
										connector.doUpdate("INSERT INTO highscores (studentID, score) VALUES ('"+vindere.get(k)+"', 1) ON DUPLICATE KEY UPDATE score=score+1;");
									} catch (SQLException e) {
									}      
								}   
							}
							return "Spillet er slut og vinderen er " + vinder;
						}
					}
				}
			}
		}

		for (int i = 0; i < deltagerListe.size(); i++) {
			if (!deltagerListe.get(i).contains(brugerID)) {
				return "Spillet er slut";
			}
		}

		if (deltagerListe.isEmpty()) {
			return "Spillet er slut";
		}

		return "venter på andre deltagere " + count;
	}


	@Override
	public String multiLog(String brugerID) {

		for (int i = 0; i < deltagerListe.size(); i++) {
			for (int j = 0; j < deltagerListe.get(i).size(); j++) {

				if (deltagerListe.get(i).get(j).equals(brugerID)) {

					Galgelogik spillet = (Galgelogik)deltagerSpil.get(i).get(j);

					String str = "";

					str += "---------- \n";
					str += "Synligt Ord = " + spillet.getSynligtOrd() + "\n";
					str += "Antal forkerte bogstaver = " + spillet.getAntalForkerteBogstaver() + "/7\n";
					str += "Brugte Bogstaver = " + spillet.getBrugteBogstaver() + "\n";
					str += "---------- ";

					return str;
				}
			}
		}
		return "noget gik galt med multiLog";
	}



	@Override
	public String log(String brugerID) {
		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(brugernavn)) {

				logik = gameList.get(i);
				String str = "";

				str += "---------- \n";
				str += "Synligt Ord = " + logik.getSynligtOrd() + "\n";
				str += "Antal forkerte bogstaver = " + logik.getAntalForkerteBogstaver() + "/7\n";
				str += "Brugte Bogstaver = " + logik.getBrugteBogstaver() + "\n";
				if (logik.erSpilletTabt())
					str += "SPILLET ER TABT! - Ordet var " + logik.getOrdet() + "\n";
				if (logik.erSpilletVundet())
					str += "SPILLET ER VUNDET!\n";
				str += "---------- ";

				return str;
			}
		}

		return "noget gik galt i log";
	}

	@Override
	public String logWeb(String brugerID) {

		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(brugernavn)) {

				logik = gameList.get(i);

				String str = "";

				str += "Synligt Ord = " + logik.getSynligtOrd() + "<br>";
				str += "Antal forkerte bogstaver = " + logik.getAntalForkerteBogstaver() + "/7 <br>";
				str += "Brugte Bogstaver = " + logik.getBrugteBogstaver() + "<br>";
				if (logik.erSpilletTabt())
					str += "SPILLET ER TABT! - Ordet var " + logik.getOrdet() + "<br>";
				if (logik.erSpilletVundet())
					str += "SPILLET ER VUNDET! <br>";

				return str;
			}
		}
		return "noget gik galt i logWeb";
	}

	@Override
	public boolean spilSlut() {
		return logik.erSpilletSlut();
	}

	@Override
	public String nulstil(String brugerID) {

		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(brugerID)) {
				gameList.get(i).nulstil();
			}
		}
		return brugerID;
	}

	@Override
	public String ordet(String brugerID) {


		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(brugerID)) {
				return gameList.get(i).getOrdet();
			}

		}
		return "noget gik galt i ordet()";
	}

	@Override
	public boolean hentBruger(String brugernavn, String password) {
		//
		//        try {
		//            Bruger b = BI.hentBruger(brugernavn, password);
		//            System.out.println("GalgelegImpl.java : Objekt modtaget");
		//            if(!nameList.contains(brugernavn)){
		//                nameList.add(brugernavn);
		//                newGame();
		//            }
		//            return true;
		//        } catch (IllegalArgumentException e) {
		//            System.out.println("GalgelegImpl.java : IllegalArgumentException");
		//            e.printStackTrace();
		//            return false;
		//        } catch (Exception p) {
		//            System.out.println("GalgelegImpl.java : Exception");
		//            p.printStackTrace();
		//        }
		//
		return false;
	}

	@Override
	public String playerCheck(String brugernavn){
		if(!nameList.contains(brugernavn)){
			nameList.add(brugernavn);
			newGame();
		}
		return brugernavn;
	}

	@Override
	public String clearLobby(String brugerID){

		for (int i = 0; i < deltagerListe.size(); i++) {

			if (deltagerListe.get(i).get(0).equals(brugerID)) {
				isGameRunning.remove(i);
				deltagerSpil.remove(i);
				deltagerListe.remove(i);

				if (availableGames.contains(brugerID + "'s spil")) {
					availableGames.remove(brugerID + "'s spil");
				}

				return "";
			}


		}
		return "";
	}

	@Override
	public Bruger login(String brugerID, String adgangskode) {
		return ba.hentBruger(brugerID, adgangskode);
	}

	@Override
	public ArrayList<scoreDTO> getScores() {
		Connector connector = new Connector();
		ArrayList<scoreDTO> list = new ArrayList<scoreDTO>();
		ResultSet rs = null;
		try {
			rs = connector.doQuery("SELECT * FROM galgescores.highscores ORDER BY score DESC LIMIT 10");
		} catch (SQLException e) {

		}
		try {
			while (rs.next()) {
				list.add(new scoreDTO(rs.getString("studentID"), rs.getInt("score")));
			}
		} catch (SQLException e) {

		}

		return list;
	}


}
