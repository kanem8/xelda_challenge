package com.xelda.searchengine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.mockito.Mockito.*;

// This class runs some simple unit tests for the SearchEngineApplication
public class SearchEngineTests {

    @Spy
    private SearchEngine searchEngine;

    private static String RANDOM_WIKI_PAGE = "https://wikipage.com/random_wiki";

    @BeforeEach
    public void setup() throws IOException, URISyntaxException, InterruptedException {
        MockitoAnnotations.openMocks(this);

        // given
        URL resource = getClass().getClassLoader().getResource("AnchorSocietyWiki.html");
        String file1 = readFromFile(resource.getFile());

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.uri()).thenReturn(new URI("https://wikipage.com/random_wiki"));
        when(httpResponse.body()).thenReturn(file1);
        doReturn(httpResponse).when(searchEngine).getNewWikiArticle();
    }

    // For the sake of time and convenience I have added two unit tests into the below test class
    // Tests are basic and were written quickly as this is all Proof of Concept
    @Test
    public void testHappyPath() throws IOException, InterruptedException {

        // testDownloadWikiArticles
        // when
        Map<URL, String> savedParsedArticles = searchEngine.downloadWikiArticles();

        // then
        // Expected 1 because hashmap does not duplicate entries
        Assertions.assertEquals(1, savedParsedArticles.keySet().size());


        // testFindMostRelevantArticles
        // given
        String searchQuery = "anchor";
        searchEngine.setPathVariablePattern1(searchQuery);

        // when
        Map<Integer, URL> sortedArticlesMap = searchEngine.findMostRelevantArticles(savedParsedArticles, searchQuery);

        // then
        Assertions.assertEquals(1, sortedArticlesMap.keySet().size());

    }

    public static String readFromFile(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);

        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }

        return sb.toString();
    }


}
