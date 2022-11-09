package com.xelda.searchengine;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class SearchEngine {

    private Pattern pathVariablePattern1;

    private static String RANDOM_WIKI_URL = "https://en.wikipedia.org/wiki/Special:Random";

    public SearchEngine() {

    }

    public void setPathVariablePattern1(String searchQuery) {
        pathVariablePattern1 = Pattern.compile(searchQuery);
    }

    public void printRelevantArticles(Map<Integer, URL> mostRelevantArticles) {
        System.out.println("Most relevant articles found:");
        TreeSet<Integer> descendingSet = new TreeSet<>(mostRelevantArticles.keySet());
        for(Integer wordCount : descendingSet.descendingSet()) {
            System.out.println("Article name: " + mostRelevantArticles.get(wordCount) + "   Search query count: " + wordCount);
        }
    }

    public Map<Integer, URL> findMostRelevantArticles(Map<URL, String> parsedWikiArticles, String searchQuery) {

        System.out.println("Finding the most relevant articles for search query: " + searchQuery);

        SortedMap<Integer, URL> sortedArticlesMap = new TreeMap<>();

        for(URL articleUrl : parsedWikiArticles.keySet()) {
            String articleText = parsedWikiArticles.get(articleUrl);
            var matcher = pathVariablePattern1.matcher(articleText.toLowerCase(Locale.ROOT));

            int count = 0;
            while(matcher.find()) {
                count += 1;
            }
            if(count > 0) {
                sortedArticlesMap.put(count, articleUrl);
            }
        }

        return sortedArticlesMap;
    }

    public Map<URL, String> downloadWikiArticles() throws IOException, InterruptedException {

        Map<URL, String> parsedWikiArticles = new HashMap<>();

        System.out.print("Downloading... 0%\r");
        // Download 200 random wikipedia articles and save
        for (int i = 1; i <= 200; i++) {
            HttpResponse<String> randomWikiArticleResponse = getNewWikiArticle();
            parsedWikiArticles.put(randomWikiArticleResponse.uri().toURL(), Jsoup.parse(randomWikiArticleResponse.body()).text());
            System.out.print("Downloading... " + i/2 + "%\r");
        }

        return parsedWikiArticles;
    }

    public HttpResponse<String> getNewWikiArticle() throws IOException, InterruptedException {
        // Retrieve randomWikiPage from wiki api
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(RANDOM_WIKI_URL))
                .build();
        return simpleHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpClient simpleHttpClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

}
