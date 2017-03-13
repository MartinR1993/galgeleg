package galgeleg;

import brugerautorisation.data.Bruger;
import java.rmi.Naming;
import javax.jws.WebService;
import java.rmi.server.UnicastRemoteObject;
import brugerautorisation.transport.rmi.Brugeradmin;

@WebService(endpointInterface = "galgeleg.GalgelegI")
public class GalgelegImpl implements GalgelegI {
    
    private Galgelogik logik;
    private Brugeradmin BI;
    
    public GalgelegImpl() {
            
        logik = new Galgelogik();
        
        try {
            logik.hentOrdFraDRUdsendelser();
            System.out.println("Hentede succesfuldt ord fra dr.dk's hjemmeside");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Mislykkedes med at hente ord fra dr.dk - anvender standard udvalg");
        }
        
        try {
            BI = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }  
    
    @Override
    public String synligtOrd() {
        return logik.getSynligtOrd();
    }
    
    @Override
    public void gætBogstav(String ord) {
        logik.gætBogstav(ord);
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
