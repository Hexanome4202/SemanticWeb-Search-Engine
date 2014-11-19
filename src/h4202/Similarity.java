package h4202;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Similarity {

	/**
	 * A map with the URL of the page as key and a set with the ??? as value
	 */
	private Map<String, SortedSet<String>> mapFiles = new HashMap<String, SortedSet<String>>();
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
		sim.readAll();
		sim.fillSimilarityList();

		for (SimilarityArc a : sim.getSimilarityList()) {

			System.out.println(a);
		}

	}

	/**
	 * Default constructor
	 */
	public Similarity() {

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
		SortedSet<String> set = new TreeSet<String>();
		int i = 0;
		String url = "";

		while ((line = br.readLine()) != null) {
			if (i == 0) {
				url = line;
			} else {
				set.add(line);
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

		File folder = new File(".");
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
		for (Map.Entry<String, SortedSet<String>> FirstEntry : mapFiles
				.entrySet()) {

			
			String firstURL;
			SortedSet<String> pagesFirst;
				firstURL = FirstEntry.getKey();
				pagesFirst = FirstEntry.getValue();
				
			int j=0;
			for (Map.Entry<String, SortedSet<String>> SecondEntry : mapFiles
					.entrySet()) {
//				il faut encore voir le seuil
				if (j!=0 && j>i) {
					String secondURL = SecondEntry.getKey();
					SortedSet<String> pagesSecond = SecondEntry.getValue();
					similarityList.add(new SimilarityArc(firstURL, secondURL,
							similarityCalcul(pagesFirst, pagesSecond)));
				}
				j++;
			}
			i++;
		}
	}

	public Map<String, SortedSet<String>> getMapFiles() {
		return mapFiles;
	}

	public ArrayList<SimilarityArc> getSimilarityList() {
		return similarityList;
	}

}
