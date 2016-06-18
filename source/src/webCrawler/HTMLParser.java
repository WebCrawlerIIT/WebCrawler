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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

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
		File input = new File("./src/webCrawler/test.html");	// opens an HTML page already stored
		
		Document doc = Jsoup.parse(input, "utf-8", "www.test.com");	// parses a page that's been downloaded
		//Document connect = Jsoup.connect("http://www.ccel.org/ccel/bible/kjv.txt").get(); // download and then parses a page
		
		// Extracts all the anchor tags that contain an href argument
		Elements links = doc.select("a[href]");
		
		// Selects all the text in the HTML, both on the head and body tags
		String text = doc.body().text();
		
		// Prints the tokens from the page to an output file, and the links found to another file
		printToFile(text, "test.com");
		printLinksToFile(links, "test.com");
	}
	
	/**
	 * This method writes all the tokens extracted from an HTML file
	 * to an output file, named "websiteURL.txt"
	 * @param text: a string containing all the tokens
	 * @param websiteURL: the URL of the page, used for naming the file
	 */
	private void printToFile(String text, String websiteURL) {
		String filename = websiteURL + ".txt";
		
		System.out.println("Printing tokens to " + filename + "...");
		try {
			PrintWriter output = new PrintWriter(filename);
			String[] splitted = text.split("\\s+");	// Splits the token string to a list of tokens
			
			for(int i = 0; i < splitted.length; i++)
				output.println(splitted[i]);
			
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Done printing tokens!");
	}
	
	/**
	 * This method writes all the links extracted from an HTML page
	 * to an output file, named "websiteURL_links.txt"
	 * @param links: a list of Elements type containing all the links, each is an Element
	 * @param websiteURL: the URL of the page
	 */
	private void printLinksToFile(Elements links, String websiteURL) {
		String filename = websiteURL + "_links.txt";
		
		System.out.println("Printing links to " + filename + "...");
		try {
			PrintWriter output = new PrintWriter(filename);
			
			for(Element link : links) {
				//output.println(link.text()); 	// prints what's written between the anchor tags
				output.println(link.attr("href"));
			}
			
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
		System.out.println("Done printing links!");
	}
	
}
