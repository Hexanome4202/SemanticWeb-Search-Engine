package h4202.controller;

import h4202.Similarity;
import h4202.GoogleResults;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class ResearchAction extends Action {

	@Override
	public void execute(HttpServletRequest request, HttpSession session) {
		String keyWords = request.getParameter("keyWords");
		
		// Search for keywords in the first page of google
		GoogleResults.save("results.json", GoogleResults.getElements(GoogleResults.search(keyWords, 1)));

		Similarity sim = new Similarity();
		BeaverBeverGo bv = new BeaverBeverGo();
		sim.readAll();
		sim.fillSimilarityList();
		String img = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.IMAGE);
		String label = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.LABEL);
		String desc = bv.searchForPredicate(sim.getMapFiles(),
				BeaverBeverGo.ABSTRACT);
		
		session.setAttribute("img", img);
		session.setAttribute("label", label);
		session.setAttribute("description", desc);
	}

}
