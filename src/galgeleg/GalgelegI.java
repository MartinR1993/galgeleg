/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;

import brugerautorisation.data.Bruger;

/**
 *
 * @author Martin
 */
@WebService
public interface GalgelegI {
    
    @WebMethod public String synligtOrd(String brugerID);
    @WebMethod public String gætBogstav(String ord, String brugerID);
    @WebMethod public String log(String brugerID);    
    @WebMethod public String logWeb(String brugerID);
    @WebMethod public boolean spilSlut();
    @WebMethod public String nulstil(String brugerID);
    @WebMethod public String ordet(String brugerID); 
    @WebMethod boolean hentBruger(String brugernavn, String password); 
    @WebMethod public String playerCheck(String brugernavn); 
    @WebMethod public Boolean isGameStarted(String brugerID); 
    @WebMethod public String newMulti(String brugernavn);
    @WebMethod public ArrayList<String> getMultiListNames();
    @WebMethod public ArrayList joinMulti(String lobbyName, String brugerID);
    @WebMethod public String startGame(String brugerID);
    @WebMethod public String gætBogstavMultiOgLog(String ord, String brugernavn);
    @WebMethod public Boolean isContinueAvailable(String bruger);
    @WebMethod public String leaveLobby(String brugerID);
    @WebMethod public String multiLog(String brugerID);
    @WebMethod public String clearLobby(String brugerID);
    @WebMethod public boolean isMyMultiActive(String brugerID);
    @WebMethod public String isMyMultiOver(String brugerID);
    @WebMethod public Bruger login(String brugerID, String password);
    @WebMethod public ArrayList<scoreDTO> getScores();
    @WebMethod boolean enoughPlayers(String brugerID);
    @WebMethod public String multiLogWeb(String brugerID);
    @WebMethod public String isMyMultiOverWithoutHighscore(String brugerID);
    @WebMethod public ArrayList peopleInLobby(String brugerID);
}
