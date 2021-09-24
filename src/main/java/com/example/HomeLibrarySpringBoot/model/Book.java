package com.example.HomeLibrarySpringBoot.model;


import com.example.HomeLibrarySpringBoot.repository.AuthorRepository;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "_isbn")
    private String isbn;
    @Transient
    private static StringBuilder urlBuild = new StringBuilder("https://data.bn.org.pl/api/bibs.json?isbnIssn=");
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "publishedyear")
    private String publishedYear;
    @Column(name = "language")
    private String language;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @ManyToOne
//    @JoinColumn(name = "authorid", insertable = false, updatable = false)
//    private Author authorEntity;  //entity needed to get authorid out of authors table
//    @Column(name ="authorid")
//    private int authorId; //=authorEntity.getId();
    @Transient
    @Autowired
    private AuthorRepository authorRepository;
    @Column(name = "saga")
    private String saga;
    @Column(name = "publishingseries")
    private String publishingSeries;


        @Autowired
        public Book(){

        }


    public Book scrapeBookByIsbn(String isbn) {
        this.isbn =isbn;
        urlBuild.append(isbn);
        try {
            URL url = new URL(urlBuild.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent","mozilla/17.0");
            connection.connect();
            int responseCode = connection.getResponseCode();
            String response="";
            if (responseCode==HttpURLConnection.HTTP_OK){
                Scanner scanner= new Scanner(url.openStream());
                while (scanner.hasNext()){
                    response+=scanner.nextLine();
                }
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(response);
                JSONArray jsonArray = (JSONArray) obj.get("bibs");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                this.title= (String) jsonObject.get("title");
                this.publisher = (String) jsonObject.get("publisher");
                String authorsString = (String) jsonObject.get("author");
                ArrayList<String> authorsArray = (ArrayList<String>) Arrays.stream(authorsString.split("\\(")).collect(Collectors.toList());
                this.author = authorsArray.get(0);
                try {
                    Optional<Author> author=authorRepository.findByName(this.author);
                    author.get().getBooksByAuthor().add(this);
                }catch (EntityNotFoundException e){
                    Author bookAuthor = new Author(this.author);
                    bookAuthor.getBooksByAuthor().add(this);
                    authorRepository.save(bookAuthor);
                }
                this.publishedYear = (String) jsonObject.get("publicationYear");
                this.language=(String) jsonObject.get("language");

            }
        } catch (IOException e){
            e.getMessage();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

//        try{
//
//            Document webSite = Jsoup.connect(urlBuild.toString()).userAgent("mozilla/17.0").get(); //connection to the website
//            // getting title based on ISBN
//            Elements results = webSite.select("h2.title");
//            for (Element result : results) {
//                this.title=(result.getElementsByTag("span").first().text());
//                if (this.title==null){
//                    this.title="";
//                }
//            }
//
//            // getting publisher
//            results = webSite.select("span.opt-publisher");
//            for (Element result:results){
//                this.publisher=(result.getElementsByTag("span").first().text());
//                if (this.publisher==null){
//                    this.publisher="";
//                }
//            }
//            //getting year published
//            results = webSite.select("span.opt-publish-date");
//            for (Element result:results){
//                this.publishedYear=(result.getElementsByTag("span").first().text());
//                if (this.publishedYear==null){
//                    this.publishedYear="";
//                }
//            }
//
//            if (this.language==null){
//                this.language=("Polski");
//            }
//            // getting author
//            results = webSite.select("p.author");
//            for (Element result : results) {
//                this.author=result.getElementsByTag("strong").first().text();
//                if (this.author==null){
//                    this.author="";
//                }
//                try {
//                    Optional<Author> author=authorRepository.findByName(this.author);
//                    author.get().getBooksByAuthor().add(this);
//                }catch (EntityNotFoundException e){
//                    Author bookAuthor = new Author(this.author);
//                    bookAuthor.getBooksByAuthor().add(this);
////                    authorRepository.save(bookAuthor);
//                }
//
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }



}
