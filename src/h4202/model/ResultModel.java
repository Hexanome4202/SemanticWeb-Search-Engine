package h4202.model;

import java.io.Serializable;
import java.util.List;

public class ResultModel  implements Serializable, Comparable<ResultModel>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private String imgURL;
	private String description;
	private String wikipediaLink;
	private String homePageLink;
	private String url;
	private double similarityAverage;
	private List<String> categories;
	
	
	public ResultModel(String label, String imgURL, String description, String wikipediaLink, String homePage, String url, double similarityAverage, List<String> categories) {
		this.label = label;
		this.imgURL = imgURL;
		this.description = description;
		this.wikipediaLink=wikipediaLink;
		this.homePageLink=homePage;
		this.url=url;
		this.similarityAverage=similarityAverage;
		this.categories = categories;
	}


	public String getLabel() {
		return label;
	}


	public String getImgURL() {
		return imgURL;
	}


	public String getDescription() {
		return description;
	}
	
	public List<String> getCategories() {
		return this.categories;
	}


	public String getWikipediaLink() {
		return wikipediaLink;
	}

	

	public String getHomePageLink() {
		return homePageLink;
	}

	


	public String getUrl() {
		return url;
	}


	public double getSimilarityAverage() {
		return similarityAverage;
	}


	@Override
	public String toString() {
		return "ResultModel [label=" + label + ", imgURL=" + imgURL
				+ ", description=" + description + ", wikipediaLink="
				+ wikipediaLink + ", homePageLink=" + homePageLink + ", url="
				+ url + ", similarityAverage=" + similarityAverage + "]";
	}


	@Override
	public int compareTo(ResultModel o) {
		String text=this.getLabel()+this.getImgURL()+this.getDescription();
		return text.compareTo(o.getLabel()+o.getImgURL()+o.getDescription());
	}
	
	
	
}
