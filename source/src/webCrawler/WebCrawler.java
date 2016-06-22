package webCrawler;

import java.lang.reflect.Array;

/**
 * Main class for the Web Crawler.
 * @author Mateus Lopes, Michael Sampietro
 *
 */
public class WebCrawler {
	
	// These lists define the 'seeds' and the 'crawl frontier' lists of pages.
	private Webpage[] seeds = new Webpage[10];
	private Webpage[] crawlFrontier = new Webpage[100];
	
	public static void main(String[] args) {
		
		HTMLParser parser = new HTMLParser();
		Webpage webpage = new Webpage();
		Webpage cnn = new Webpage("http://cnn.com");

	}
}
