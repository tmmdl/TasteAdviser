package webparsing;

import dbworks.DataBase;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *In this class we are parsing websites to get required data
 */

public class Parser {

    static void parseBooks() {

        int count = 0;
        String description;

        try {
            String url = "https://www.librarything.com/bookaward/501+Must-Read+Books";
            Document document = Jsoup.connect(url).timeout(10*1000).get();
            for (Element element : document.select(".odd")) {
                try {
                    String tempId = element.select("a[href]").attr("href");
                    if (tempId.contains("author")) {
                        continue;
                    }
                    String id = tempId;
                    String source = "https://www.librarything.com" + id;
                    String genre = element.select(".order").text();
                    String title = element.select("a[target=_top]").text();
                    document = Jsoup.connect(source).get();
                    String author = document.select("h2 a[href]").text();
                    url = source + "/descriptions";
                    document = Jsoup.connect(url).get();
                    String year = document.select(".headsummary .date").text();
                    Element show = document.select(".qelcontent").first();
                    if (show.hasText()) {
                        Element desc = document.select(".qelcontent > div:not(:has(.showmore))").first();
                        description = desc.text();
                    } else {
                        description = "no description available";
                    }

                    JSONObject jsonString = new JSONObject();
                    jsonString.put("title", title)
                            .put("author", author)
                            .put("year", year)
                            .put("genre", genre)
                            .put("description", description)
                            .put("source", source);

                    PGobject json = new PGobject();
                    json.setType("json");
                    json.setValue(jsonString.toString());
                    count++;
                    System.out.println(count);
                    DataBase.update("literature", json);
                }
                catch (NullPointerException npe) {
                    System.out.println(npe);
                }
                try {
                    Thread.sleep(3000);
                }
                catch (InterruptedException ie) {
                    System.out.println(ie);
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void parseMusic() {

        String type = "music";
        String url = "https://www.cs.ubc.ca/~davet/music/list/Best9.html";
        ArrayList<String> info = new ArrayList<String>();

        try {
            Document document = Jsoup.connect(url).get();
            int j = 0;
            for (Element element : document.select("tr")) {
                j++;
                if (j > 2) {
                    Elements elements = element.select("td");

                    String artist = elements.get(2).text();
                    String title = elements.get(3).text();
                    String year = elements.get(6).text();
                    String genre = elements.get(7).text();
                    System.out.println(artist + " " + title);

                    JSONObject jsonString = new JSONObject();
                    jsonString.put("title", title)
                            .put("artist", artist)
                            .put("year", year)
                            .put("genre", genre);

                    PGobject json = new PGobject();
                    json.setType("json");
                    json.setValue(jsonString.toString());
                    DataBase.update("music", json);
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void parseMovies() {

        int i = 1;
        int count = 0;
        for (; i < 21; i++) {
            String url = "http://www.imdb.com/search/title?groups=top_1000&sort=user_rating&view=simple&page=" + i + "&ref_=adv_prv";
            try {
                Document document = Jsoup.connect(url).header("Accept-Language", "en,az;q=0.9,de;q=0.8,tr;q=0.7,ru;q=0.6").get();
                for (Element element : document.select(".col-title a[href]")) {
                    String id = element.attr("href");
                    String source = "https://www.imdb.com" + id;
                    String title = element.text();
                    document = Jsoup.connect(source).get();
                    String imdb = document.select("span[itemprop~=ratingValue]").text();
                    String genre = document.select("span[itemprop~=genre]").text();
                    String description = document.select("div[itemprop~=description]").text();
                    String director = "Director:" + document.select("span[itemprop~=director]").text();
                    String year = document.select("span[id~=titleYear]").text();

                    JSONObject jsonString = new JSONObject();
                    jsonString.put("title", title)
                            .put("director", director)
                            .put("year", year)
                            .put("genre", genre)
                            .put("description", description)
                            .put("source", source)
                            .put("imdb", imdb);

                    PGobject json = new PGobject();
                    json.setType("json");
                    json.setValue(jsonString.toString());
                    count++;
                    System.out.println(count);
                    DataBase.update("film", json);
                    count++;
                    System.out.println(count);
                }
                try {
                    Thread.sleep(3000);
                }
                catch (InterruptedException ie) {
                    System.out.println(ie);
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}