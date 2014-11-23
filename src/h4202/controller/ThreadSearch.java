package h4202.controller;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.json.simple.JSONObject;

import h4202.GoogleResults;
import h4202.model.GoogleElement;

public class ThreadSearch implements Runnable {
	private JSONObject jsonObject;
	private GoogleElement[] elements;
	private int i;
	private Semaphore semaphore;
	
	public ThreadSearch(JSONObject jsonObject, GoogleElement[] elements, int i, Semaphore semaphore) {
		this.jsonObject = jsonObject;
		this.elements = elements;
		this.i = i;
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		System.out.println("début thread");
		String link;
		String text;
		String snippet;
		
		link = this.jsonObject.get("link").toString();
		link = link.replace("\\/", "/");
		if(!link.contains(".pdf")) {
			text = GoogleResults.getTextFromPage(link).replace("\\/", "/").replaceAll("\\s+", " ").replaceAll("\\\"", "").replaceAll("\\[.*?\\]", "");
			snippet = this.jsonObject.get("snippet").toString();
			if(text != "")
				elements[i] = new GoogleElement(link, text + snippet);
		}
		semaphore.release();
		System.out.println("fin thread");
	}

}
