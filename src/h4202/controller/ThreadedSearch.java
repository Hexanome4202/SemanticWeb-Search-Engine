package h4202.controller;

import h4202.GoogleResults;
import h4202.Similarity;
import h4202.controller.ThreadSearch;
import h4202.module2.Triplet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.bcel.internal.generic.SIPUSH;

public class ThreadedSearch extends Action {
	
	private static final Integer LIMIT_WORDS=150;

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
			File f = new File(keyWords+".ser");
			if(f.exists() && !f.isDirectory()){
				url_triplets = deserializeGraph(keyWords);
			} else {
				String json = GoogleResults.search(keyWords, 1);
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
			}
			
			// ----- PART II & III
			Similarity sim = new Similarity(url_triplets);
			BeaverBeverGo bv = new BeaverBeverGo();
			//sim.readAll();
			sim.fillSimilarityList();
			String img = bv.searchForPredicate(sim.getMapFiles(),
					BeaverBeverGo.IMAGE, keyWords);
			String label = bv.searchForPredicate(sim.getMapFiles(),
					BeaverBeverGo.LABEL, keyWords);
			String desc = bv.searchForPredicate(sim.getMapFiles(),
					BeaverBeverGo.ABSTRACT, keyWords);
			String[] descriptionArray = desc.split("\\s+");
			String description="";
			if (descriptionArray.length<=LIMIT_WORDS) {
				description=desc;
			} else {
				for(int j=0;j<=LIMIT_WORDS;j++){
					description=description+descriptionArray[j]+" ";
				}
				description=description+"...";
			}
			
			//System.out.println(img + "   " + label + "   " + desc);
			
			session.setAttribute("keyWords", keyWords);
			session.setAttribute("img", img);
			session.setAttribute("label", label);
			session.setAttribute("description", description);
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
}
