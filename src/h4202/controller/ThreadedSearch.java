package h4202.controller;

import h4202.GoogleResults;
import h4202.Similarity;
import h4202.model.GoogleElement;
import h4202.module2.Entities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
		// ----- Part I
		String keyWords = request.getParameter("keyWords");
		String json = GoogleResults.search(keyWords, 1);
		GoogleElement[] elements = new GoogleElement[10];
		Semaphore semaphore = new Semaphore(0);
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
				new ThreadSearch(jsonObject, elements, i, semaphore).run();
				++i;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			semaphore.acquire(10);
		} catch (InterruptedException e) {
			// TODO: Do something smarter
			e.printStackTrace();
			return;
		}
		List<GoogleElement> elems = Arrays.asList(elements);
		JSONArray jsonArray = GoogleResults.createJSON(elems);
//		long estimatedTime = System.nanoTime() - startTime;
//		System.out.println(estimatedTime);
//		System.exit(0);
		
		// ----- PART II & III
		Similarity sim = new Similarity(Entities.getGraph(jsonArray));
		BeaverBeverGo bv = new BeaverBeverGo();
		//sim.readAll();
		sim.fillSimilarityList();
		String img = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.IMAGE);
		String label = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.LABEL);
		String desc = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.ABSTRACT);
		
		//System.out.println(img + "   " + label + "   " + desc);
		
		session.setAttribute("keyWords", keyWords);
		session.setAttribute("img", img);
		session.setAttribute("label", label);
		session.setAttribute("description", desc);
	}

}
