package galgeleg;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;



public class Galgelogik {
    private ArrayList<String> muligeOrd = new ArrayList<String>();
    private String ordet;
    private ArrayList<String> brugteBogstaver = new ArrayList<String>();
    private String synligtOrd;
    private int antalForkerteBogstaver;
    private boolean sidsteBogstavVarKorrekt;
    private boolean spilletErVundet;
    private boolean spilletErTabt;
    
    
    public ArrayList<String> getBrugteBogstaver() {
        return brugteBogstaver;
    }
    
    public String getSynligtOrd() {
        return synligtOrd;
    }
    
    public String getOrdet() {
        return ordet;
    }
    
    public int getAntalForkerteBogstaver() {
        return antalForkerteBogstaver;
    }
    
    public boolean erSidsteBogstavKorrekt() {
        return sidsteBogstavVarKorrekt;
    }
    
    public boolean erSpilletVundet() {
        return spilletErVundet;
    }
    
    public boolean erSpilletTabt() {
        return spilletErTabt;
    }
    
    public boolean erSpilletSlut() {
        return spilletErTabt || spilletErVundet;
    }
    
    
    public Galgelogik() {
    	try {
			hentOrdFraDRUdsendelser();
		} catch (Exception e) {
	        muligeOrd.add("bil");
	        muligeOrd.add("computer");
	        muligeOrd.add("programmering");
	        muligeOrd.add("motorvej");
	        muligeOrd.add("busrute");
	        muligeOrd.add("gangsti");
	        muligeOrd.add("skovsnegl");
	        muligeOrd.add("solsort");
			e.printStackTrace();
		}

        nulstil();
    }
    
    public void nulstil() {
        brugteBogstaver.clear();
        antalForkerteBogstaver = 0;
        spilletErVundet = false;
        spilletErTabt = false;
        ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
        opdaterSynligtOrd();
    }
    
    
    public void opdaterSynligtOrd() {
        synligtOrd = "";
        spilletErVundet = true;
        for (int n = 0; n < ordet.length(); n++) {
            String bogstav = ordet.substring(n, n + 1);
            if (brugteBogstaver.contains(bogstav)) {
                synligtOrd = synligtOrd + bogstav;
            } else {
                synligtOrd = synligtOrd + "*";
                spilletErVundet = false;
            }
        }
    }
    
    public void gætBogstav(String bogstav) {
        if (bogstav.length() != 1) return;
        System.out.println("Der gættes på bogstavet: " + bogstav);
        if (brugteBogstaver.contains(bogstav)) return;
        if (spilletErVundet || spilletErTabt) return;
        
        brugteBogstaver.add(bogstav);
        
        if (ordet.contains(bogstav)) {
            sidsteBogstavVarKorrekt = true;
            System.out.println("Bogstavet var korrekt");
        } else {
            // Vi gættede på et bogstav der ikke var i ordet.
            sidsteBogstavVarKorrekt = false;
            System.out.println("Bogstavet var IKKE korrekt");
            antalForkerteBogstaver = antalForkerteBogstaver + 1;
            if (antalForkerteBogstaver > 6) {
                spilletErTabt = true;
            }
        }
        opdaterSynligtOrd();
    }
    
    public void logStatus() {
        System.out.println("---------- ");
//    System.out.println("- ordet (skjult) = " + ordet);
System.out.println("- synligtOrd = " + synligtOrd);
System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver + "/7");
System.out.println("- brugeBogstaver = " + brugteBogstaver);
if (spilletErTabt) System.out.println("- SPILLET ER TABT");
if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
System.out.println("---------- ");
    }
    
    
    public static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }
    
    public void hentOrdFraDr() throws Exception {
        String data = hentUrl("http://dr.dk");
//        System.out.println("data = " + data);
        
        data = data.replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");
//        System.out.println("data = " + data);
        muligeOrd.clear();
        muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
        
        System.out.println("muligeOrd = " + muligeOrd);
        nulstil();
    }
    
    public void hentOrdFraDRUdsendelser() throws Exception {
        
        Client client = ClientBuilder.newClient();
        Response res = client.target("http://www.dr.dk/mu-online/api/1.3/list/view/mostviewed?channel=dr1&channeltype=TV&limit=3&offset=0")
                .request(MediaType.APPLICATION_JSON).get();
        String svar = res.readEntity(String.class);
        
        try {
            //Parse svar som et JSON-objekt
            JSONObject json = new JSONObject(svar);
            JSONArray jarray = json.getJSONArray("Items");
            muligeOrd.clear();
            
            for (int i = 0; i <= 2; i++) {
                String slug = jarray.getJSONObject(i).getString("Slug");
                System.out.println("\nSlug "+i+": " + jarray.getJSONObject(i).getString("Slug"));
                Response obj = client.target("http://www.dr.dk/mu-online/api/1.3/programcard/" + slug)
                        .request(MediaType.APPLICATION_JSON).get();
                String svar1 = obj.readEntity(String.class);
                             
                JSONObject json1 = new JSONObject(svar1);
                String description = json1.getString("Description");
                String data = description.replaceAll("<.+?>", " ").toLowerCase().replaceAll("[^a-zæøå]", " ");
                System.out.println("data = " + data);
                muligeOrd.addAll(new HashSet<String>(Arrays.asList(data.split(" "))));
                System.out.println("Description "+i+": " + json1.getString("Description"));
            }
            System.out.println("\nmuligeOrd = " + muligeOrd);
            nulstil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
