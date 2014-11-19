package h4202.module2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
				entities.put(doublet.get("link").toString(), uris);
				
				getTriplets(uris);
			}
			
		} catch (IOException ie) {
			System.out.println(ie);
		} catch (ParseException pe) {
			System.out.println(pe);
		}
	}
	
	public static Set<Triplet> getTriplets(Set<String> resources) {
		Set<Triplet> triplets = new HashSet<Triplet>();		 
				
		String uris = "";
		Iterator<String> it = resources.iterator();
		while(it.hasNext()){
			
			try {
				uris += URLEncoder.encode("<"+ it.next() +">", "UTF8");
				if(it.hasNext()) 
					uris += "%2C";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		try {
			String spotlightResult = "";
			
			URI uri = new URI("http://live.dbpedia.org/sparql" +
					"?query=SELECT+*+WHERE+%7B+%3Fs+%3Fp+%3Fo.+FILTER%28%3Fs+in+%28"
					+ uris 
					+ "%29%29%0D%0AFILTER%28%3Fp+not+in+%28%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23sameAs%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageID%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageLength%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageModified%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageOutDegree%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageRevisionID%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageRevisionLink%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Falign%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Fcaption%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Fdirection%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2FfooterAlign%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Frows%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageEditLink%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2FwikiPageExtracted%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2FimageSize%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Fimage%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2FwikiPageUsesTemplate%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Fwidth%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2FheaderAlign%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2FimageName%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Fsignature%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2FhasPhotoCollection%3E%2C+%3Chttp%3A%2F%2Fwww.georss.org%2Fgeorss%2Fpoint%3E%2C+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2Fbgcolor%3E%29%29%7D+&format=application%2Fsparql-results%2Bjson&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=on"
					);
			
			
			// get URL content
			URLConnection conn = uri.toURL().openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String inputLine;
 
			while ((inputLine = br.readLine()) != null) {
				spotlightResult += inputLine;
			}
			br.close();
			
			System.out.println(spotlightResult);
			
			/*JSONParser parser = new JSONParser();
			
			JSONObject obj = (JSONObject) parser.parse(spotlightResult);
			JSONArray resources = (JSONArray) obj.get("Resources");
			for(int i=0; i < resources.size(); i++){
				result.add((((JSONObject)resources.get(i)).get("@URI")).toString());
			}*/
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
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
	
	public class Triplet{
		public String predicate;
		public String subject;
		public String object;
		
		public Triplet(String subject, String predicate, String object){
			this.subject = subject;
			this.object = object;
			this.predicate = predicate;
		}
	}
}
