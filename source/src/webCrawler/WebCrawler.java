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
		
		// The number of available cores for processing will determine how many queries will run 
		// at the same time
		int availableCores = Runtime.getRuntime().availableProcessors();
		System.out.println("Number of available cores: " + availableCores);
		
		// Definition of the imageCrawlers and Threads that will parallelize the search.
		// Once again, the amount of crawlers and threads will be defined by the available cores
		ImageCrawler[] imageCrawlers = new ImageCrawler[availableCores];
		Thread[] threads = new Thread[availableCores];
		
		// Instatiation of each image crawler and each thread, as well as linking them both
		for(int i = 0; i < availableCores; i++) {			
			imageCrawlers[i] = new ImageCrawler();
			imageCrawlers[i].setId(i);
			String query = imageCrawlers[i].getSearchQuery();
			imageCrawlers[i].setSearchQuery(query);
			
			threads[i] = new Thread(imageCrawlers[i]);
		}
		
		// Starts all the threads so they run in parallel
		for(int i = 0; i < availableCores; i++) 
			threads[i].start();
		
			
		//boolean resultsDirectory = new File("results").mkdir();
				
//		try {
//			importSeedsFromFile(seeds);
//			
//			while(!seeds.isEmpty()) {
//				Iterator<Webpage> iterator = seeds.iterator();
//				
//				while(iterator.hasNext()) {
//					webpage = seeds.removeFirst();
//					parser.downloadAndParsePage(webpage);
//					
//					String linksFile = webpage.getLinksFile();
//					//importLinksFromFile(seeds, webpage);
//				}
//			}
//				
//			} catch (IOException e) {
//			e.printStackTrace();
//		}
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
