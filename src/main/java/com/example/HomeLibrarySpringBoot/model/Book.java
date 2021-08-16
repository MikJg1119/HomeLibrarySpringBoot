package com.example.HomeLibrarySpringBoot.model;


import com.example.HomeLibrarySpringBoot.repository.AuthorRepository;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.persistence.*;
import java.io.IOException;

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

    private static StringBuilder urlBuild = new StringBuilder("https://www.abebooks.com/servlet/SearchResults?sts=t&isbn=");
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "publishedyear")
    private String publishedYear;
    @Column(name = "language")
    private String language=null;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "authorid", insertable = false, updatable = false)
    private Author authorEntity;  //entity needed to get authorid out of authors table
    @Column(name ="authorid")
    private int authorId; //=authorEntity.getId();
    @Transient
    private AuthorRepository authorRepository;
    @Column(name = "saga")
    private String saga;
    @Column(name = "publishingseries")
    private String publishingSeries;
    @Column(name ="loaneeid")
    private int loaneeId;
    @ManyToOne
    @JoinColumn(name = "loaneeid", insertable = false, updatable = false)
    private Loanee loanee;
//    @Autowired
//    public Book() {
//    }


        public Book(){
        if (this.language==null){
            this.language="Polski";
        }

    }


    public Book(String isbn) {
        this.isbn =isbn;
        urlBuild.append(isbn);
        try{

            Document webSite = Jsoup.connect(urlBuild.toString()).userAgent("mozilla/17.0").get(); //connection to the website
            // getting title based on ISBN
            Elements results = webSite.select("h2.title");
            for (Element result : results) {
                this.title=(result.getElementsByTag("span").first().text());
            }
            // getting author
            results = webSite.select("p.author");
            for (Element result : results) {
                this.authorEntity=authorRepository.findByName(result.getElementsByTag("strong").first().text());
//                this.authorEntity = new Author(result.getElementsByTag("strong").first().text());
                this.author = authorEntity.getName();
            }
            // getting publisher
            results = webSite.select("span.opt-publisher");
            for (Element result:results){
                this.publisher=(result.getElementsByTag("span").first().text());
            }
            //getting year published
            results = webSite.select("span.opt-publish-date");
            for (Element result:results){
                this.publishedYear=(result.getElementsByTag("span").first().text());
            }

            if (this.language==null){
                this.language=("Polski");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
