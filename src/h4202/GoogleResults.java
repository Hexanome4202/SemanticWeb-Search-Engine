package h4202;

import h4202.model.GoogleElement;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GoogleResults {
	//Vitré's key
	private static final String GOOGLE_API_KEY = "AIzaSyD4vwwD8HzF1NLbhE04U0td5NOmH1NYwpw";
	//Vipi's key
	//private static final String GOOGLE_API_KEY = "AIzaSyCGxfGjMGKQhnQ2cm1MkXdFL6UCFgScgMQ";
	private static final String GOOGLE_URL_QUERY = "https://www.googleapis.com/customsearch/v1";
	
	private static final String ALCHEMY_API_KEY = "85c049ad20d7b445086d6f8aa0e738660f232f9c";
	private static final String ALCHEMY_URL_QUERY = "http://access.alchemyapi.com/calls/url/URLGetText?";
	
	private static final int NB_RESULTS = 3;
	
	public static void main(String[] args) {
		save("search.json", createJSON(getElements(search("barack obama", 1))));
	}
	
	/**
	 * @param keywords used for the google search
	 * @param pageNum number of the result page
	 * @return
	 */
	public static String search(String keywords, int pageNum) {
		URL url;
		String text = "";
		int startIndex = (pageNum - 1) * NB_RESULTS + 1;
		// TODO: change?
		if(startIndex > 100) startIndex = 1;
		keywords = keywords.replace(" ", "%20");
		
		try {
			// get URL content
			url = new URL(GOOGLE_URL_QUERY + "?key=" + GOOGLE_API_KEY + "&cx=015624405265777598503:nlbkiqyhteg&q=" + keywords + "&start=" + startIndex + "&num=" + NB_RESULTS);
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
	 * TODO: Clean text
	 * @param json
	 * @return a Google element created with the html
	 */
	public static List<GoogleElement> getElements(String json) {
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
				link = link.replace("\\/", "/");
				if(!link.contains(".pdf")) {
					text = getTextFromPage(link).replace("\\/", "/").replaceAll("\\s+", " ").replaceAll("\\\"", "").replaceAll("\\[.*?\\]", "");
					snippet = jsonObject.get("snippet").toString();
					if(text != "")
						elements.add(new GoogleElement(link, text + snippet));
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elements;
	}
	/**
	 * Uses Alchemy
	 * TODO: try to make it faster
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
	 * Local version
	 * @param link
	 * @return the text from the html web page
	 */
	public static String getTextFromPage2(String link) {
		URL url;
		String text = "";
		String html = "";
		 
		try {
			// get URL content
			url = new URL(link);
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				html += inputLine;
			}
			br.close();
			
			Document doc = Jsoup.parse(html);
			text = doc.body().text();
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * Save google elements into JSON syntax
	 * @param elements Google elements to save into JSON
	 * @return list An element of type JSONArray containing all google elements
	 */
	public static JSONArray createJSON(List<GoogleElement> elements) {
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
		
		return list;
	}
	
	/**
	 * Save the JSON object into file
	 * @param filename
	 * @param list JSON list to save into file
	 */
	public static void save(String filename, JSONArray list) {	 
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
