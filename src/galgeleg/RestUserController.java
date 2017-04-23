package galgeleg;

import static spark.Spark.*;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Route;
import utils.ResponseError;

import static utils.JsonUtil.*;

import java.util.ArrayList;


public class RestUserController {
	public RestUserController(final GalgelegI userService) {
	//REST
		
		/*
		 * @author Asger
		 * 
		 */
		
			/*
			 @WebMethod public String synligtOrd(String brugerID);
			    @WebMethod public void gætBogstav(String ord, String brugerID);
			    @WebMethod public String log(String brugerID);    
			    @WebMethod public String logWeb(String brugerID);
			    @WebMethod public boolean spilSlut();
			    @WebMethod public void nulstil(String brugerID);   
			    @WebMethod public String ordet(String brugerID); 
			    @WebMethod boolean hentBruger(String brugernavn, String password);  //ikke implementeret
			    @WebMethod void playerCheck(String brugernavn); 
			    
			    @WebMethod public Boolean isGameStarted(String brugerID); 
			    @WebMethod public void newMulti(String brugernavn);
			    @WebMethod public ArrayList<String> getMultiListNames();
			    @WebMethod public ArrayList joinMulti(String lobbyName, String brugerID);
			    @WebMethod public void startGame(String brugerID);
			    @WebMethod public String gætBogstavMultiOgLog(String ord, String brugernavn);
			    @WebMethod public Boolean isContinueAvailable(String bruger);
			    @WebMethod public void leaveLobby(String brugerID);
			    */
		
		get("/galgeleg/synligtOrd/:id", (req, res) -> {
			  String id = req.params(":id");
			  String synligtOrd = userService.synligtOrd(id);
			  if (synligtOrd != null) {
			    return synligtOrd;
			  }
			  res.status(400);
			  return new ResponseError("No user with id '%s' found", id);
			}, json());

		
		post("/galgeleg/gaetBogstav/:id", (req, res) -> userService.gætBogstav(
			    req.queryParams("ord"),
			    req.params(":id")
			), json());
		
		
		get("/galgeleg/log/:id", (req, res) -> {
			  String id = req.params(":id");
			  String log = userService.log(id);
			  if (log != null) {
			    return log;
			  }
			  res.status(400);
			  return new ResponseError("No user with id '%s' found", id);
			}, json());
		
		get("/galgeleg/logWeb/:id", (req, res) -> {
			  String id = req.params(":id");
			  String logWeb = userService.logWeb(id);
			  if (logWeb != null) {
			    return logWeb;
			  }
			  res.status(400);
			  return new ResponseError("No user with id '%s' found", id);
			}, json());
		
		get("/galgeleg/spilSlut", (req, res) -> {
			  
			  Boolean spilSlut = userService.spilSlut();
			  if (spilSlut != null) {
			    return spilSlut;
			  }
			  res.status(400);
			  return new ResponseError("spilSlut() er null");
			}, json());
	
		
		post("/galgeleg/nulstil/:id", (req, res) -> userService.nulstil(
			    req.params(":id")
			), json());
		
		get("/galgeleg/ordet/:id", (req, res) -> {
			  String id = req.params(":id");
			  String ordet = userService.ordet(id);
			  if (ordet != null) {
			    return ordet;
			  }
			  res.status(400);
			  return new ResponseError("No user with id '%s' found", id);
			}, json());
		
		post("/galgeleg/playerCheck/:id", (req, res) -> userService.playerCheck(
			    req.params(":id")
			), json());
		
		
		get("/galgeleg/isGameStarted/:id", (req, res) -> {
			  String id = req.params(":id");
			  Boolean isGameStarted = userService.isGameStarted(id);
			  if (isGameStarted != null) {
			    return isGameStarted;
			  }
			  res.status(400);
			  return new ResponseError("No user with id '%s' found", id);
			}, json());
		
		post("/galgeleg/newMulti/:id", (req, res) -> userService.newMulti(
			    req.params(":id")
			), json());
	
		
		/*get("/galgeleg/getMultiListNames/", (req, res) -> {			//Ved ikke helt om det er REST metoden her den er galt med eller galgeserveren
			  
			  ArrayList<String> availableGames = userService.getMultiListNames();
			  if (availableGames != null) {
				  Gson gson = new Gson();
				    StringBuilder sb = new StringBuilder();
				    for(String d : availableGames) {
				        sb.append(gson.toJson(d));
				    }
				    return sb.toString();
			  }
			  res.status(400);
			  return new ResponseError("Available games was null");
			}, json());*/
		
		post("/galgeleg/getMultiListNames/", (req, res) -> userService.getMultiListNames( //Virker ikke ordentligt, ved ikke om det er arraylist retur type der giver problemer eller galgelegImpl metoden
				
			    
			), json());
		
		post("/galgeleg/joinMulti/:id", (req, res) -> userService.joinMulti(  //Virker ikke ordentligt, ved ikke om det er arraylist retur type der giver problemer eller galgelegImpl metoden
				req.queryParams("Hostname"),
			    req.params(":id")
			    
			), json());
		
		post("/galgeleg/startGame/:id", (req, res) -> userService.startGame(
			    req.params(":id")
			    
			), json());
		
		post("/galgeleg/gaetBogstavMultiOgLog/:id", (req, res) -> userService.gætBogstavMultiOgLog(
			    req.queryParams("ord"),
			    req.params(":id")
			), json());
		
		post("/galgeleg/isContinueAvailable/:id", (req, res) -> userService.isContinueAvailable(
			    req.params(":id")
			), json());
		
		post("/galgeleg/leaveLobby/:id", (req, res) -> userService.leaveLobby(
			    req.params(":id")
			), json());
		
}
}


	

