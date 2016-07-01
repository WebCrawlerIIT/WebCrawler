package webCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Main class for the Web Crawler.
 * @author Mateus Lopes Teixeira, Michael Sampietro
 *
 */
public class WebCrawler {
		
	// "seeds" and "crawlFrontier" are both linked lists of Webpages
	//protected static LinkedList<Webpage> seeds = new LinkedList<Webpage>();
	//protected LinkedList<Webpage> crawlFrontier = new LinkedList<Webpage>();
	
	public static void main(String[] args) {
		
		LinkedList<Webpage> seeds = new LinkedList<Webpage>();
		LinkedList<Webpage> crawlFrontier = new LinkedList<Webpage>();
		
		HTMLParser parser = new HTMLParser();
		Webpage webpage = new Webpage();
		ImageCrawler imageCrawler = new ImageCrawler();
		
		String query = imageCrawler.getSearchQuery();
		imageCrawler.getJSONfromQuery(query);
	
		boolean resultsDirectory = new File("results").mkdir();
				
		try {
			importSeedsFromFile(seeds);
			
			while(!seeds.isEmpty()) {
				Iterator<Webpage> iterator = seeds.iterator();
				
				while(iterator.hasNext()) {
					webpage = seeds.removeFirst();
					parser.downloadAndParsePage(webpage);
					
					String linksFile = webpage.getLinksFile();
					//importLinksFromFile(seeds, webpage);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void importSeedsFromFile(LinkedList<Webpage> seeds) throws FileNotFoundException, IOException {
		Webpage page = null;
		try(BufferedReader br = new BufferedReader(new FileReader("seeds.wcl"))) {
			String line = "";
			
			while((line = br.readLine()) != null ) {
				page = new Webpage(line);
				seeds.add(page);
			}
		}
	}
	
	private static void importLinksFromFile(LinkedList<Webpage> seeds, Webpage page) throws FileNotFoundException, IOException {
		
		String filename = page.getLinksFile().substring(7);
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = "";
			
			while((line = br.readLine()) != null) {
				Webpage temp = new Webpage(line);
				seeds.add(temp);
			}
		}
	}
}
