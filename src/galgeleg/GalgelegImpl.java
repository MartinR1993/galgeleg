package galgeleg;

import brugerautorisation.data.Bruger;
import javax.jws.WebService;
import brugerautorisation.transport.soap.Brugeradmin;
import java.net.URL;
import java.rmi.Naming;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

@WebService(endpointInterface = "galgeleg.GalgelegI")
public class GalgelegImpl implements GalgelegI {
    
    private Galgelogik logik;
    private Brugeradmin BI;
    public String brugernavn;
    ArrayList<String> nameList, availableGames;
    ArrayList<Galgelogik> gameList;
    ArrayList<ArrayList> deltagerListe, deltagerSpil;
    ArrayList<Boolean> isGameRunning;
    
    
    
    public GalgelegImpl() {
        
        nameList = new ArrayList();
        gameList = new ArrayList();
        availableGames = new ArrayList();
        deltagerListe = new ArrayList();
        deltagerSpil = new ArrayList();
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
        
        deltagerListe.add(deltagere);
        deltagerSpil.add(deltagereSpil);
        isGameRunning.add(false);
        
        
        availableGames.add(host + "'s spil");
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
                
                for (int j = 0; j < deltagerListe.get(i).size(); j++) {
                
                deltagerSpil.get(i).add(new Galgelogik());
                
                
                isGameRunning.set(i, true);
                }
                
                System.out.println("test deltagere " + deltagerListe.get(i).size());
                System.out.println("test spil "+ deltagerSpil.get(i).size());
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
//
//        try {
//            URL url2 = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
//            QName qname2 = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
//            Service service2 = Service.create(url2, qname2);
//            BI = (Brugeradmin) service2.getPort(Brugeradmin.class);
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

gameList.add(logik);
    }
    
    @Override
    public String synligtOrd(String brugerID) {
        
        for (int i = 0; i < nameList.size(); i++) {
            if (nameList.get(i).equals(brugerID)) {
                
                return  gameList.get(i).getSynligtOrd();
//        logik = gameList.get(i);
            }
        }
        return "Fejl i synligtOrd";
    }
    
    @Override
    public String gætBogstav(String ord, String brugernavn) {
        //        logik.gætBogstav(ord);
        
        this.brugernavn = brugernavn;
        
        System.out.println(brugernavn);
        System.out.println(nameList.get(0));
        
        for (int i = 0; i < nameList.size(); i++) {
            if (nameList.get(i).equals(brugernavn)) {
                
                gameList.get(i).gætBogstav(ord);
//        logik = gameList.get(i);

            }
        }
        return synligtOrd(brugernavn);
    }
    
    
    @Override
    public String gætBogstavMultiOgLog(String ord, String brugernavn) {
        //        logik.gætBogstav(ord);
        
        for (int i = 0; i < deltagerListe.size(); i++) {
            for (int j = 0; j < deltagerListe.get(i).size(); j++) {
                
            if (deltagerListe.get(i).get(j).equals(brugernavn)) {
                System.out.println("Han er i spil " + i + " " + j);
                Galgelogik spillet = (Galgelogik)deltagerSpil.get(i).get(j);
                spillet.gætBogstav(ord);
                
                        String str = "";
                
                str += "---------- \n";
                //str += "Ordet (skjult) = " + logik.getOrdet() + "\n";
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
    	for (int i = 0; i < deltagerListe.size(); i++) {
    		
    		if (deltagerListe.contains(brugerID)) {
				for (int j = 0; j < deltagerListe.size(); j++) {
					
					Galgelogik spillet = (Galgelogik)deltagerSpil.get(i).get(j);
					if (spillet.erSpilletSlut()) {
						count++;
						
						if (count == deltagerListe.get(i).size()) {
							return "Spillet er slut";
						}
						
					}
				}
			}	
    	}
    	
    	return "venter på andre deltagere";
    	
    	
    	
    	
    	
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
                //str += "Ordet (skjult) = " + logik.getOrdet() + "\n";
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
//        logik = gameList.get(i);
            }
        }
        return brugerID;
    }
    
    @Override
    public String ordet(String brugerID) {
        
        
        for (int i = 0; i < nameList.size(); i++) {
            if (nameList.get(i).equals(brugerID)) {
                
                return gameList.get(i).getOrdet();
//        logik = gameList.get(i);


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
        return "fejl i clearlobby";
    }
    
    
}
