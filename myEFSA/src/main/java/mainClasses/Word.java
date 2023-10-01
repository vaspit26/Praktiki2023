package mainClasses;

public class Word {

	private String content;
	private String uri;
	private String resource; /*where did I find it*/
	private boolean found;
	
	public Word(String content, String uri, String resource, boolean found) {
		
		this.content = content;
		this.uri = uri;
		this.resource = resource;
		this.found = found;
	}
	
	public Word(String content, boolean found) {
		
		this.content = content;
		this.uri = null;
		this.resource = null;
		this.found = found;
	}
	
	public Word(Word w) {
		this.content = w.getContent();
		this.uri = w.getUri();
		this.resource = w.getResource();
		this.found = w.getFound();
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public String getResource() {
		return this.resource;
	}
	
	public void setFound(boolean found) {
		this.found = found;
	}
	
	public boolean getFound() {
		return this.found;
	}
	
	public void PrintWord() {
		System.out.println("---Printing word---");
		System.out.println(this.content);
		System.out.println(this.uri);
		System.out.println(this.resource);
		System.out.println(this.found);
		System.out.println("--- ---");
	}
	
}
