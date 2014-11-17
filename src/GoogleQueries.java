package h4202;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GoogleResults {
	private static String API_KEY = "AIzaSyD4vwwD8HzF1NLbhE04U0td5NOmH1NYwpw";
	private static String URL_QUERY = "https://www.googleapis.com/customsearch/v1";
	
	public static void main(String[] args) {
		search("obama");
	}
	
	public static void search(String keywords) {
		URL url;
		 
		try {
			// get URL content
			url = new URL(URL_QUERY + "?key=" + API_KEY + "&cx=017576662512468239146:omuauf_lfve&q=" + keywords);
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				System.out.println(inputLine);
			}
			br.close();
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
}
