package h4202;

import java.io.BufferedReader;
import java.io.FileWriter;
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
	private static final String GOOGLE_API_KEY = "AIzaSyD4vwwD8HzF1NLbhE04U0td5NOmH1NYwpw";
	private static final String GOOGLE_URL_QUERY = "https://www.googleapis.com/customsearch/v1";
	
	private static final String ALCHEMY_API_KEY = "85c049ad20d7b445086d6f8aa0e738660f232f9c";
	private static final String ALCHEMY_URL_QUERY = "http://access.alchemyapi.com/calls/url/URLGetText?";
	
	public static void main(String[] args) {
		save("test.json", getElements(search("barack obama", 1)));
	}
	
	/**
	 * @param keywords used for the google search
	 * @param pageNum number of the result page
	 * @return
	 */
	public static String search(String keywords, int pageNum) {
		URL url;
		String text = "";
		int startIndex = (pageNum - 1) * 10 + 1;
		// TODO: change?
		if(startIndex > 100) startIndex = 1;
		keywords = keywords.replace(" ", "%20");
		
		try {
			// get URL content
			url = new URL(GOOGLE_URL_QUERY + "?key=" + GOOGLE_API_KEY + "&cx=015624405265777598503:nlbkiqyhteg&q=" + keywords + "&start=" + startIndex + "&num=10");
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
		System.out.println(text);
		return text;
	}
	
	/**
	 * 
	 * @param json
	 * @return a Google element created with the html
	 */
	public static List<GoogleElement> getElements(String json) {
		System.out.println("Début getElements");
		JSONParser parser = new JSONParser();
		List<GoogleElement> elements = new ArrayList<GoogleElement>();
		String link;
		String text;
		String snippet;
		
		Object obj;
		try {
			obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			 
			// loop array
			JSONArray links = (JSONArray) jsonObject.get("items");
			Iterator<JSONObject> iterator = links.iterator();
			while (iterator.hasNext()) {
				jsonObject = iterator.next();
				link = jsonObject.get("link").toString();
				if(!link.contains(".pdf")) {
					text = getTextFromPage(link);
					snippet = jsonObject.get("snippet").toString();
					if(text != "")
						elements.add(new GoogleElement(link, text + snippet));
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fin getElements");
		return elements;
	}
	/**
	 * 
	 * @param link
	 * @return the text from the html web page
	 */
	public static String getTextFromPage(String link) {
		
		URL url;
		String json = "";
		JSONParser parser = new JSONParser();
		 
		try {
			// get URL content
			url = new URL(ALCHEMY_URL_QUERY + "apikey=" + ALCHEMY_API_KEY + "&url=" + link + "&outputMode=json");
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				json += inputLine;
			}
			br.close();
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
	 
			Object obj = parser.parse(json);
	 
			JSONObject jsonObject = (JSONObject) obj;
	 
			String text = (String) jsonObject.get("text");
			return text;
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param filename
	 * @param elements
	 * desc : save link and text in json form for each page in a file 
	 */
	public static void save(String filename, List<GoogleElement> elements) {
		JSONArray list = new JSONArray();
		JSONObject elem;
		GoogleElement element;
		for(int i = 0; i < elements.size(); ++i) {
			elem = new JSONObject();
			element = elements.get(i);
			elem.put("link", element.getLink());
			elem.put("text", element.getText());
			list.add(elem);
		}
	 
		try {
			FileWriter file = new FileWriter(filename);
			file.write("{\"pages\":" + list.toJSONString() + "}");
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	
	public static String cleanText(String text) {
		return text;
	}
}
