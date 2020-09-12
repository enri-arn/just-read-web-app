package domain.service;

import bean.Author;
import bean.Book;
import bean.Borrowing;
import bean.Category;
import bean.Publisher;
import bean.Rating;
import bean.User;
import dao.AuthorRepository;
import dao.BookAuthorRepository;
import dao.BookCategoryRepository;
import dao.BookRepository;
import dao.BorrowingRepository;
import dao.CategoryRepository;
import dao.PublisherRepository;
import dao.RatingRepository;
import dao.RuleRepository;
import dao.UserRepository;
import domain.data.BookInfoData;
import domain.data.BorrowingBookData;
import domain.data.DefaultBookData;
import domain.data.ReviewsData;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class BookService {

    private static BookService instance;
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final CategoryRepository categoryRepository;
    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;
    private final RuleRepository ruleRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookCategoryRepository bookCategoryRepository;

    /**
     * Get an instance of this service of not exist.
     *
     * @return an instance of AuthenticationService.
     */
    public static BookService getInstance() {
        if (instance == null) {
            instance = new BookService();
        }
        return instance;
    }

    public BookService() {
        this.bookRepository = new BookRepository();
        this.ratingRepository = new RatingRepository();
        this.categoryRepository = new CategoryRepository();
        this.borrowingRepository = new BorrowingRepository();
        this.userRepository = new UserRepository();
        this.ruleRepository = new RuleRepository();
        this.authorRepository = new AuthorRepository();
        this.publisherRepository = new PublisherRepository();
        this.bookAuthorRepository = new BookAuthorRepository();
        this.bookCategoryRepository = new BookCategoryRepository();
    }

    /**
     * Call the delete method of BookRepository to delete book.
     * 
     * @param isbn of the book.
     * @return true if delete was successufl, false otherwise.
     */
    public boolean deleteBook(String isbn) {
        return (isbn != null && !isbn.isEmpty()) ? bookRepository.delete(isbn) : false;
    }

    /**
     * Find the book with isbn passed. If exists get all the information about
     * it.
     *
     * @param user: user for check if has alredy the book.
     * @param isbn: isbn of the book to find.
     * @return BookInfoData data if book exists, null otherwise.
     */
    public BookInfoData getBookInfo(String user, String isbn) {
        BookInfoData bookInfo = new BookInfoData();
        if (isbn != null && !isbn.isEmpty()) {
            bookInfo.setIsbn(bookRepository.find(isbn).getIsbn());
            bookInfo.setTitle(bookRepository.find(isbn).getTitle());
            bookInfo.setPublisher(bookRepository.find(isbn).getPublisher().getName());
            bookInfo.setLanguage(bookRepository.find(isbn).getLanguage());
            bookInfo.setPlot(bookRepository.find(isbn).getPlot());
            bookInfo.setCoverHD(bookRepository.find(isbn).getCoverHD());
            bookInfo.setCover(bookRepository.find(isbn).getCover());
            bookInfo.setPages(bookRepository.find(isbn).getPages());
            bookInfo.setYearsOfPublication(bookRepository.find(isbn).getYearsOfPublication().toString());
            if (user != null) {
                bookInfo.setIsAvaiable(borrowingRepository.find(user, isbn) == null);
            } else {
                bookInfo.setIsAvaiable(true);
            }
            bookInfo.setAuthor(authorRepository.getAuthorOfBook(isbn));
            bookInfo.setRate(ratingRepository.getRateOfBook(isbn));
            bookInfo.setOneStar(ratingRepository.countOneStar(isbn, 0, 1));
            bookInfo.setTwoStar(ratingRepository.countOneStar(isbn, 1, 2));
            bookInfo.setThreeStar(ratingRepository.countOneStar(isbn, 2, 3));
            bookInfo.setFourStar(ratingRepository.countOneStar(isbn, 3, 4));
            bookInfo.setFiveStar(ratingRepository.countOneStar(isbn, 4, 5));
            bookInfo.setNumVoters(ratingRepository.getNumVoters(isbn));
        }
        return bookInfo;
    }

    /**
     * Get all the reviews of a book by calling the 
     * RatingReposotory.
     * 
     * @param isbn of the book.
     * @return a list of instance of ReviewsData.
     */
    public List<ReviewsData> getReviewsOfBook(String isbn) {
        return ratingRepository.getReviews(isbn);
    }

    /**
     * Finds all the category.
     *
     * @return a list of instance of Category.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Finds the book of the category passed in the limit start - end.
     *
     * @param nameCategory: name of the category.
     * @return a list of instance of Book.
     */
    public List<DefaultBookData> getBooksByCategory(String nameCategory) {
        ArrayList<DefaultBookData> defaultBookData = new ArrayList<>();
        ArrayList<Book> books = (ArrayList<Book>) bookRepository.findByCategory(nameCategory);
        books.stream().map((item) -> {
            DefaultBookData defaultBook = new DefaultBookData();
            defaultBook.setIsbn(item.getIsbn());
            defaultBook.setTitle(item.getTitle());
            defaultBook.setCover(item.getCover());
            defaultBook.setRate(ratingRepository.getRateOfBook(item.getIsbn()));
            defaultBook.setAuthor(authorRepository.getAuthorOfBook(item.getIsbn()));
            return defaultBook;
        }).forEachOrdered((defaultBook) -> {
            defaultBookData.add(defaultBook);
        });
        return defaultBookData;
    }

    /**
     * Finds all the category of the book.
     *
     * @param isbn: isbn of the book.
     * @return a list of instance of category of the book passed.
     */
    public List<Category> getCategoryOfBook(String isbn) {
        return categoryRepository.getBookCategory(isbn);
    }

    /**
     * Add a borrowing of the book with isbn passed.
     *
     * @param email: email of the user of the borrowing.
     * @param isbn: isbn of the book to borrow.
     * @param sendHome: rapresent the intention of the user to send home book.
     * @return true if borrowing is saved or false otherwise.
     */
    public boolean addBorrowing(String email, String isbn, boolean sendHome) {
        if (email != null && isbn != null && !email.isEmpty() && !isbn.isEmpty()) {
            if (sendHome) {
                if (checkUserStreet(email)) {
                    return false;
                }
            }
            if (checkBookExists(isbn) && !checkBorrowingExists(isbn) && !checkBorrowingExists(isbn, email) && checkMaxBorrowing(email)) {
                Borrowing borrowing = new Borrowing();
                borrowing.setBook(bookRepository.find(isbn));
                borrowing.setUser(userRepository.find(email));
                LocalDate localDate = LocalDate.now();
                borrowing.setStartDate(Date.valueOf(localDate));
                java.sql.Date endDate = Date.valueOf(localDate);
                Calendar c = Calendar.getInstance();
                c.setTime(endDate);
                int borrowDays = ruleRepository.getBorrowDays();
                c.add(Calendar.DATE, borrowDays);
                java.sql.Date endDatesql = new java.sql.Date(c.getTimeInMillis());
                borrowing.setEndDate(endDatesql);
                borrowing.setRenewal(0);
                borrowing.setIsDeliver(false);
                System.out.println("\n\nprima di save\n\n");
                return borrowingRepository.save(borrowing);
            }
        }
        return false;
    }

    /**
     * Check if user passed have address in the database.
     * 
     * @param email of the user.
     * @return true if user have address, false otherwise.
     */
    public boolean checkUserStreet(String email) {
        if(email == null || email.isEmpty()){
            return false;
        }
        User user = userRepository.find(email);
        return user.getAddress().equalsIgnoreCase("address not present, modify your profile to add it.");
    }

    /**
     * Increase days of the actual borrowing if possible.
     *
     * @param email: user of the borrowing.
     * @param isbn: isbn of the book.
     * @return true if operation was succesful, false otherwise.
     */
    public boolean increaseBorrowing(String email, String isbn) {
        if (email != null && isbn != null && !email.isEmpty() && !isbn.isEmpty()) {
            if (checkBookExists(isbn) && checkBorrowingExists(isbn, email)) {
                Borrowing actualBorrow = borrowingRepository.find(email, isbn);
                if (borrowingRepository.checkBorrowRenewal(actualBorrow) && borrowingRepository.checkLowerRenewalDay(actualBorrow)) {
                    java.sql.Date endDate = actualBorrow.getEndDate();
                    Calendar c = Calendar.getInstance();
                    c.setTime(endDate);
                    int borrowDays = ruleRepository.getBorrowDays();
                    c.add(Calendar.DATE, borrowDays);
                    java.sql.Date endDatesql = new java.sql.Date(c.getTimeInMillis());
                    actualBorrow.setEndDate(endDatesql);
                    actualBorrow.setRenewal(actualBorrow.getRenewal() + 1);
                    System.out.println(actualBorrow.toString());
                    return borrowingRepository.saveAll(actualBorrow);
                }
            }
        }
        return false;
    }

    /**
     * Deliver the actual borrowing of the user.
     *
     * @param email: user of the borrowing.
     * @param isbn: isbn of the book.
     * @return true if operation was succesful, false otherwise.
     */
    public boolean deliverBorrowing(String email, String isbn) {
        if (email != null && isbn != null && !email.isEmpty() && !isbn.isEmpty()) {
            if (checkBookExists(isbn) && checkBorrowingExists(isbn)) {
                Borrowing borrowing = borrowingRepository.find(email, isbn);
                LocalDate localDate = LocalDate.now();
                borrowing.setEndDate(Date.valueOf(localDate));
                borrowing.setIsDeliver(true);
                return borrowingRepository.saveAll(borrowing);
            }
        }
        return false;
    }

    /**
     * Check if book passed exists in database.
     *
     * @param isbn: isbn of the book.
     * @return true if book exists, false otherwise.
     */
    public boolean checkBookExists(String isbn) {
        System.out.println("check book exists");
        return bookRepository.find(isbn) != null;
    }

    /**
     * Check if user borrow are less than max borrowing permitted
     * by library.
     * 
     * @param email of the user.
     * @return true if it's all ok, false otherwise.
     */
    public boolean checkMaxBorrowing(String email) {
        int userBorrow = (borrowingRepository.findActiveByUser(email)).size();
        int getMaxBorrowing = ruleRepository.getMaxBorrowing();
        System.out.println("check max borrowing ... " + userBorrow + "  " + getMaxBorrowing);
        return userBorrow < getMaxBorrowing;
    }

    /**
     * Check if borrowing alredy exists.
     *
     * @param isbn: isbn of the book.
     * @param email: email of the user.
     * @return true if borrowing exists, false otherwise.
     */
    public boolean checkBorrowingExists(String isbn, String email) {
        System.out.println("check borrowing exists isbn email");
        return borrowingRepository.find(email, isbn) != null;
    }

    /**
     * Check if borrowing alredy exists.
     *
     * @param isbn: isbn of the book.
     * @return true if borrowing exists, false otherwise.
     */
    public boolean checkBorrowingExists(String isbn) {
        System.out.println("check borrowing exists isbn");
        return borrowingRepository.find(isbn);
    }

    /**
     * Finds all the borrowing of the user with email passed.
     *
     * @param email: email of the user.
     * @return a list of instance of Borrowing if exists.
     */
    public List<BorrowingBookData> getBorrowingByUser(String email) {
        ArrayList<BorrowingBookData> borrowing = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Borrowing> books = (ArrayList<Borrowing>) borrowingRepository.findActiveByUser(email);
        books.stream().map((item) -> {
            BorrowingBookData borrowingData = new BorrowingBookData();
            borrowingData.setIsbn(item.getBook().getIsbn());
            borrowingData.setTitle(item.getBook().getTitle());
            borrowingData.setCover(item.getBook().getCover());
            borrowingData.setDates(df.format(item.getStartDate()) + "  -  " + df.format(item.getEndDate()));
            Calendar c = Calendar.getInstance();
            long diff = c.getTimeInMillis() - item.getStartDate().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long difftot = item.getEndDate().getTime() - item.getStartDate().getTime();
            long diffDaystot = difftot / (24 * 60 * 60 * 1000);
            borrowingData.setProgress((int) (((int) diffDays * 100) / diffDaystot));
            borrowingData.setAuthor(authorRepository.getAuthorOfBook(item.getBook().getIsbn()));
            return borrowingData;
        }).forEachOrdered((borrowingData) -> {
            borrowing.add(borrowingData);
        });
        return borrowing;
    }

    /**
     * Finds all the borrowing of the user with email passed.
     *
     * @param email: email of the user.
     * @return a list of instance of Borrowing if exists.
     */
    public List<DefaultBookData> getHistoryByUser(String email) {
        ArrayList<DefaultBookData> histories = new ArrayList<>();
        ArrayList<Borrowing> books = (ArrayList<Borrowing>) borrowingRepository.findByUser(email);
        books.stream().map((item) -> {
            DefaultBookData defaultBook = new DefaultBookData();
            defaultBook.setIsbn(item.getBook().getIsbn());
            defaultBook.setTitle(item.getBook().getTitle());
            defaultBook.setCover(item.getBook().getCover());
            defaultBook.setRate(ratingRepository.getRateOfBook(item.getBook().getIsbn()));
            defaultBook.setAuthor(authorRepository.getAuthorOfBook(item.getBook().getIsbn()));
            return defaultBook;
        }).forEachOrdered((defaultBook) -> {
            histories.add(defaultBook);
        });
        return histories;
    }

    /**
     * Finds all the favourite books of the user.
     *
     * @param email: email of the user.
     * @return a list of instance of Book if exist.
     */
    public List<DefaultBookData> getFavouriteByUser(String email) {
        ArrayList<DefaultBookData> favourites = new ArrayList<>();
        ArrayList<Book> books = (ArrayList<Book>) bookRepository.findFavouriteByUser(email);
        books.stream().map((item) -> {
            DefaultBookData defaultBook = new DefaultBookData();
            defaultBook.setIsbn(item.getIsbn());
            defaultBook.setTitle(item.getTitle());
            defaultBook.setCover(item.getCover());
            defaultBook.setRate(ratingRepository.getRateOfBook(item.getIsbn()));
            defaultBook.setAuthor(authorRepository.getAuthorOfBook(item.getIsbn()));
            return defaultBook;
        }).forEachOrdered((defaultBook) -> {
            favourites.add(defaultBook);
        });
        return favourites;
    }

    /**
     * Save or update a favourite book updating or inserting in rating table the
     * little heart column.
     *
     * @param book: isbn of the book.
     * @param email: email of the user.
     * @param littleHeart: integer which value rapresent: - 1: little heart
     * present(this is a favourite); - 0: little heart non present(this isn't a
     * favourite);
     * @return true if updating was succesful, false otherwise.
     */
    public boolean updateFavourite(String book, String email, int littleHeart) {
        if (book != null && email != null && !book.isEmpty() && !email.isEmpty()) {
            if (checkBookExists(book)) {
                Rating rate;
                if (ratingRepository.findByUserBook(email, book) != null) {
                    rate = ratingRepository.findByUserBook(email, book);
                    rate.setLittleHeart((littleHeart != 0));
                    return ratingRepository.saveFavourite(rate);
                } else {
                    rate = new Rating();
                    rate.setBook(bookRepository.find(book));
                    rate.setUser(userRepository.find(email));
                    rate.setRate(3);
                    rate.setLittleHeart((littleHeart != 0));
                    return ratingRepository.save(rate);
                }
            }
        }
        return false;
    }

    /**
     * Finds all the borrowing in the database.
     *
     * @return a list of instance of Borrowing.
     */
    public List<Borrowing> getAllBorrowing() {
        return borrowingRepository.findAll();
    }

    /**
     * Check if rating book is possibile and add a rate of a book.
     * Also user that have read this book can rating it.
     * Book can rate just once.
     * 
     * @param email of the user
     * @param isbn of the book
     * @param comment of the user
     * @param starRate number of star user selected
     * @param littleHeart if user add book to his favourite list.
     * @return true if adding rate was successful, false otherwise.
     */
    public boolean addRate(String email, String isbn, String comment, float starRate, boolean littleHeart) {
        if (email != null && isbn != null && comment != null) {
            if (checkBookIsRead(email, isbn)) {
                if (checkRatePossible(email, isbn)) {
                    System.out.println("................existing rate");
                    Rating existRate = ratingRepository.findByUserBook(email, isbn);
                    existRate.setComment(comment);
                    existRate.setRate(starRate);
                    return ratingRepository.saveFavourite(existRate);
                } else {
                    System.out.println("................new rate");
                    Rating rate = new Rating();
                    rate.setBook(bookRepository.find(isbn));
                    rate.setUser(userRepository.find(email));
                    rate.setComment(comment);
                    rate.setRate(starRate);
                    rate.setLittleHeart(littleHeart);
                    return ratingRepository.save(rate);
                }
            }
        }
        return false;
    }

    /**
     * Add a new category to the database if it doesn't already exist.
     *
     * @param name
     * @param icon
     * @param color
     * @return
     */
    public boolean addCategory(String name, String icon, String color) {
        if (name != null && icon != null && color != null) {
            if (!name.isEmpty()) {
                if (icon.isEmpty()) {
                    icon = "ico.ico";
                }
                if (color.isEmpty()) {
                    color = "#fff000";
                }
                if (categoryRepository.findByName(name) != null) {
                    return false;
                }
                Category c = new Category();
                c.setName(name);
                c.setIcon(icon);
                return categoryRepository.save(c);
            }
        }
        return false;
    }

    /**
     * Add a new author to the database if it doesn't already exist.
     *
     * @param name
     * @param surname
     * @return
     */
    public boolean addAuthor(String name, String surname) {
        if (name != null && surname != null) {
            if (!(name.isEmpty() && surname.isEmpty())) {
                if (authorRepository.find(name, surname) != null) {
                    return false;
                }
                Author a = new Author();
                a.setName(name);
                a.setSurname(surname);
                return authorRepository.save(a);
            }
        }
        return false;
    }

    /**
     * Check if rate is possible.
     * 
     * @param email of the user.
     * @param isbn of the book.
     * @return true if rateis possible, false othrwise.
     */
    public boolean checkRatePossible(String email, String isbn) {
        return ratingRepository.findByUserBook(email, isbn) != null;
    }

    /**
     * Check if book is read.
     * 
     * @param email of the user
     * @param isbn of the book
     * @return true if book is read, false otherwise.
     */
    public boolean checkBookIsRead(String email, String isbn) {
        return borrowingRepository.find(email, isbn) != null;
    }

    /**
     * Stting the default cover of a book.
     * This method is not implemented but its goal
     * would be to save in database thedefault cover book 
     * in the server cover directory.
     * 
     * @return a string rapresent the default path.
     */
    public String setDefaultCover() {
        return "";
    }

    /**
     * Find an author by name and surname calling correct repsoitory.
     * 
     * @param name of the author
     * @param surname of the author
     * @return  an instance of author.
     */
    public Author findAuthorByName(String name, String surname) {
        if(name == null || name.isEmpty() || surname == null || surname.isEmpty()){
            return null;
        }
        return authorRepository.find(name, surname);
    }

    /**
     * Finds all the active borrowing.
     *
     * @return
     */
    public List<Borrowing> getAllActiveBorrowing() {
        return borrowingRepository.findAllActive();
    }

    /**
     * Finds all the expired borrowing.
     *
     * @return
     */
    public List<Borrowing> getAllExpiredBorrowing() {
        return borrowingRepository.findAllExpired();
    }

    /**
     * Finds all the archived borrowing.
     *
     * @return
     */
    public List<Borrowing> getAllArchivedBorrowing() {
        return borrowingRepository.findAllArchivied();
    }

    /**
     * Finds all the publishers.
     *
     * @return a list of instance of Publisher.
     */
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    /**
     * Add a new book to the library.
     *
     * @param ISBN of the book
     * @param title of the book
     * @param categories of the book
     * @param publisher of the book
     * @param language of the book
     * @param plot of the book
     * @param cover of the book
     * @param pages of the book
     * @param year of the book
     * @param authors of the book
     * @return true if book was successfully added, false otherwise.
     */
    public boolean addBook(String ISBN, String title, String[] categories, String publisher, String language, String plot, String cover, int pages, Year year, String[] authors) {
        if (ISBN != null && title != null && categories != null && publisher != null && language != null && plot != null && cover != null && year != null && pages >= 0 && authors != null) {
            if (!(ISBN.isEmpty() && title.isEmpty() && categories.length < 1 && language.isEmpty() && plot.isEmpty() && authors.length < 1)) {
                Book book = new Book();
                if (cover.isEmpty()) {
                    cover = setDefaultCover();
                }
                if (plot.trim().isEmpty()) {
                    plot = "Plot unavailable.";
                }
                if(checkBookExists(ISBN)){
                    if(!deleteAllAuthorCategory(ISBN)){
                        return false;
                    }
                }
                System.out.println("cover e plot ok");
                Publisher pub = publisherRepository.findByName(publisher);
                System.out.println("cerco publisher ok");
                if (pub == null) {
                    pub = new Publisher();
                    pub.setName(publisher);
                    publisherRepository.save(pub);
                    pub = publisherRepository.findByName(publisher);
                }
                System.out.println("publisher ok");
                book.setIsbn(ISBN);
                book.setTitle(title);
                book.setCover(cover);
                book.setLanguage(language);
                book.setPages(pages);
                book.setPlot(plot);
                book.setYearsOfPublication(year);
                book.setPublisher(pub);
                book.setUnavailable(false);
                System.out.println("prima di save");
                bookRepository.save(book);
                System.out.println("book ok");
                for (String author : authors) {
                    String[] name = author.trim().split(" ");
                    System.out.println("nome: " + name[0] + "; cognome: " + name[1]);
                    Author a = findAuthorByName(name[0], name[1]);
                    System.out.println("cerco author ok");
                    if (a == null) {
                        //System.out.println("nome: " + name[0] + "; cognome: " + name[1] + " è nuovo");
                        a = new Author();
                        a.setName(name[0]);
                        a.setSurname(name[1]);
                        authorRepository.save(a);
                        System.out.println("salvo author ok");
                        a = findAuthorByName(name[0], name[1]);
                    }
                    bookAuthorRepository.save(a, book);
                    System.out.println("salvo author book ok");
                }
                for (String category : categories) {
                    Category c = categoryRepository.findByName(category);
                    System.out.println("salvo category book prima " + category);
                    bookCategoryRepository.setCategory(c, book);
                    System.out.println("salvo category book ok");
                }
                System.out.println("ritornerò true ok");
                return true;
            }
            System.out.println("ritornerò false ok");
        }
        return false;
    }
    
    /**
     * Delete all the occurencies of book in association.
     * 
     * @param isbn of the book.
     * @return of delete was succesful, false otherwise.
     */
    public boolean deleteAllAuthorCategory(String isbn){
        return bookCategoryRepository.delete(isbn) && bookAuthorRepository.delete(isbn);
    }

    /**
     * Return a list of books.
     *
     * @return
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Returns all the authors.
     *
     * @return
     */
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Delete a category given.
     *
     * @param id
     * @return
     */
    public boolean deleteCategory(int id) {
        return categoryRepository.delete(id);
    }

    /**
     * A list of all books, all of their authors and all of their categories.
     *
     * @return
     */
    public List<Object> getAllBooksInfo() {
        List<Object> triade = new ArrayList<>();
        //array di array di autori
        List<List<Author>> authorslist = new ArrayList<>();
        //array di array di categories
        List<List<Category>> categorieslist = new ArrayList<>();
        //array di libri
        List<Book> books = getAllBooks();
        books.stream().map((book) -> {
            //per ogni libro aggiungo un array di autori e un array di generi ai rispettivi array di array
            authorslist.add(authorRepository.getAuthorsOfBook(book.getIsbn()));
            return book;
        }).forEachOrdered((book) -> {
            categorieslist.add(categoryRepository.getBookCategory(book.getIsbn()));
        });
        triade.add(books);
        triade.add(authorslist);
        triade.add(categorieslist);
        return triade;
    }

    /**
     * Update a book.
     *
     * @param book
     * @return
     */
    public boolean updateBook(Book book) {
        return bookRepository.save(book);
    }

}
