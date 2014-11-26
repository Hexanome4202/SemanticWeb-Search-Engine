package h4202;


import h4202.controller.BeaverBeverGo;
import h4202.module2.Triplet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Similarity {

	/**
	 * A map with the URL of the page as key and a set with the triplets
	 */
	private Map<String, SortedSet<Triplet>> mapFiles;
	/**
	 * An array list with all the arcs between the different URLs (similarity
	 * between the two pages as arc value)
	 */
	private ArrayList<SimilarityArc> similarityList = new ArrayList<SimilarityArc>();

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Similarity sim = new Similarity();
		BeaverBeverGo bv = new BeaverBeverGo();
		sim.readAll();
		sim.fillSimilarityList();

		for (SimilarityArc a : sim.getSimilarityList()) {

			//System.out.println(a);
		}
//		String imageURL=bv.searchForPredicate(sim.getMapFiles(), BeaverBeverGo.IMAGE);
//		String label=bv.searchForPredicate(sim.getMapFiles(), BeaverBeverGo.LABEL);
//		String desc=bv.searchForPredicate(sim.getMapFiles(), BeaverBeverGo.ABSTRACT);
//		System.out.println(imageURL);
//		System.out.println(label);
//		System.out.println(desc);
		
		sim.createGraphViz("teste.graphviz");

	}


	public Similarity(HashMap<String, SortedSet<Triplet>> hashMap) {
		this.mapFiles = hashMap;
	}

	public Similarity() {
		 mapFiles = new HashMap<String, SortedSet<Triplet>>();
	}


	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	private void readFile(String path) throws IOException {

		FileInputStream fis = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis,
				"UTF8")); // Pas de probleme avec les ???, les strings sont bien
							// formees

		String line = null;
		SortedSet<Triplet> set = new TreeSet<Triplet>();
		int i = 0;
		String url = "";

		while ((line = br.readLine()) != null) {
			if (i == 0) {
				url = line;
			} else {
				String[] triplet = line.split("\\s+");
				set.add(new Triplet(triplet[0], triplet[1] , triplet[2]));
			}
			i++;
		}
		br.close();
		mapFiles.put(url, set);
	}

	/**
	 * Calls the readFile method for every text file in the current folder
	 */
	public void readAll() {

		File folder = new File(".");//faire attention au chemin pour la partie web
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".txt")) {
				try {
					readFile(file.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param setA
	 * @param setB
	 * @return
	 */
	public <T> Set<T> union(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.addAll(setB);
		return tmp;
	}

	/**
	 * 
	 * @param setA the first set
	 * @param setB the second set
	 * @return the intersection of the two set
	 */
	public <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>();
		for (T x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}

	/**
	 * 
	 * @param setA the first set
	 * @param setB the second set
	 * @return the union of the two sets
	 */
	public <T> Double similarityCalcul(Set<T> setA, Set<T> setB) {
		double i = intersection(setA, setB).size();
		double u = union(setA, setB).size();
		Double d = i / u;
		return d;
	}

	/**
	 * 
	 */
	public void fillSimilarityList() {

		int i = 0;
		for (Map.Entry<String, SortedSet<Triplet>> FirstEntry : mapFiles
				.entrySet()) {

			
			String firstURL;
			SortedSet<Triplet> pagesFirst;
				firstURL = FirstEntry.getKey();
				pagesFirst = FirstEntry.getValue();
				
			int j=0;
			for (Map.Entry<String, SortedSet<Triplet>> SecondEntry : mapFiles
					.entrySet()) {
				
				if (j!=0 && j>i) {
					String secondURL = SecondEntry.getKey();
					SortedSet<Triplet> pagesSecond = SecondEntry.getValue();
					Double simIndex = similarityCalcul(pagesFirst, pagesSecond);
					
					if (simIndex>=0.00) { //seuil
						similarityList.add(new SimilarityArc(firstURL,
								secondURL, simIndex));
						//System.out.println(firstURL+"  " + secondURL + "  " + simIndex);
					}
				}
				j++;
			}
			i++;
		}
	}
	
	public void createGraphViz(String filename){
		try {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			int i = 0;
			String one = "", two = "";
			File file = new File(filename);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("digraph sample {");
			output.newLine();
			for(SimilarityArc arc : similarityList){
				one = arc.getFirstURL();
				if(!map.containsKey(one)) {
					map.put(one, i);
					++i;
				}
				
				two = arc.getSecondURL();
				if(!map.containsKey(two)) {
					map.put(two, i);
					++i;
				}
				
				output.write(map.get(one)+" -> "+ map.get(two) +" [ weight = "+ arc.getSimilarityIndex() + "];");
				output.newLine();
			}
			output.write("}");
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
	}
	
	public String createGraphViz(HashMap<String, Integer> map){
		String graph = "";
		int i = 0, j = 0;
		double valMax = 0;
		String one = "", two = "";
		String nodes = "var nodes = [";
		String edges = "var edges = [";
		String urlMax = "";
		
		List<String> urls = new ArrayList<String>();
		urls.addAll(this.mapFiles.keySet());
		
		double[] vals = new double[this.mapFiles.keySet().size()];
		for(i = 0; i < vals.length; ++i) vals[i] = 0;
		i=0;
		for(String elem : urls) {
			for(SimilarityArc arc : similarityList){
				if(arc.getFirstURL().equals(elem) )
					vals[i] += arc.getSimilarityIndex();
			}
			++i;
		}
		
		for(i = 0; i < vals.length; ++i) {
			if(vals[i] > valMax) {
				valMax = vals[i];
				urlMax = urls.get(i);
			}
		}
		
		i = 0;
		String extra = ", group: 'max'";
		for(SimilarityArc arc : similarityList){
			if(arc.getSimilarityIndex() < 0.01) continue;
			one = arc.getFirstURL();
			if(!map.containsKey(one)) {
				map.put(one, i);
				if(i != 0) nodes += ",";
				nodes += "{ id: " + i + ", label: '" + i + "'";
				if(one.equals(urlMax)) nodes += extra;
				nodes += "}";
				++i;
			}
			
			two = arc.getSecondURL();
			if(!map.containsKey(two)) {
				map.put(two, i);
				if(i != 0) nodes += ",";
				nodes += "{ id: " + i + ", label: '" + i + "'";
				if(two.equals(urlMax)) nodes += extra;
				nodes += "}";
				++i;
			}
			
			if(j != 0) edges += ",";
			String index = arc.getSimilarityIndex().toString();
			index = index.substring(0,5);
			edges += "{from: " + map.get(one) + ", to: " + map.get(two) +", label:"+ index + " }";
			++j;
		}
		graph = "<script>" + nodes + "];" + edges + "]; var container=document.getElementById('mynetwork'),data={nodes:nodes,edges:edges},options={width:'800px',height:'400px',groups:{max:{color:'red'}}},network=new vis.Network(container,data,options);</script>";
		return graph;
	}
	
	public double similatiryAverage(String url){

		double sum=0;
		double size=0;
		for(SimilarityArc arc : similarityList){
			
			if(arc.getFirstURL()==url || arc.getSecondURL()==url){
				sum+=arc.getSimilarityIndex();
				size++;
			}
			
		}
		
		return sum/size;
	}

	public Map<String, SortedSet<Triplet>> getMapFiles() {
		return mapFiles;
	}

	public ArrayList<SimilarityArc> getSimilarityList() {
		return similarityList;
	}

}
