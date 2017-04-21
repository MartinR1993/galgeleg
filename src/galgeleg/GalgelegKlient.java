/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package galgeleg;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.soap.Brugeradmin;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * dette er en prøve 2
 * @author Martin
 */
public class GalgelegKlient {
    
    private static boolean aktiv = true;
    public static String bruger;
    public static GalgelegI g;
    public static Scanner scanner = new Scanner(System.in);
    public static Brugeradmin ba;
    public static Bruger user;
    public static Galgelogik logik;
    
    public static void main(String[] args) throws MalformedURLException{
        
        //skal bruges til forbindelse
        //"http://ubuntu4.javabog.dk:portNr/9943"
        
        logik = new Galgelogik();
        
        //local server
        //URL url = new URL("http://localhost:9943/galgelegtjeneste?wsdl");

        //jacobs server
        URL url = new URL("http://ubuntu4.javabog.dk:4206/galgelegtjeneste?wsdl");
        QName qname = new QName("http://galgeleg/", "GalgelegImplService");
        Service service = Service.create(url, qname);
        g = service.getPort(GalgelegI.class);

        //Opretter logik og nulstiller
        //Galgelogik spil = new Galgelogik();
        //spil.nulstil();

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
                user = ba.hentBruger(bruger, password);
                g.playerCheck(bruger);
            } catch(Exception e) {
                System.out.println("Forkert login - prøv igen");
                loginMenu();
            }
            break;
            
        }
        System.out.println("----------");
        System.out.println("Du er nu logget ind som " + user.brugernavn);
        hovedmenu();
    }
    
    public static void hovedmenu () throws MalformedURLException {
        System.out.println("----------");
        System.out.println("Du har nu følgende 3 muligheder:");
        System.out.println("1. Singleplayer");
        System.out.println("2. Multiplayer");
        System.out.println("3. Log ud");
        
        boolean startLoop = true;
        int id = scanner.nextInt();
        
        while (startLoop) {
            
            switch(id) {
                case 1:
                    // Singleplayer
                    startLoop = false;
                    singlePlayer();
                    break;
                case 2:
                    // Multiplayer
                    startLoop = false;
                    multiPlayer();
                    break;
                case 3:
                    //Log ud
                    System.out.println("Du er nu logget ud");
                    startLoop = false;
                    welcomeMenu();
                    break;
                default:
                    System.out.println("Du kan kun taste 1, 2 eller 3 - Prøv igen");
                    System.out.println("----------");
                    id = scanner.nextInt();
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
        int id = scanner.nextInt();
        boolean startLoop = true;
        
        while(startLoop){
            if (g.isContinueAvailable(bruger) == true){
                switch (id) {
                    case 1:
                        //Start gammelt spil, hvis der er et
                        System.out.println("Gammelt spil startet");
                        startLoop = false;
                        spillet();
                        break;
                    case 2:
                        //Start nyt spil
                        g.nulstil(bruger);
                        System.out.println("----------");
                        System.out.println("Nyt spil startet");
                        System.out.println("Du har nu 7 forsøg til at gætte ordet " + g.synligtOrd(bruger));
                        startLoop = false;
                        spillet();
                        break;
                    case 3:
                        //tilbage
                        startLoop = false;
                        hovedmenu();
                        break;
                    default:
                        System.out.println("Du kan kun taste 1, 2 eller 3 - Prøv igen");
                        System.out.println("----------");
                        id = scanner.nextInt();
                        break;
                }
            }
            else{
                switch (id) {
                    case 1:
                        //Start nyt spil
                        g.nulstil(bruger);
                        System.out.println("----------");
                        System.out.println("Nyt spil startet");
                        System.out.println("Du har nu 7 forsøg til at gætte ordet " + g.synligtOrd(bruger));
                        startLoop = false;
                        spillet();
                        break;
                    case 2:
                        //tilbage
                        startLoop = false;
                        hovedmenu();
                        break;
                    default:
                        System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
                        System.out.println("----------");
                        id = scanner.nextInt();
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
        System.out.println("3. Tilbage");
        
        boolean startLoop = true;
        int id = scanner.nextInt();
        
        while (startLoop) {
            
            switch(id) {
                case 1:
                    g.newMulti(bruger);
                    System.out.println("Du har nu oprettet et spil");
                    startLoop = false;
                    lobbyMenu(0);
                    
                    // testkode til at starte og køre et spil
                    System.out.println("Tast 1 for at starte spillet");
                    if (scanner.nextInt() == 1) {
                        g.startGame(bruger);
                    }
                    
                    for (int i = 0; i < 10; i++) {
                        System.out.println(bruger);
                        System.out.println(g.gætBogstavMultiOgLog(scanner.nextLine(),bruger));
                        
                    }
                    // slut
                    
                    hovedmenu();
                    break;
                case 2:
                    System.out.println("------");
                    System.out.println("Her er en liste over alle lobbys: ");
                    for (int i = 1; i < g.getMultiListNames().size()+1; i++) {
                        System.out.println(i + ". " + g.getMultiListNames().get(i-1));
                    }
                    startLoop = false;
                    spilListe();
                    break;
                case 3:
                    //tilbage
                    startLoop = false;
                    hovedmenu();
                    break;
                default:
                    System.out.println("Du kan kun taste 1, 2 eller 3 - Prøv igen");
                    System.out.println("----------");
                    id = scanner.nextInt();
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
        int id = scanner.nextInt();
        
        while (startLoop) {
            
            if (id >= 1 && id <= g.getMultiListNames().size()){
                System.out.println("------");
                System.out.println("Du har nu joinet " + g.getMultiListNames().get(id-1));
                ArrayList<String> navne =g.joinMulti(g.getMultiListNames().get(id-1), bruger);
                
                
                //skal slettes
                System.out.println("Deltagere: ");
                    for (int j = 0; j < navne.size(); j++) {
                    System.out.println(navne.get(j));
                }
                    lobbyMenu(1);

                //testkode til kør af multispil
                while (g.isGameStarted(bruger) == false) {
                }

                for (int i = 0; i < 10; i++) {
                    System.out.println(bruger);
                    System.out.println(g.gætBogstavMultiOgLog(scanner.nextLine(),bruger));
                }
                //slut

                startLoop = false;
            }
            
            else if (id == (g.getMultiListNames().size()+1)){
                startLoop = false;
                multiPlayer();
            }
            else {
                System.out.println("Du kan kun taste et tal mellem 1 og " + (g.getMultiListNames().size()+1) + " - Prøv igen");
                System.out.println("----------");
                id = scanner.nextInt();
            }
        }
        multiPlayer();
    }
    
    public static void lobbyMenu(int i){
        if(i == 0){
            System.out.println("------");
            System.out.println("Du er host og har nu følgende 2 muligheder:");
            System.out.println("1. Start spil");
            System.out.println("2. Slet lobby");
        }
        else if(i == 0){
            System.out.println("------");
            System.out.println("Du er gæst og har nu følgende mulighed:");
            System.out.println("1. Forlad lobby");
        }
         boolean startLoop = true;
        int id = scanner.nextInt();
        
        while (startLoop) {
            if(i == 0){
                switch(id) {
                    case 1:
                //testkode til kør af multispil
                while (g.isGameStarted(bruger) == false) {
                }

                for (int j = 0; j < 10; j++) {
                    System.out.println(bruger);
                    System.out.println(g.gætBogstavMultiOgLog(scanner.nextLine(),bruger));
                }
                //slut
                        break;
                    case 2:
                        System.out.println("Du har nu slettet din lobby");
                        startLoop = false;
                        g.leaveLobby(bruger);
                        break;
                    default:
                        System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
                        System.out.println("----------");
                        id = scanner.nextInt();
                }
            }
            else if (id == 1){
                switch(id) {
                    case 1:
                        System.out.println("Du har nu forladt lobbyen");
                        startLoop = false;
                        g.leaveLobby(bruger);
                        break;
                    default:
                        System.out.println("Du kan kun taste 1 eller 2 - Prøv igen");
                        System.out.println("----------");
                        id = scanner.nextInt();
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
}
