package com.example.HomeLibrarySpringBoot.service;

import com.example.HomeLibrarySpringBoot.model.Author;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.dto.BookDto;
import com.example.HomeLibrarySpringBoot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private UserService userService;

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void removeBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBook(int id) {
        Optional<Book> optional = bookRepository.findById(id);
        Book book = null;
        if (optional.isPresent()){
            book = optional.get();
        }else {
            throw new RuntimeException(" Book not found ");
        }
        return book;
    }

    @Override
    public void updateBook(Book book) {
        Optional<Book> optional = bookRepository.findById(book.getId());
        if (optional.isPresent()){
            Book dBEntity = optional.get();
            dBEntity.setId(book.getId());
            dBEntity.setAuthor(book.getAuthor());
            dBEntity.setIsbn(book.getIsbn());
            dBEntity.setPublisher(book.getPublisher());
            dBEntity.setPublishedYear(book.getPublishedYear());
            dBEntity.setLanguage(book.getLanguage());
            dBEntity.setSaga(book.getSaga());
            dBEntity.setPublishingSeries(book.getPublishingSeries());
            bookRepository.save(dBEntity);
        }else {
            bookRepository.save(book);
        }
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();

    }

    @Override
    public List<Book> getBooksById(Iterable<Integer> ids) {
        return bookRepository.findAllById(ids);
    }

    @Override
    public Book getBookByName(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> getBookByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> optional = bookRepository.findByIsbn(isbn);
        Book book = null;
        if (optional.isPresent()){
            return  optional.get();
        }else {
            book = new Book();
            book.scrapeBookByIsbn(isbn);
            bookRepository.save(book);
        }
        Optional<Author> author=authorService.getAuthorByName(book.getAuthor());
        if (author.isPresent()) {
        author.get().getBooksByAuthor().add(book);

//      book.setAuthorId(author.get().getId());
        authorService.updateAuthor(author.get());
        }else {
        Author bookAuthor = new Author(book.getAuthor());
        bookAuthor.getBooksByAuthor().add(book);
//                bookRepository.save(book);
//                book.setAuthorId(bookAuthor.getId());
        authorService.addAuthor(bookAuthor);
        }
        return book;
    }

    @Override
    public Book toBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setPublisher(bookDto.getPublisher());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setLanguage(bookDto.getLanguage());
        book.setId(bookDto.getId());
        book.setSaga(bookDto.getSaga());
        book.setPublishingSeries(bookDto.getPublishingSeries());
        return book;
    }

    @Override
    public BookDto toBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPublisher(book.getPublisher());
        bookDto.setPublishedYear(book.getPublishedYear());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setId(book.getId());
        bookDto.setSaga(book.getSaga());
        bookDto.setPublishingSeries(book.getPublishingSeries());
        return bookDto;
    }

    @Override
    public List<BookDto> getAllBooksDto(List<Book> books) {
        List<BookDto> listBookDto = new ArrayList<BookDto>();
        for (Book book : books){
            listBookDto.add(toBookDto(book));
    }

        return listBookDto;
    }
}
