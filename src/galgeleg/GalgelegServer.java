/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.rmi.Naming;
import java.rmi.Remote;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Martin
 */
public class GalgelegServer {
    
    public static void main(String[] arg) throws Exception
	{
		System.out.println("publicerer Galgelegtjeneste");
		GalgelegI g = new GalgelegImpl();
    // Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og 
		Endpoint.publish("http://[::]:9943/galgelegtjeneste", g);
		System.out.println("Galgelegtjeneste publiceret.");
                System.out.println("Ordet : " + g.ordet());

	}
}
