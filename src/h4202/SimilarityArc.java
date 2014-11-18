/**
 * 
 */
package h4202;

/**
 * @author Felipe
 *
 */
public class SimilarityArc {
	
	private String firstURL;
	private String secondURL;
	private Double similarityIndex;
	
	
	public SimilarityArc(String firstURL, String secondURL,
			Double similarityIndex) {
		this.firstURL = firstURL;
		this.secondURL = secondURL;
		this.similarityIndex = similarityIndex;
	}


	public String getFirstURL() {
		return firstURL;
	}


	public String getSecondURL() {
		return secondURL;
	}


	public Double getSimilarityIndex() {
		return similarityIndex;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Premiere URL: "+firstURL+"\nDeuxieme URL: "+secondURL+"\nSimilarité: "+similarityIndex;
	}
	
	
	

}
