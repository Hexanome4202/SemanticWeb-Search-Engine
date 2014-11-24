package h4202.controller;

import h4202.module2.Triplet;

import java.util.Map;
import java.util.SortedSet;

public class BeaverBeverGo {
	
	public static final String IMAGE= "http://dbpedia.org/ontology/thumbnail";
	public static final String LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	public static final String ABSTRACT = "http://dbpedia.org/ontology/abstract";
	
	
	
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
					//System.out.println(t.toString());
				}
		}
		return "";
	}
}
