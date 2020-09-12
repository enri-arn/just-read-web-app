package domain.service;

import bean.Author;
import bean.Book;
import bean.Borrowing;
import bean.Category;
import bean.User;
import dao.AuthorRepository;
import dao.BorrowingRepository;
import dao.CategoryRepository;
import dao.RatingRepository;
import dao.SearchableRepository;
import dao.UserRepository;
import domain.data.DefaultBookData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class SearchableService {

    private static SearchableService instance;
    private final SearchableRepository searchableRepository;
    private final RatingRepository ratingRepository;
    private final AuthorRepository authorRepository;
    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Get an instance of this service of not exist.
     *
     * @return an instance of AuthenticationService.
     */
    public static SearchableService getInstance() {
        if (instance == null) {
            instance = new SearchableService();
        }
        return instance;
    }

    public SearchableService() {
        this.searchableRepository = new SearchableRepository();
        this.ratingRepository = new RatingRepository();
        this.authorRepository = new AuthorRepository();
        this.borrowingRepository = new BorrowingRepository();
        this.userRepository = new UserRepository();
        this.categoryRepository = new CategoryRepository();
    }

    /**
     * Find book in databse.
     *
     * @param query: string with the request of the user.
     * @return a list if instance of DefaultBookData.
     */
    public List<DefaultBookData> findBookBy(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        ArrayList<DefaultBookData> defaultBookData = new ArrayList<>();
        ArrayList<Book> books = (ArrayList<Book>) searchableRepository.findBookBy(query);
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
     * Find active borrowing in databse.
     *
     * @param query: string with the request of the user.
     * @return a list if instance of Borrowing.
     */
    public List<Borrowing> findActiveBorrowing(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        List<Borrowing> active = borrowingRepository.findAllActive();
        List<Borrowing> search = new ArrayList<>();
        active.stream().filter((borrow) -> (containsIgnoreCase(borrow.getBook().getTitle(), query)
                || containsIgnoreCase(borrow.getBook().getIsbn(), query)
                || containsIgnoreCase(borrow.getBook().getPublisher().getName(), query)
                || containsIgnoreCase(borrow.getUser().getEmail(), query)
                || containsIgnoreCase(borrow.getUser().getName(), query)
                || containsIgnoreCase(borrow.getUser().getSurname(), query))).forEachOrdered((borrow) -> {
            search.add(borrow);
        });
        System.out.println(search);
        return search;
    }

    /**
     * Find expired borrowing in databse.
     *
     * @param query: string with the request of the user.
     * @return a list if instance of Borrowing.
     */
    public List<Borrowing> findExpiredBorrowing(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        List<Borrowing> expired = borrowingRepository.findAllExpired();
        List<Borrowing> search = new ArrayList<>();
        expired.stream().filter((borrow) -> (containsIgnoreCase(borrow.getBook().getTitle(), query)
                || containsIgnoreCase(borrow.getBook().getIsbn(), query)
                || containsIgnoreCase(borrow.getBook().getPublisher().getName(), query)
                || containsIgnoreCase(borrow.getUser().getEmail(), query)
                || containsIgnoreCase(borrow.getUser().getName(), query)
                || containsIgnoreCase(borrow.getUser().getSurname(), query))).forEachOrdered((borrow) -> {
            search.add(borrow);
        });
        System.out.println(search);
        return search;
    }

    /**
     * Find archived borrowing in databse.
     *
     * @param query: string with the request of the user.
     * @return a list if instance of Borrowing.
     */
    public List<Borrowing> findArchivedBorrowing(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        List<Borrowing> archived = borrowingRepository.findAllArchivied();
        List<Borrowing> search = new ArrayList<>();
        archived.stream().filter((borrow) -> (containsIgnoreCase(borrow.getBook().getTitle(), query)
                || containsIgnoreCase(borrow.getBook().getIsbn(), query)
                || containsIgnoreCase(borrow.getBook().getPublisher().getName(), query)
                || containsIgnoreCase(borrow.getUser().getEmail(), query)
                || containsIgnoreCase(borrow.getUser().getName(), query)
                || containsIgnoreCase(borrow.getUser().getSurname(), query))).forEachOrdered((borrow) -> {
            search.add(borrow);
        });
        System.out.println(search);
        return search;
    }

    /**
     * Find user in databse.
     *
     * @param query: string with the request of the user.
     * @return a list if instance of User.
     */
    public List<User> findUser(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        List<User> users = userRepository.findAll();
        List<User> search = new ArrayList<>();
        users.stream().filter((user) -> (containsIgnoreCase(user.getName(), query)
                || containsIgnoreCase(user.getSurname(), query)
                || containsIgnoreCase(user.getName().concat(" " + user.getSurname()), query)
                || containsIgnoreCase(user.getEmail(), query))).forEachOrdered((user) -> {
            search.add(user);
        });
        System.out.println(search);
        return search;
    }

    /**
     * Find book in databse.
     *
     * @param query: string with the request of the user.
     * @return a list if instance of Object with books, author and category.
     */
    public List<Object> findBook(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        BookService bookService = BookService.getInstance();
        List<Object> triade = new ArrayList<>();
        //array di array di autori
        List<List<Author>> authorslist = new ArrayList<>();
        //array di array di categories
        List<List<Category>> categorieslist = new ArrayList<>();
        //array di libri
        List<Book> books = bookService.getAllBooks();
        List<Book> search = new ArrayList<>();
        books.stream().filter((book) -> {
            return book.getIsbn().contains(query)
                    || containsIgnoreCase(book.getTitle(), query)
                    || containsIgnoreCase(book.getYearsOfPublication().toString(), query)
                    || containsIgnoreCase(book.getLanguage(), query)
                    || containsIgnoreCase(book.getPublisher().getName(), query);
        }).forEachOrdered((book) -> {
            search.add(book);
        });

        if (search.isEmpty()) {
            return null;
        }
        search.stream().map((Book book) -> {
            authorslist.add(authorRepository.getAuthorsOfBook(book.getIsbn()));
            return book;
        }).forEachOrdered((book) -> {
            categorieslist.add(categoryRepository.getBookCategory(book.getIsbn()));
        });
        triade.add(search);
        triade.add(authorslist);
        triade.add(categorieslist);
        return triade;
    }

    /**
     * Check if string str contains ignoring case searchStr
     *
     * @param str where to find
     * @param searchStr what to find
     * @return true if str contains searchStr
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }

        final int length = searchStr.length();
        if (length == 0) {
            return true;
        }

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) {
                return true;
            }
        }
        return false;
    }

}
