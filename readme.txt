The application itself is currently up and running at prywatnabiblioteka.herokuapp.com
I am constantly upgrading the app and adding the new functionalities to it, therefore it's constantly a work in progress and might contain some internal issues
(at the time of writing user registration and login work fine, as well as adding the books based on ISBN and displaying the books to the user, the rest of functionalities still need to be tested and may contain some bugs)
To create your own instance of the application, just set up a postgresql database, fill in your postgres database's credentials in application.properties file, build the application using gradle and then run it from main class 'HomeLibrarySpringBootApplication',
or by executing jar file in the build folder.
Note:
While Hibernate creates the structure inside database for you, I have noticed, that for tables users_library_books and users_library_loanees it creates unique value constraint for columns book_id and loanee_id - it causes an error due to which different users are not able to
add a book to their own library, that some other user has previously added, or loan books to the same loanee - simply removing that constraint should rectify the bug and if you want to make sure, that duplicate rows are prevented execute the following lines in postgres:
ALTER TABLE users_library_books ADD UNIQUE (book_id, user_library_id);
ALTER TABLE users_library_loanees ADD UNIQUE (loanee_id, user_library_id);
(obviously change the names in those updates, if Hibernate created tables, that are named differently in one way or another)