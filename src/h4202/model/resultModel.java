package h4202.model;

import java.io.Serializable;

import h4202.module2.Triplet;

public class ResultModel  implements Serializable, Comparable<ResultModel>{
	
	private String label;
	private String imgURL;
	private String description;
	
	
	public ResultModel(String label, String imgURL, String description) {
		this.label = label;
		this.imgURL = imgURL;
		this.description = description;
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
