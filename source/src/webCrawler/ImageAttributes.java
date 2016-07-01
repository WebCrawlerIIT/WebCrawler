package webCrawler;

/**
 * The ImageAttributes class represents the multiple attributes that
 * an image result from Google Images can contain. 
 * @author Mateus Lopes Teixeira, Michael Sampietro
 *
 */
public class ImageAttributes {
	
	private String searchTerms = "";
	private String link = "";
	private String snippet = "";
	private String contextLink = "";
	
	public String getSearchTerms() {
		return searchTerms;
	}
	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public String getContextLink() {
		return contextLink;
	}
	public void setContextLink(String contextLink) {
		this.contextLink = contextLink;
	}

}
