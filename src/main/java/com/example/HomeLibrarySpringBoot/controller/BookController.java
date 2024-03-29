package com.example.HomeLibrarySpringBoot.controller;

import com.example.HomeLibrarySpringBoot.config.JwtTokenUtil;
import com.example.HomeLibrarySpringBoot.model.Book;
import com.example.HomeLibrarySpringBoot.model.User;
import com.example.HomeLibrarySpringBoot.model.UsersLibrary;
import com.example.HomeLibrarySpringBoot.model.dto.BookDto;
import com.example.HomeLibrarySpringBoot.service.BookService;
import com.example.HomeLibrarySpringBoot.service.UserService;
import com.example.HomeLibrarySpringBoot.service.UsersLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersLibraryService usersLibraryService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public BookController() {
    }

    public BookController(BookService bookService, UserService userService, UsersLibraryService usersLibraryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.usersLibraryService = usersLibraryService;
    }

    @GetMapping(value = "/booksList")
    @ResponseBody
    public List<BookDto> booksList(HttpServletRequest request){
        String username = jwtTokenUtil.returnUserFromRequest(request);
        User user = userService.getUserByEmail(username);
        Map<Book, String> books;
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        books=usersLibrary.getBooksAndLocation();
        List<BookDto> bookDtos = bookService.getAllBooksDto(new ArrayList<>(books.keySet()));
        for (BookDto bookDto : bookDtos){
            for (Book book : books.keySet()){
            if (bookDto.getId() == book.getId()){
                bookDto.setLocation(books.get(book));
            }
            }
        }

        return bookDtos;
//        return bookService.getAllBooksDto(books);
    }


    @PostMapping(value = "/saveBook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus saveBook(@RequestHeader @Nullable String isbn,
                               HttpServletRequest request,
                               @RequestBody @Nullable BookDto bookDto){
        Book book = null;
        if (bookDto !=null){
            book = bookService.toBook(bookDto);
        }else {
            book = bookService.getBookByIsbn(isbn);
        }
        if (Optional.ofNullable(book.getTitle()).isEmpty()){
            book = new Book();
            book.setIsbn(isbn);
        }
        String username = jwtTokenUtil.returnUserFromRequest(request);
        User user = userService.getUserByEmail(username);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        Map<Book, String> booksAndLocation = usersLibrary.getBooksAndLocation();
        booksAndLocation.put(book, bookDto.getLocation());
        usersLibrary.setBooksAndLocation(booksAndLocation);
        usersLibraryService.save(usersLibrary);
        return HttpStatus.OK;
    }

    @PostMapping("/updateBook")
    public HttpStatus updateBook(@RequestBody BookDto bookDto,
                                 HttpServletRequest request){
        Book book = bookService.toBook(bookDto);
        bookService.updateBook(book);
        String username = jwtTokenUtil.returnUserFromRequest(request);
        User user = userService.getUserByEmail(username);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        Map<Book, String> bookshelf = usersLibrary.getBooksAndLocation();
        bookshelf.put(book, bookDto.getLocation());
        usersLibrary.setBooksAndLocation(bookshelf);
        usersLibraryService.save(usersLibrary);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/uploadCover")
    public HttpStatus uploadCover(HttpServletRequest request,
                                  @RequestParam("cover") MultipartFile image,
                                  @RequestParam("bookId") Integer bookId) throws IOException {
        Book book = bookService.getBook(bookId);
        if (book.getCover() == null){
            book.setCover(image.getBytes());
            bookService.updateBook(book);
            return HttpStatus.ACCEPTED;
        }

        return HttpStatus.IM_USED;
    }


    @DeleteMapping("/deleteBook/{id}")
    public HttpStatus deleteBook(@PathVariable(value = "id") int id,
                                 HttpServletRequest request){
        String username = jwtTokenUtil.returnUserFromRequest(request);
        User user = userService.getUserByEmail(username);
        UsersLibrary usersLibrary = usersLibraryService.getUsersLibraryByUser(user);
        usersLibrary.getBooksAndLocation().remove(bookService.getBook(id));
        usersLibraryService.save(usersLibrary);

        return HttpStatus.ACCEPTED;
    }



}
