package h4202.module2;

public class Triplet implements Comparable<Triplet>{
	private String predicate;
	private String subject;
	private String object;
	
	public Triplet(String subject, String predicate, String object){
		this.subject = subject;
		this.object = object;
		this.predicate = predicate;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getSubject() {
		return subject;
	}

	public String getObject() {
		return object;
	}

	@Override
	public int compareTo(Triplet o) {
		String text=this.getSubject()+this.getPredicate()+this.getObject();
		return text.compareTo(o.getSubject()+o.getPredicate()+o.getObject());
	}
	
	
	
}