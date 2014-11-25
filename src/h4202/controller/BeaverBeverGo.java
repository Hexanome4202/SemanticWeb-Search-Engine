package h4202.controller;

import h4202.model.ResultModel;
import h4202.module2.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class BeaverBeverGo {
	
	private static final Integer LIMIT_WORDS=150;

	
	public static final String IMAGE= "http://dbpedia.org/ontology/thumbnail";
	public static final String LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	public static final String ABSTRACT = "http://dbpedia.org/ontology/abstract";
	public static final String WIKIPEDIA_LINK = "http://xmlns.com/foaf/0.1/isPrimaryTopicOf";
	public static final String HOMEPAGE = "http://xmlns.com/foaf/0.1/homepage";
	
	
	
	/**
	 * Method to find the resource value with its predicate
	 * @param map
	 * @param predicate
	 * @return
	 */
	public String searchForPredicate(Map<String, SortedSet<Triplet>> map, String predicate, String keyWord){
		
		
		
		for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
			
				SortedSet<Triplet> tripletsSet = Entry.getValue();
				for(Triplet t : tripletsSet){
					if (t.getPredicate().equals(predicate)) {
						String[] keys = keyWord.split("\\s+");
						for (String s : keys)
						{
						  if (t.getSubject().toLowerCase().contains(s.toLowerCase()))
						  {
							  	return t.getObject();
						  }
						}
						
					}
				}
		}
		return "";
	}
	
public List<String> searchForPredicateList(Map<String, SortedSet<Triplet>> map, String predicate, String keyWord){
		
		List<String> list = new ArrayList<String>();
		
		for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
			
				SortedSet<Triplet> tripletsSet = Entry.getValue();
				for(Triplet t : tripletsSet){
					if (t.getPredicate().equals(predicate)) {
						String[] keys = keyWord.split("\\s+");
						for (String s : keys)
						{
						  if (t.getSubject().toLowerCase().contains(s.toLowerCase()))
						  {
							  	list.add(t.getObject());
						  }
						}
						
					}
				}
		}
		return list;
	}
	public SortedSet<ResultModel> searchForResultList(Map<String, SortedSet<Triplet>> map, String keyWord){
		
		SortedSet<ResultModel> list = new TreeSet<ResultModel>();
		
		for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
			
			SortedSet<Triplet> tripletsSet = Entry.getValue();
			for(Triplet t : tripletsSet){
				System.out.println(t.toString());
				String img="";
				String desc="";
				String wikiPage="";
				String homePage="";
				if (t.getPredicate().equals(BeaverBeverGo.LABEL)) {
					String[] keys = keyWord.split("\\s+");
					for (String s : keys)
					{
					  if (t.getSubject().toLowerCase().contains(s.toLowerCase()))
					  {
						  img=searchForSubjectPredicate(map, t.getSubject() , BeaverBeverGo.IMAGE, keyWord);
						  desc=searchForSubjectPredicate(map, t.getSubject() , BeaverBeverGo.ABSTRACT, keyWord);
						  wikiPage=searchForSubjectPredicate(map, t.getSubject(), BeaverBeverGo.WIKIPEDIA_LINK, keyWord);
						  homePage=searchForSubjectPredicate(map, t.getSubject(), BeaverBeverGo.HOMEPAGE, keyWord);
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
						  list.add(new ResultModel(t.getObject(), img, description, wikiPage, homePage));
					  }
					}
					
				}
			}
	}
		
		return list;
	}
	
public String searchForSubjectPredicate(Map<String, SortedSet<Triplet>> map,String subject, String predicate,String keyWord){
		
		
		
		for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
			
				SortedSet<Triplet> tripletsSet = Entry.getValue();
				for(Triplet t : tripletsSet){
					if (t.getPredicate().equals(predicate) && t.getSubject().equals(subject)) {
						String[] keys = keyWord.split("\\s+");
						for (String s : keys)
						{
						  if (t.getSubject().toLowerCase().contains(s.toLowerCase()))
						  {
							  	return t.getObject();
						  }
						}
						
					}
				}
		}
		return "";
	}

}
