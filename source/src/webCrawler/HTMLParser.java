/**
 * HTMLParser is a class responsible for parsing HTML files.
 * It should remove all the tags, extract all links from a .html file.
 * From an input html file, it should return a list of all the links in said file,
 * and all the text in human readable format.
 * This class uses the JSoup library (jar included) to extract tags.
 * @author Mateus Lopes Teixeira, Michael Sampietro
 *
 */

package webCrawler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParser {
		
	public HTMLParser() {
		System.out.println("Instantiated an HTML parser!");
	}
	
	// This is a test class to test removing html tags 
	// and how to work with them in JSoup
	public void downloadAndParsePage(Webpage page) throws IOException {
		
		/*
		 * HOW TO OPEN A WEBPAGE THAT HAS BEEN DOWNLOADED, USING JSOUP
		 * File input = new File("./src/webCrawler/test.html");	// opens an HTML page already stored
		Document doc = Jsoup.parse(input, "utf-8", "www.test.com");	// parses a page that's been downloaded		
		*/
		
		if(page.getURL().startsWith("http://") || page.getURL().startsWith("https://")) {
			Document connect = Jsoup.connect(page.getURL()).get();
			
			// Extracts all the anchor tags that contain an href argument
			Elements links = connect.select("a[href]");
			
			// Selects all the text in the HTML, both on the head and body tags
			String pageText = connect.body().text();
					
			// Prints the tokens from the page to an output file, and the links found to another file
			printTokensToFile(pageText, page.getTokensFile());
			printLinksToFile(links, page);
		}
	}
	
	/**
	 * This method writes all the tokens extracted from an HTML file
	 * to an output file, named "websiteURL.txt"
	 * @param text: a string containing all the tokens
	 * @param websiteURL: the URL of the page, used for naming the file
	 */
	private void printTokensToFile(String text, String tokensFilename) {
		// TODO: there has to be a better way to remove "http://" from the string...
		// TODO: also do this to printLinksToFile();
		String filename = tokensFilename.substring(7);

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
	private void printLinksToFile(Elements links, Webpage page) {
		String filename = page.getLinksFile().substring(7);

		// TODO: what's the best way to close the output stream? in the finally?
		//PrintWriter output = null;
		
		try {
			PrintWriter output = new PrintWriter(filename);
			
			for(Element link : links) {					
				//String url = completeLink(link, page.getURL());
				String url = validateLink(link);
				if(!url.isEmpty())
				//System.out.println(link.attr("href").toString());
					output.println(link.attr("href").toString());
			}
			
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		} /*finally {
			output.flush();
			output.close();
		} */
		
		System.out.println("Done printing links!");
	}
	
	private String validateLink(Element link) {
		String finalLink = "";
		
		if((link.attr("href").startsWith("http://"))) {
			return link.attr("href").toString();
		}
		
		return finalLink;
	}
	
}
