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
    public static Scanner scanner;
    public static Brugeradmin ba;
    public static Bruger user;
    
    public static void main(String[] args) throws MalformedURLException{
        
        //skal bruges til forbindelse
        //"http://ubuntu4.javabog.dk:portNr/9943"
        
        Galgelogik logik = new Galgelogik();
        
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

        //Opsætter scanner
        scanner = new Scanner(System.in);
       
        velcomeMenu();

        loginMenu();

        //Spillet
        while (aktiv) try {
            
            System.out.println("Indtast et bogstav!");
            String input = scanner.nextLine();
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
            else{
                System.out.println("----------");
                System.out.println("Du kan kun skrive ét bogstav");
            }
            
            if (g.spilSlut()) {
                System.out.println("\nSpillet er slut! \nDu har nu to muligheder: \n1. Nyt spil \n2. Afslut");
                int id = scanner.nextInt();
                
                boolean startLoop = true;
                
                while (startLoop) {
                    
                    switch(id) {
                        case 1:
                            // Start nyt spil
                            g.nulstil(bruger);
                            System.out.println("Nyt spil startet");
                            System.out.println("----------");
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
                            id = scanner.nextInt();
                    }
                }
            }
            
        } catch (Throwable t) {
            t.printStackTrace();
            scanner.nextLine();
        }
    }
    public static void velcomeMenu () {
        //Intro tekst
        System.out.println("Velkommen til Galgeleg");
        System.out.println("Du skal logge ind for at kunne spille spillet");
        System.out.println("----------");
    }
    
    public static void loginMenu () throws MalformedURLException {
        
        URL url1 = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
        QName qname1 = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
        Service service = Service.create(url1, qname1);
        ba = service.getPort(Brugeradmin.class);
        //Login
        while (true) {
            System.out.println("Indtast brugernavn: ");
            bruger = scanner.nextLine();
            
            System.out.println("Indtast password: ");
            String password = scanner.nextLine();
            try {
                user = ba.hentBruger(bruger, password);
            } catch(Exception e) {
                System.out.println("Forkert login - prøv igen");
                loginMenu();
            }
            break;
            
        }
        System.out.println("----------");
        System.out.println("Du er nu logget ind som " + user.brugernavn);
        System.out.println("Du skal gætte et ord og må max have 7 forkerte bogstaver");
        System.out.println(g.log(bruger));
    }
    
    
}
