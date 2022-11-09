package com.xelda.searchengine;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class SearchEngineApplication {

    private static Map<URL, String> parsedWikiArticles;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Welcome to Wikipedia Search Engine!");
        System.out.println("Please wait 2 minutes while we download 200 random articles from Wikipedia...");

        // Download 200 random wikipedia articles
        SearchEngine searchEngine = new SearchEngine();
        parsedWikiArticles = searchEngine.downloadWikiArticles();
        System.out.println(parsedWikiArticles.size() + " articles successfully downloaded.");


        // Prompt user for search query
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a search query: ");
        String searchQuery = scanner.nextLine();
        searchEngine.setPathVariablePattern1(searchQuery);
        System.out.println();

        // Find the most relevant articles for search query
        Map<Integer, URL> mostRelevantArticles = searchEngine.findMostRelevantArticles(parsedWikiArticles, searchQuery);

        // Print most relevant articles
        searchEngine.printRelevantArticles(mostRelevantArticles);
        System.out.println();
        System.out.println();

        // Repeat process until user quits program by entering 'quit search engine'
        System.out.println("Whenever you want to quit the search engine application, enter the following search query: 'quit search engine'");
        while(true) {
            System.out.print("Please enter a search query: ");
            searchQuery = scanner.nextLine();
            if(searchQuery.equals("quit search engine")) {
                break;
            }
            searchEngine.setPathVariablePattern1(searchQuery);
            System.out.println();
            // Find the most relevant articles
            mostRelevantArticles = searchEngine.findMostRelevantArticles(parsedWikiArticles, searchQuery);

            // Print most relevant articles
            searchEngine.printRelevantArticles(mostRelevantArticles);
            System.out.println();
            System.out.println();
        }

        System.out.println("Thank you for using the Wikipedia Search Engine, have a nice day!");

    }


}
