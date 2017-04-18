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
//        URL url = new URL("http://localhost:9943/galgelegtjeneste?wsdl");

//        jacobs server
URL url = new URL("http://ubuntu4.javabog.dk:3043/galgelegtjeneste?wsdl");
QName qname = new QName("http://galgeleg/", "GalgelegImplService");
Service service = Service.create(url, qname);
g = service.getPort(GalgelegI.class);

//Opretter logik og nulstiller
Galgelogik spil = new Galgelogik();
spil.nulstil();

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
                    // Start nyt spil
                    g.nulstil(bruger);
                    System.out.println("Nyt spil startet");
                    System.out.println("Du har nu 7 forsøg til at gætte ordet");
                    startLoop = false;
                    spillet();
                    break;
                case 2:
                    // Multiplayer
                    System.out.println("Ikke implementeret endnu");
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
    
    public static void singlePlayer(){
        
    }
    
    public static void multiPlayer(){
        
    }
    
    public static void spillet() {
        //Spillet
        System.out.println(g.log(bruger));
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
                System.out.println("\nSpillet er slut! \nDu har nu to muligheder: \n1. Nyt spil \n2. Afslut");
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
                            // Afslut spillet
                            System.out.println("Spil afsluttet");
                            aktiv = false;
                            startLoop = false;
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
