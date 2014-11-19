package h4202.module2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Entities {
	public static void main(String[] args) {
		
		HashMap<String, Set<String>> entities = new HashMap<String, Set<String>>();
		
		JSONParser parser = new JSONParser();
		try {
			FileReader filereader = new FileReader(new File("part1_sample.json"));
			
			JSONObject obj = (JSONObject) parser.parse(filereader);
			JSONArray pages = (JSONArray) obj.get("pages");
			
			for(int i=0; i < pages.size(); i++){
				JSONObject doublet = (JSONObject)pages.get(i);
				
				Set<String> uris = getEntities(doublet.get("text").toString());
				
				//TODO pour chaque entité -> relaxer le graphe
				
				entities.put(doublet.get("link").toString(), uris);
			}
			
		} catch (IOException ie) {
			System.out.println(ie);
		} catch (ParseException pe) {
			System.out.println(pe);
		}
	}
	
	public static Set<String> getEntities(String text){
		Set<String> result = new HashSet<String>();
		
		String confidence = "0.2";
		String support = "20";
		String params = "&confidence="+ confidence +"&support=" + support;
		
		try {
			String spotlightResult = "";
			URI uri = new URI("http","//spotlight.dbpedia.org/rest/annotate?text="+text+params,null);
			// get URL content
			URLConnection conn = uri.toURL().openConnection();
			conn.setRequestProperty("Accept", "application/json");
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				spotlightResult += inputLine;
			}
			br.close();
			
			JSONParser parser = new JSONParser();
			
			JSONObject obj = (JSONObject) parser.parse(spotlightResult);
			JSONArray resources = (JSONArray) obj.get("Resources");
			for(int i=0; i < resources.size(); i++){
				result.add((((JSONObject)resources.get(i)).get("@URI")).toString());
			}
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
