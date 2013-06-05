package com.github.drincruz.crawler;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import com.github.drincruz.crawler.models.HttpLink;

/**
 * From a starting link finds links and crawls them
 * @author Adrian Cruz
 * @version 0.1-alpha
 */
public class Crawl {
    /**
     * Main
     */
    public static void main(String args[]) throws Exception {
        HttpLink siteUrl = new HttpLink(args[0]);
        List<HttpLink> linkQueue = new ArrayList();
        System.out.printf("site: %s\n", siteUrl.getUri());
        HttpLink processedLink = null;
        ExecutorService pool = Executors.newFixedThreadPool(16);
        linkQueue.add(siteUrl);
        HashMap<String, HttpLink> spiderIndex = new HashMap<String, HttpLink>();
        spiderIndex.put(siteUrl.getUri(), siteUrl);
        while (!linkQueue.isEmpty()) {
            HttpLink next = linkQueue.remove(0);
            if (spiderIndex.get(next.getUri()).getStatus() == HttpLink.Status.VISITED) {
                continue;
            }
            spiderIndex.get(next.getUri()).setStatus(HttpLink.Status.VISITED);
            System.err.printf("Processing...%s\n", next.getUri());
            Callable<HttpLink> callable = new Crawler(next);
            Future<HttpLink> future = pool.submit(callable);
            try {
                processedLink = future.get();
                if (!spiderIndex.containsKey(processedLink.getUri())) {
                    spiderIndex.put(processedLink.getUri(), processedLink);
                }
                for (HttpLink l: processedLink.getLinksList()) {
                    if (!spiderIndex.containsKey(l.getUri())) {
                        spiderIndex.put(l.getUri(), l);
                        linkQueue.add(l);
                    }
                }
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
