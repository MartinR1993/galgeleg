/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import javax.jws.WebMethod;
import javax.xml.ws.Endpoint;

import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

import java.util.ArrayList;


/**
 *
 * @author Martin
 * @author Asger
 */
public class GalgelegServer {
    
    public static void main(String[] arg) throws Exception
	{
		System.out.println("publicerer Galgelegtjeneste");
		GalgelegI g = new GalgelegImpl();
    // Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og 
                
                // Forbindelse til lokal server
//		Endpoint.publish("http://[::]:9943/galgelegtjeneste", g);
                
                // Forbindelse til Jacob's server
    		Endpoint.publish("http://[::]:/galgelegtjeneste", g);
                
		System.out.println("Galgelegtjeneste publiceret.");
//                System.out.println("Ordet : " + g.ordet());
		
		
		//REST
		new RestUserController(g);
		

	}
}
