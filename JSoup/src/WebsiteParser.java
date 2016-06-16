import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebsiteParser {
	
	public static void main(String args[]) throws IOException {
		Document doc;
		doc = Jsoup.connect("http://stackoverflow.com/questions/14467459/403-error-while-getting-the-google-result-using-jsoup").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
		Elements links = doc.getElementsByTag("a");
		for (Element link:links){
			String l = link.attr("href");
			if (l.length()>0)
				l = l.substring(0);
		System.out.println(l);
		}
	}
}
