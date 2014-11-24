package h4202.controller;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

import org.json.simple.JSONObject;

import h4202.GoogleResults;
import h4202.module2.Entities;
import h4202.module2.Triplet;
import h4202.module2.Entities.Pair;

public class ThreadSearch implements Runnable {
	private JSONObject jsonObject;
	private Semaphore semaphoreFin;
	private Semaphore hashMapSemaphore;
	private HashMap<String, SortedSet<Triplet>> url_triplets;
	
	public ThreadSearch(JSONObject jsonObject, HashMap<String, SortedSet<Triplet>> url_triplets, 
			Semaphore semaphoreFin, Semaphore hashMapSemaphore) {
		this.jsonObject = jsonObject;
		this.semaphoreFin = semaphoreFin;
		this.hashMapSemaphore = hashMapSemaphore;
		this.url_triplets = url_triplets;
	}

	@Override
	public void run() {
		String link;
		String text;
		String snippet;
		JSONObject doublet;
		
		link = this.jsonObject.get("link").toString();
		link = link.replace("\\/", "/");
		if(!link.contains(".pdf") && !this.url_triplets.containsKey(link)) {
			text = GoogleResults.getTextFromPage(link).replace("\\/", "/").replaceAll("\\s+", " ").replaceAll("\\\"", "").replaceAll("\\[.*?\\]", "");
			snippet = this.jsonObject.get("snippet").toString();
			if(text != "") {
				doublet = new JSONObject();
				doublet.put("link", link);
				doublet.put("text", text + snippet);
				
				Pair graph = Entities.getIndividualGraph(doublet);
				
				try {
					hashMapSemaphore.acquire();
					url_triplets.put(graph.getUrl(),graph.getTriplets());
					hashMapSemaphore.release();
				} catch (InterruptedException e) {
					// TODO: do something
				}
			}
		}
		semaphoreFin.release();
	}

}
