package h4202.controller;

import h4202.GoogleResults;
import h4202.Similarity;
import h4202.module2.Entities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;


public class ResearchAction extends Action {

	@Override
	public void execute(HttpServletRequest request, HttpSession session) {
		//long startTime = System.nanoTime();
		String keyWords = request.getParameter("keyWords");
		
		// Search for keywords in the first page of google
		JSONArray jsonArray = GoogleResults.createJSON(GoogleResults.getElements(GoogleResults.search(keyWords, 1)));
//		long estimatedTime = System.nanoTime() - startTime;
//		System.out.println(estimatedTime);
//		System.exit(0);
		
		Similarity sim = new Similarity(Entities.getGraph(jsonArray));
		BeaverBeverGo bv = new BeaverBeverGo();
		//sim.readAll();
		sim.fillSimilarityList();
		String img = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.IMAGE, keyWords);
		String label = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.LABEL, keyWords);
		String desc = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.ABSTRACT, keyWords);
		
		//System.out.println(img + "   " + label + "   " + desc);
		
		session.setAttribute("keyWords", keyWords);
		session.setAttribute("img", img);
		session.setAttribute("label", label);
		session.setAttribute("description", desc);
	}

}
