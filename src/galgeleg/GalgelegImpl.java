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
    ArrayList<String> nameList;
    ArrayList<Galgelogik> gameList;
    
    public GalgelegImpl() {
        
        nameList = new ArrayList();
        gameList = new ArrayList();
        logik = new Galgelogik();
        
        try {
            logik.hentOrdFraDr();
            System.out.println("Hentede succesfuldt ord fra dr.dk's hjemmeside");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Mislykkedes med at hente ord fra dr.dk - anvender standard udvalg");
        }
        
        try {
            URL url2 = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname2 = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service2 = Service.create(url2, qname2);
            BI = (Brugeradmin) service2.getPort(Brugeradmin.class);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        
        try {
            URL url2 = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname2 = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service2 = Service.create(url2, qname2);
            BI = (Brugeradmin) service2.getPort(Brugeradmin.class);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        gameList.add(logik);
    }
    
    @Override
    public String synligtOrd() {
        return logik.getSynligtOrd();
    }
    
    @Override
    public void gætBogstav(String ord, String brugernavn) {
    //        logik.gætBogstav(ord);

    this.brugernavn = brugernavn;

    System.out.println(brugernavn);
    System.out.println(nameList.get(0));

    for (int i = 0; i < nameList.size(); i++) {
        if (nameList.get(i).equals(brugernavn)) {
        
        gameList.get(i).gætBogstav(ord);
        logik = gameList.get(i);
        
    }
}
    }
    
    @Override
    public String log() {
        
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
    
    @Override
    public String logWeb() {
        
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
    
    @Override
    public boolean spilSlut() {
        
        return logik.erSpilletSlut();
    }
    
    @Override
    public void nulstil() {
        logik.nulstil();
    }
    
    @Override
    public String ordet() {
        return logik.getOrdet();
    }
    
    @Override
    public boolean hentBruger(String brugernavn, String password) {
        
        try {
            Bruger b = BI.hentBruger(brugernavn, password);
            System.out.println("GalgelegImpl.java : Objekt modtaget");
            if(!nameList.contains(brugernavn)){
                nameList.add(brugernavn);
                newGame();   
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("GalgelegImpl.java : IllegalArgumentException");
            e.printStackTrace();
            return false;
        } catch (Exception p) {
            System.out.println("GalgelegImpl.java : Exception");
            p.printStackTrace();
        }
        
        return false;
    }
    
}
