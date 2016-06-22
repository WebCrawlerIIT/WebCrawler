package webCrawler;

import java.util.Date;

/**
 * This class is a representation of a webpage.
 * It contains many informations about it, such as: the webpage's URL,
 * if it has been visited yet, the time of the last visit, etc;
 * @author Mateus Lopes Teixeira
 *
 */
public class Webpage {
	
	private String URL = "";
	private String tokensFile = "";
	private String linksFile = "";
	private boolean visited = false;
	private Date lastVisitDate = new Date();
	
	public Webpage() {
		System.out.println("Created a Webpage instance with no URL!");
	}
	public Webpage(String url) {
		System.out.println("Creating a Webpage instance for " + url);
		setURL(url);
		setTokensFile(url);
		setLinksFile(url);
	}
	
	// Getters and Setters for the attributes
	public String getURL() {
		return URL;
	}
	public void setURL(String url) {
		this.URL = url;
	}
	
	public String getTokensFile() {
		return tokensFile;
	}
	public void setTokensFile(String url) {
		this.tokensFile = url + "_tokens.txt";
	}
	
	public String getLinksFile() {
		return linksFile;
	}
	public void setLinksFile(String url) {
		this.linksFile = url + "_links.txt";
	}
	
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public Date getLastVisitDate() {
		return lastVisitDate;
	}
	public void setLastVisitDate(Date date) {
		this.lastVisitDate = date;
	}
	

}
