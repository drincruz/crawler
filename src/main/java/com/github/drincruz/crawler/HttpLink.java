package com.github.drincruz.crawler.models;

import java.util.List;

/**
 * HttpLink model class
 */
public class HttpLink {
    // enum Status 
    public static enum Status {
        VISITED, UNVISITED, DISCOVERED
    }

    // private
    private String uri;
    private Status status = Status.UNVISITED;
    private List<HttpLink> links;
    
    /**
     * Constructor
     * @param String u
     */
    public HttpLink(String u) {
        uri = u;
    }

    /**
     * Gets uri
     * @return String uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Gets list of links
     * @return List<HttpLink> links
     */
    public List<HttpLink> getLinksList() {
        return links;
    }

    /**
     * Sets links list
     * @param List<HttpLink> l
     * @return void
     */
    public void setLinksList(List<HttpLink> l) {
        links = l;
    }

    /**
     * Sets the Status
     * @param Status s
     * @return void
     */
    public void setStatus(Status s) {
        status = s;
    }

    /**
     * Gets status
     * @return Status status
     */
    public Status getStatus() {
        return status;
    }
}
