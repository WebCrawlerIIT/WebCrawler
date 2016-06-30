package webCrawler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Class used from crawling images from Google Search
 * @author Mateus Lopes Teixeira, Michael Sampietro
 *
 */
public class ImageCrawler {
	
	private String url = "https://www.googleapis.com/customsearch/v1?q=";
	private String key = "AIzaSyBn2GPcApdNEwdClTXepws2RKdvsPPLdFs";
	private String cx = "014112626754147301237:delvakx2lme";
	private String searchType = "image";
	private int num = 10;
	
	public ImageCrawler() {
		
	}
	
	/**
	 * This method gets an user input to query Google Images
	 * @return a String containing the query
	 */
	public String getSearchQuery() {
		String query = "";
		System.out.println("Query: ");
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			query = br.readLine().replace(" ", "+");	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return query;
	}
	
	
	// TODO: Check that the result of the query was 200, and not something else
	/**
	 * Using the query submitted by the user, searches Google Images
	 * and returns a JSON containing the results. This JSON can be further explored
	 * by changing the "index", being 1 the 1st result in the 1st page, 11 the 1st result
	 * in the 2nd page, and so further.
	 * @param query - a String containing the user query
	 * @return a JSONObject with the result returned by Google
	 */
	public JSONObject getJSONfromQuery(String query) {
		
		JSONObject resultJSON = null;
		try {
			resultJSON = new JSONObject();
			JSONParser parser = new JSONParser();
			
			String urlString = url + query + "&key=" + key + "&cx=" + cx + "&searchType=" + searchType;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			
			connection.setRequestProperty("User-Agent", "WebCrawler IIT");
			connection.connect();
			
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonString = readAll(br);
			
			resultJSON = (JSONObject) parser.parse(jsonString);
						
//			ArrayList link1 = ((ArrayList) resultJSON.get("items"));
//			int size = link1.size();
//			System.out.println("size: " + size);
			
			ImageAttributes img = new ImageAttributes();
			getImageAttributes(img, resultJSON);
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
	
	public void getImageAttributes(ImageAttributes image, JSONObject json) {
		
		ArrayList<JSONObject> itemsArray = (ArrayList<JSONObject>) json.get("items");
		Iterator<JSONObject> iterator = itemsArray.iterator();
		
		while(iterator.hasNext()) {
			JSONObject item = iterator.next();
			String link = (String) item.get("link");
			System.out.println("Link: " + link);
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
}
