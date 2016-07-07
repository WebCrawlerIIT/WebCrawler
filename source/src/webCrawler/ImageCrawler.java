package webCrawler;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * Class used from crawling images from Google Search
 * @author Mateus Lopes Teixeira, Michael Sampietro
 *
 */
public class ImageCrawler implements Runnable {
	
	private int num 			= 10;
	private int startIndex 		= 1;
	private String url 			= "https://www.googleapis.com/customsearch/v1?q=";
	private String key 			= "AIzaSyBn2GPcApdNEwdClTXepws2RKdvsPPLdFs";	// MINHA CONTA
	private String cx 			= "014112626754147301237:delvakx2lme";			// MINHA CONTA
//	private String key 			= "AIzaSyAWskVyL0vUTiMHbzuKdLR6FSUBDBZB7Q4";	// CONTA IIT
//	private String cx 			= "016812031941283514304:1yphssixquu";			// CONTA IIT

	private String searchType	= "image";
	private String fileType 	= "jpg";
	
	private int errors 			= 0;	// Number of errors that occured while downloading images
	private int results 		= 20;	// Number of results that we want to save
	private int numberOfQueries = (int) Math.ceil((double) results / (double) num); // Necessary number of queries to download the amount of images desired
	
	public int id;
	public String searchQuery;
	//private final String searchQuery;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSearchQuery(String sq) {
		this.searchQuery = sq;
	}
	public ImageCrawler() {

	}
	public ImageCrawler (int id) 
	{
		this.setId(id);
		searchQuery = getSearchQuery();
		run();
	}
		
	public void run() {
		boolean resultsDirectory = new File("results").mkdir();

		System.out.println(" Query from inside run: " + searchQuery);
		
		//String query = getSearchQuery();
		getJSONfromQuery(searchQuery);
	}
	
	/**
	 * This method gets an user input to query Google Images
	 * @return a String containing the query
	 */
	public String getSearchQuery() {
				
		String query = "";
		System.out.print("Query: ");
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			query = br.readLine().replace(" ", "+");	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return query;
	}
	
	
	/**
	 * Using the query submitted by the user, searches Google Images
	 * and returns a JSON containing the results. This JSON can be further explored
	 * by changing the "index", being 1 the 1st result in the 1st page, 11 the 1st result
	 * in the 2nd page, and so on.
	 * @param query - a String containing the user query
	 * @return a JSONObject with the result returned by Google
	 */
	public JSONObject getJSONfromQuery(String query) {
		
		String searchTerms = query.replaceAll(" ", "_");
		JSONObject resultJSON = null;
		
		try {
			resultJSON = new JSONObject();
			JSONParser parser = new JSONParser();
			
			for(int i = 0; i < numberOfQueries; i++) {
				
				String urlString = generateURL(query);
				URL url = new URL(urlString);
				//URLConnection connection = url.openConnection();
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				
				//connection.setRequestProperty("User-Agent", "WebCrawler IIT");
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			    connection.setRequestProperty("Accept", "application/json");
				connection.connect();
				
				InputStream is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				String jsonString = readAll(br);
				
				resultJSON = (JSONObject) parser.parse(jsonString);
							
				getImageAttributesFromJSON(resultJSON, startIndex, query);
				
				startIndex += 10;	// Sets the startIndex variable to the first result in the next page
			}
			
			return resultJSON;
			
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return resultJSON;
	}
	

	/**
	 * Extracts all the information about the images from the JSON returned by Google Images.
	 * @param image: an ImageAttributes instance
	 * @param json: a JSONObject instance
	 * @param startIndex: the startIndex used while querying Google Images
	 */
	public void getImageAttributesFromJSON(JSONObject json, int startIndex, String query) {
			
		ImageAttributes image = new ImageAttributes();
		image.setSearchTerms(query.replaceAll(" ", "+"));
		
		// Gets an array of results, that will be iterated on and the informations about the 
		// images will be extracted and then downloaded
		ArrayList<JSONObject> itemsArray = (ArrayList<JSONObject>) json.get("items");
		Iterator<JSONObject> iterator = itemsArray.iterator();
		int index = 0 + startIndex;
		
		// Iterates through the array of results
		while(iterator.hasNext()) {
			JSONObject item = iterator.next();
			String link = (String) item.get("link");
			String snippet = (String) item.get("snippet");
			
			image.setLink(link);
			image.setSnippet(snippet);
			
			boolean queryResultsDir = new File("../results/" + image.getSearchTerms()).mkdir();
			downloadImage(image, index);
			
			index++;
		}
	}
	
	private void downloadImage(ImageAttributes image, int index) {
		
		try {	
			int newIndex;
			int stopCondition = results - errors; // Stops the program when the necessary amount of images has been downloaded
			
			if((index - errors) == 0) 
				newIndex = startIndex + 1;
			else 
				newIndex = index - errors;
			
			if(newIndex <= stopCondition) {

				URL url = new URL(image.getLink());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				int responseCode = connection.getResponseCode();
				System.out.println("Code: " + responseCode);
				if(responseCode != 200)
					throw new IOException();
				
				System.out.println("(" + id + ")" + " IndexReal: " + index + " |newIndex: " + newIndex + " |Results: " + results);
				String filename = newIndex + ".jpg";
				String path = "../results/" + image.getSearchTerms() + "/";

				InputStream in	 = new BufferedInputStream(connection.getInputStream());
				OutputStream out = new BufferedOutputStream(new FileOutputStream(path + filename));

				int readByte;
				while ((readByte = in.read()) != -1) {
					out.write(readByte);
				}

				in.close();
				out.close();
			}
			
		} catch (UnknownHostException uhe) {
			System.out.println("===UNKNOWN HOST EXCEPTION===");
			errors++;
			results += 1;
			numberOfQueries = (int) Math.ceil((double) results / (double) num);
			//uhe.printStackTrace();
		} catch (IOException e) {
			System.out.println("===IOEXCEPTION===");
			errors++;
			results += 1;
			numberOfQueries = (int) Math.ceil((double) results / (double) num);
			System.out.println(image.getLink());
			//e.printStackTrace();
		} 
	}
	
	/**
	 * This is a private method that reads all the characters read by a Reader
	 * @param reader a Reader with the information that will be stored in a String
	 * @return	a String with all the info from the reader
	 * @throws IOException
	 */
	private static String readAll(Reader reader) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
	    int cp;

	    while ((cp = reader.read()) != -1) {
	      stringBuilder.append((char) cp);
	    }
	    return stringBuilder.toString();
	  }
	
	private String generateURL(String query) {
		
		String queryEncoded;
		
		try {
			queryEncoded = URLEncoder.encode(query, "UTF-8").replaceAll("\\%28", "(") 
					.replaceAll("\\%29", ")") 
					.replaceAll("\\+", "%20") 
					.replaceAll("\\%27", "'") 
					.replaceAll("\\%21", "!") 
					.replaceAll("\\%7E", "~");
			
			String urlString = url + queryEncoded + "&key=" + key + "&cx=" + cx 
					+ "&searchType=" + searchType  + "&start=" 
					+ startIndex + "&fileType=" + fileType;
			
			System.out.println(urlString);
			return urlString;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}

