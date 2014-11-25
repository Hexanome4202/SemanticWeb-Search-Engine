package h4202.model;

import java.io.Serializable;

import h4202.module2.Triplet;

public class ResultModel  implements Serializable, Comparable<ResultModel>{
	
	private String label;
	private String imgURL;
	private String description;
	private String wikipediaLink;
	private String homePageLink;
	
	
	public ResultModel(String label, String imgURL, String description, String wikipediaLink, String homePage) {
		this.label = label;
		this.imgURL = imgURL;
		this.description = description;
		this.wikipediaLink=wikipediaLink;
		this.homePageLink=homePage;
		
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
	
	


	public String getWikipediaLink() {
		return wikipediaLink;
	}

	

	public String getHomePageLink() {
		return homePageLink;
	}


	@Override
	public String toString() {
		return "ResultModel [label=" + label + ", imgURL=" + imgURL
				+ ", description=" + description + "]";
	}


	@Override
	public int compareTo(ResultModel o) {
		String text=this.getLabel()+this.getImgURL()+this.getDescription();
		return text.compareTo(o.getLabel()+o.getImgURL()+o.getDescription());
	}
	
}
