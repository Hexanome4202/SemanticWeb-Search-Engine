package h4202;

public class GoogleElement {
	private String link;
	private String text;
	
	// Constructor(s)
	public GoogleElement(String link, String text) {
		this.link = link;
		this.text = text;
	}

	// Getter(s)
	public String getLink() {
		return link;
	}

	public String getText() {
		return text;
	}
	
	// Method(s)
	@Override
	public String toString() {
		return this.link + " " + this.text;
	}
}
