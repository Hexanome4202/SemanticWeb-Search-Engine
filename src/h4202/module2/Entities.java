package h4202.module2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
				
				Set<String> uris = new HashSet<String>();
				
				//TODO prendre le texte et trouver les entités associées
				//TODO pour chaque entité -> relaxer le graphe
				
				entities.put(doublet.get("link").toString(), uris);
			}
			
			
		} catch (IOException ie) {
			System.out.println(ie);
		} catch (ParseException pe) {
			System.out.println(pe);
		}	
		
		getEntities(" ");
	}
	
	public static Set<String> getEntities(String text){
		//TODO clean text
		String cleanText = "President%20Michelle%20Obama%20called%20Thursday%20on%20Congress%20to%20extend%20a%20tax%20break%20for%20students%20included%20in%20last%20year%27s%20economic%20stimulus%20package,%20arguing%20that%20the%20policy%20provides%20more%20generous%20assistance.";
		String confidence = "0.2";
		String support = "20";
		
		String params = "&confidence="+ confidence +"&support=" + support;
		
		try {
			String result = "";
			// get URL content
			URL url = new URL("http://spotlight.dbpedia.org/rest/annotate?text="+ cleanText + params);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Accept", "application/json");
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				result += inputLine;
			}
			br.close();
			
			System.out.println(result);
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
