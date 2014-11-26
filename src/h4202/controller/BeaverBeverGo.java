package h4202.controller;

import h4202.Similarity;
import h4202.model.ResultModel;
import h4202.module2.Triplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	
	public static final String[] PREPOSITIONS ={"for", "the", "as", "to", "de", "le","la"};
	
	
	
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
	public SortedSet<ResultModel> searchForResultList(Map<String, SortedSet<Triplet>> map, String keyWord, Similarity sim){
		
		
		SortedSet<ResultModel> list = new TreeSet<ResultModel>();
		
		for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
			
			String url = Entry.getKey();
			SortedSet<Triplet> tripletsSet = Entry.getValue();
			for(Triplet t : tripletsSet){
				String img="";
				String desc="";
				String wikiPage="";
				String homePage="";
				List<String> categories = new ArrayList<String>();
				if (t.getPredicate().equals(BeaverBeverGo.LABEL)) {
					String[] keys = keyWord.split("\\s+");
					for (String s : keys)
					{
						if(!Arrays.asList(PREPOSITIONS).contains(s)){
							  if (t.getSubject().toLowerCase().contains(s.toLowerCase()))
							  {
								  int i =1;
								  if (t.getObject().toLowerCase().equals(keyWord.toLowerCase())) {
									i=10;
								  }else if(t.getObject().toLowerCase().contains(keyWord.toLowerCase())){
									  i=5;
								  }
								  img=searchForSubjectPredicate(tripletsSet, t.getSubject() , BeaverBeverGo.IMAGE, keyWord);
								  desc=searchForSubjectPredicate(tripletsSet, t.getSubject() , BeaverBeverGo.ABSTRACT, keyWord);
								  wikiPage=searchForSubjectPredicate(tripletsSet, t.getSubject(), BeaverBeverGo.WIKIPEDIA_LINK, keyWord);
								  homePage=searchForSubjectPredicate(tripletsSet, t.getSubject(), BeaverBeverGo.HOMEPAGE, keyWord);
								  categories=searchForSubjectCategories(map, t.getSubject());
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
								  list.add(new ResultModel(t.getObject(), img, description, wikiPage, homePage, url, sim.similatiryAverage(url)*i, categories));
							  }
						}
					}
					
				}
			}
	}
		
		
		return list;
	}
	
public String searchForSubjectPredicate(SortedSet<Triplet> tripletsSet,String subject, String predicate,String keyWord){
		
		
		
		//for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
			
				//SortedSet<Triplet> tripletsSet = Entry.getValue();
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
		//}
		return "";
	}

	/**
	 * TODO: pretify categories (i.e. better name...)
	 * TODO: select 3 best
	 * @param map
	 * @param subject
	 * @return
	 */
	public List<String> searchForSubjectCategories(Map<String, SortedSet<Triplet>> map,String subject){
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		for (Map.Entry<String, SortedSet<Triplet>> Entry : map.entrySet()) {
				SortedSet<Triplet> tripletsSet = Entry.getValue();
				for(Triplet t : tripletsSet){
					if (t.getSubject().equals(subject)) {
						if(resultMap.containsKey(t.getObject())) {
							resultMap.put(t.getObject(), resultMap.get(t.getObject()) + 1);
						} else {
							resultMap.put(t.getObject(), 1);
						}
					}
				}
		}
		List<String> ret = new ArrayList<String>();
		String[] splited;
		for ( String key : resultMap.keySet() ) {
			if(!key.contains("dbpedia.org") || !key.contains("Category:")) continue;
			splited = key.split("/");
			key = splited[splited.length - 1];
			key = key.replaceAll( "Category:" , "" ).replaceAll("_", " ");
		    ret.add(key);
		}
		return ret;
	}
}
