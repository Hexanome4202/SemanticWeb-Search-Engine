package h4202.module2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Entities {
	public static void main(String[] args) {
		
		HashMap<String, String> urlTexts = new HashMap<String, String>();
		
		JSONParser parser = new JSONParser();
		try {
			FileReader filereader = new FileReader(new File("part1_sample.json"));
			
			JSONObject obj = (JSONObject) parser.parse(filereader);
			JSONArray pages = (JSONArray) obj.get("pages");
			
			for(int i=0; i < pages.size(); i++){
				JSONObject doublet = (JSONObject)pages.get(i);
				urlTexts.put(doublet.get("link").toString(), doublet.get("text").toString());
			}
			
			
		} catch (IOException ie) {
			System.out.println(ie);
		} catch (ParseException pe) {
			System.out.println(pe);
		}	
	}
}
