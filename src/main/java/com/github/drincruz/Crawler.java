package com.github.drincruz.crawler;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import java.net.URL;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import com.github.drincruz.crawler.models.HttpLink;

/**
 * Crawler thread finds links via href attributes
 * @see HtmlCleaner
 */
public class Crawler implements Callable<HttpLink> {
    // private
    private static HttpLink siteUrl;
    private static HtmlCleaner cleaner = new HtmlCleaner();

    /**
     * Constructor
     */
    public Crawler(HttpLink url) {
        siteUrl = url;
    }

    /**
     * call() method
     * return List<HttpLink> foundUrls
     */
    public HttpLink call() {
        crawl();
        return siteUrl;
    }

    /**
     * crawl() method
     * @return void
     */
    public void crawl() {
        String uri;
        List n = null;
        List<HttpLink> uriStore = new ArrayList<HttpLink>();
        try {
            TagNode node = cleaner.clean(new URL(siteUrl.getUri()));
            n = node.getElementListByName("a",true);
            for (int i=0;i < n.size(); i++) {
                TagNode t = (TagNode) n.get(i);
                uri = t.getAttributeByName("href");
                if ( (null != uri) && (2 <= uri.trim().length()) ) {
                    HttpLink link = null;
                    if (uri.trim().startsWith("http")) {
                        link = new HttpLink(uri.trim());
                    }
                    else {
                        link = new HttpLink(siteUrl.getUri() + uri.trim());
                    }
                    link.setStatus(HttpLink.Status.DISCOVERED);
                    uriStore.add(link);
                }
            }
            siteUrl.setLinksList(uriStore);
        }
        catch (IOException e) {
            System.err.printf("IOException: %s\n", e.getMessage());
        }
    }
}
