/**
 * HTMLParser is a class responsible for parsing HTML files.
 * It should remove all the tags, extract all links from a .html file.
 * From an input html file, it should return a list of all the links in said file,
 * and all the text in human readable format.
 * This class uses the JSoup library (jar included) to extract tags.
 * @author Mateus Lopes
 *
 */

package webCrawler;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParser {
		
	public HTMLParser() {
		try {
			removeHtmlTagsTEST();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// This is a test class to test removing html tags 
	// and how to work with them in JSoup
	private void removeHtmlTagsTEST() throws IOException {
		File input = new File("./src/webCrawler/test.html");
		Document doc = Jsoup.parse(input, "utf-8", "www.test.com");
		
		Elements paragraphs, links;
		paragraphs = doc.getElementsByTag("p");
		links = doc.getElementsByTag("a");
		
		System.out.println("Printing all the links found on " + input.getName() + ":");
		for(Element link : links) {
			String linkHref = link.attr("href");
			String linkText = link.text();
			System.out.println(linkText + ": " + linkHref);
		}
	}
	
}
