package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.dto.AuthorDto;
import com.example.HomeLibrarySpringBoot.model.dto.BookDto;
import com.example.HomeLibrarySpringBoot.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UsersLibraryService usersLibraryService;



    @Override
    public void addAuthor(Author author) {
    authorRepository.save(author);
    }

    @Override
    public void removeAuthor(int id) {

    authorRepository.deleteById(id);
    }

    @Override
    public Author getAuthor(int id) {
        return authorRepository.getById(id);
    }

    @Override
    public void updateAuthor(Author author) {
        Optional<Author> optionalAuthor = authorRepository.findById(author.getId());
        if (optionalAuthor.isPresent()){
            Author fromDb = optionalAuthor.get();
            fromDb.setId(author.getId());
            fromDb.setBooksByAuthor(author.getBooksByAuthor());
            fromDb.setName(author.getName());

            authorRepository.save(fromDb);
        }else {
            authorRepository.save(author);
        }
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getAuthorsById(Iterable<Integer> ids) {
        return authorRepository.findAllById(ids);
    }

    @Override
    public Optional<Author> getAuthorByName(String name) throws EntityNotFoundException {
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> getAuthorsByUser(User user) {
        List<Book> usersbooks = usersLibraryService.getBooksByUser(user);
        List<String> authorsNames = new ArrayList<String>();
        for (Book book : usersbooks){
            authorsNames.add(book.getAuthor());
        }
        List<Author> authors=new ArrayList<Author>();
        for (String author :authorsNames){
            Optional <Author> search = getAuthorByName(author);
            if (search.isPresent()){
                authors.add(search.get());
            }
        }
        return authors;
    }

    @Override
    public Author getAuthorFromDto(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());
        author.setId(authorDto.getId());

        return author;
    }
}
