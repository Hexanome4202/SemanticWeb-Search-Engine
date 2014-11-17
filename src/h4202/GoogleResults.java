package h4202;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GoogleResults {
	private static String API_KEY = "AIzaSyD4vwwD8HzF1NLbhE04U0td5NOmH1NYwpw";
	private static String URL_QUERY = "https://www.googleapis.com/customsearch/v1";
	
	public static void main(String[] args) {
		System.out.println(getElements(search("obama")));
	}
	
	public static String search(String keywords) {
		URL url;
		String text = "";
		 
		try {
			// get URL content
			url = new URL(URL_QUERY + "?key=" + API_KEY + "&cx=017576662512468239146:omuauf_lfve&q=" + keywords);
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				text += inputLine;
			}
			br.close();
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text;
	}
	
	/**
	 * 
	 * @param json
	 * @return a Google element created with the html
	 */
	public static List<GoogleElement> getElements(String json) {
		JSONParser parser = new JSONParser();
		List<GoogleElement> elements = new ArrayList<GoogleElement>();
		
		Object obj;
		try {
			obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			 
			// loop array
			JSONArray links = (JSONArray) jsonObject.get("items");
			Iterator<JSONObject> iterator = links.iterator();
			while (iterator.hasNext()) {
				elements.add(new GoogleElement(iterator.next().get("link").toString(), ""));
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return elements;
	}
	
	public static String getTextFromPage(String link) {
		return "";
	}
}
