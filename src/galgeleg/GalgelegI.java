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
public interface GalgelegI extends java.rmi.Remote {
    
    @WebMethod public String synligtOrd() throws java.rmi.RemoteException;
    @WebMethod public void gætBogstav(String ord) throws java.rmi.RemoteException;
    @WebMethod public String log() throws java.rmi.RemoteException;    
    @WebMethod public String logWeb() throws java.rmi.RemoteException;
    @WebMethod public boolean spilSlut() throws java.rmi.RemoteException;
    @WebMethod public void nulstil() throws java.rmi.RemoteException;
    @WebMethod public String ordet() throws java.rmi.RemoteException; 
    @WebMethod boolean hentBruger(String brugernavn, String password) throws java.rmi.RemoteException;     
}
