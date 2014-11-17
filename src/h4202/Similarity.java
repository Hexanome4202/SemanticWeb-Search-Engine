package h4202;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Similarity {
	private static ArrayList<SortedSet<String>> listFiles = new ArrayList<SortedSet<String>>();

	public static void main(String[] args) {
		readAll();

	}

	private static SortedSet<String> readFile(String path) throws IOException {
		FileInputStream fis = new FileInputStream(path);

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		SortedSet<String> set = new TreeSet<String>();
		while ((line = br.readLine()) != null) {
			set.add(line);
			System.out.println(line);
		}
		br.close();
		return set;
	}

	public static void readAll() {

		File folder = new File(".");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".txt")) {
				try {
					listFiles.add(readFile(file.getCanonicalPath()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.addAll(setB);
		return tmp;
	}

	public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>();
		for (T x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}
	
	public static <T> double similarity(Set<T> setA, Set<T> setB){
		
		
		return 0;
	}

}
