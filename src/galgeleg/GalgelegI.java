/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Martin
 */
@WebService
public interface GalgelegI {
    
    @WebMethod public String synligtOrd(String brugerID);
    @WebMethod public void gætBogstav(String ord, String brugerID);
    @WebMethod public String log(String brugerID);    
    @WebMethod public String logWeb(String brugerID);
    @WebMethod public boolean spilSlut();
    @WebMethod public void nulstil(String brugerID);
    @WebMethod public String ordet(String brugerID); 
    @WebMethod boolean hentBruger(String brugernavn, String password); 
    @WebMethod void playerCheck(String brugernavn); 
    @WebMethod public Boolean isGameStarted(String brugerID); 
    @WebMethod public void newMulti(String brugernavn);
    @WebMethod public ArrayList<String> getMultiListNames();
    @WebMethod public ArrayList joinMulti(String lobbyName, String brugerID);
    @WebMethod public void startGame(String brugerID);
    @WebMethod public String gætBogstavMultiOgLog(String ord, String brugernavn);
}
