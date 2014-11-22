package h4202.action;

import h4202.GoogleResults;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SearchAction extends Action {

	@Override
	public void execute(HttpServletRequest request, HttpSession session) {
		GoogleResults.search("barack obama", 1);
		//GoogleResults.save("results.json", GoogleResults.getElements(GoogleResults.search("barack obama", 1)));
	}

}
