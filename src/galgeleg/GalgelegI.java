/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Martin
 */
@WebService
public interface GalgelegI {
    
    @WebMethod public String synligtOrd();
    @WebMethod public void gætBogstav(String ord, String brugerID);
    @WebMethod public String log();    
    @WebMethod public String logWeb();
    @WebMethod public boolean spilSlut();
    @WebMethod public void nulstil(String brugerID);
    @WebMethod public String ordet(); 
    @WebMethod boolean hentBruger(String brugernavn, String password);     
}