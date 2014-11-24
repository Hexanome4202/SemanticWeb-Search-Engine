package h4202.model;

public class resultModel {
	
	private String label;
	private String imgURL;
	private String description;
	
	
	public resultModel(String label, String imgURL, String description) {
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
	
}
