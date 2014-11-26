package h4202.controller;

import h4202.GoogleResults;
import h4202.Similarity;
import h4202.model.ResultModel;
import h4202.module2.Triplet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ThreadedSearch extends Action {
	

	@Override
	/**
	 * TODO: oh god, it's so dirty... It obviously needs a big refactoring
	 */
	public void execute(HttpServletRequest request, HttpSession session) {
		//long startTime = System.nanoTime();
		HashMap<String, SortedSet<Triplet>> url_triplets = new HashMap<String, SortedSet<Triplet>>();
		// ----- Part I
		String keyWords = request.getParameter("keyWords");
		
		if(keyWords != null) {
			String json = GoogleResults.search(keyWords, 1);
			File f = new File(keyWords+".ser");
			if(f.exists() && !f.isDirectory()){
				url_triplets = deserializeGraph(keyWords);
			}
			
			Semaphore semaphore = new Semaphore(0);
			Semaphore hashMapSemaphore = new Semaphore(1);
			int i = 0;
			
			JSONParser parser = new JSONParser();
			
			Object obj;
			try {
				obj = parser.parse(json);
				JSONObject jsonObject = (JSONObject) obj;
				 
				// loop array
				JSONArray links = (JSONArray) jsonObject.get("items");
				Iterator<JSONObject> iterator = links.iterator();
				while (iterator.hasNext()) {
					jsonObject = iterator.next();
					new Thread(new ThreadSearch(jsonObject, url_triplets, semaphore, hashMapSemaphore)).start();
					++i;
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				semaphore.acquire(i);
			} catch (InterruptedException e) {
				// TODO: Do something smarter
				e.printStackTrace();
				return;
			}
//				long estimatedTime = System.nanoTime() - startTime;
//				System.out.println(estimatedTime);
//				System.exit(0);
			
			cache(url_triplets, keyWords);
			
			// ----- PART II & III
			Similarity sim = new Similarity(url_triplets);
			BeaverBeverGo bv = new BeaverBeverGo();
			sim.fillSimilarityList();
			
			Set<ResultModel> rM=bv.searchForResultList(sim.getMapFiles(), keyWords, sim);

			List<ResultModel> listRM = new ArrayList<>(rM);
			
			Comparator<ResultModel> comparator = new Comparator<ResultModel>() {
			    public int compare(ResultModel c1, ResultModel c2) {
			        return (int) ((c2.getSimilarityAverage() - c1.getSimilarityAverage())*10000000);
			    }
			};

			Collections.sort(listRM, comparator);
			
			for(ResultModel r : listRM){
				//System.out.println(r.toString());
			}
			
			//sim.createGraphViz(keyWords+".graph");
			String script = "<script>";
			BufferedReader br;
		    try {
		    	br = new BufferedReader(new FileReader("vis.min.js"));
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        script += sb.toString() + "</script>";
		    } catch (FileNotFoundException e) {
		    	
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    HashMap<String, Integer> map = new HashMap<String, Integer>();
		    
		    session.setAttribute("viz", script);
			session.setAttribute("keyWords", keyWords);
			session.setAttribute("resultsList", rM);
			session.setAttribute("graph", sim.createGraphViz(map));
			session.setAttribute("resultsList", listRM);
			session.setAttribute("map", map);
		}
	}
	
	private void cache(HashMap<String, SortedSet<Triplet>> graph, String keyWords) {
		try
		{
			FileOutputStream fos = new FileOutputStream(keyWords + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(graph);
			oos.close();
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Method to deserialize a Graph
	 * @param keyWord : name of the file
	 * @return : the graph
	 */
	private HashMap<String, SortedSet<Triplet>> deserializeGraph(String keyWord){
		HashMap<String, SortedSet<Triplet>> results = new HashMap<String, SortedSet<Triplet>>();
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyWord+".ser"));
			results = (HashMap<String, SortedSet<Triplet>>) in.readObject();
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return results;
	}
	
	private boolean similarResults(String keyWords, String json, HashMap<String, SortedSet<Triplet>> url_triplets) {
		boolean result = true;
		JSONParser parser = new JSONParser();
		Object obj;
		String link;
		
		try {
			obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			 
			// loop array
			JSONArray links = (JSONArray) jsonObject.get("items");
			Iterator<JSONObject> iterator = links.iterator();
			while (iterator.hasNext() && result) {
				jsonObject = iterator.next();
				link = jsonObject.get("link").toString();
				link = link.replace("\\/", "/");
				if(! url_triplets.containsKey(link)) result = false;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
