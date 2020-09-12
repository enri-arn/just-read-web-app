package domain.service;

import bean.Borrowing;
import bean.User;
import dao.BorrowingRepository;
import dao.UserRepository;
import java.util.ArrayList;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class NotificationService {

    private static NotificationService instance;
    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;

    /**
     * Get an instance of this service of not exist.
     *
     * @return an instance of AuthenticationService.
     */
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public NotificationService() {
        this.borrowingRepository = new BorrowingRepository();
        this.userRepository = new UserRepository();
    }

    /**
     * Get all theborrowing not deliver and put its in a string.
     * 
     * @param email of the user
     * @return a string rapresent a list of borrowing not deliver yet.
     */
    public String getNotification(String email) {
        String list = null;
        if (email != null && !email.isEmpty()) {
            ArrayList<Borrowing> activeBorrowing = (ArrayList<Borrowing>) borrowingRepository.getBorrowingNotDeliver(email);
            list = "You have " + activeBorrowing.size() + " borrowing active and not deliver now";
            if (!activeBorrowing.isEmpty()){
                list += ":<br>";
            } else {
                list += ".<br>";
            }
            list = activeBorrowing.stream().map((borrow) -> " - " + borrow.getBook().getTitle() + " <br>").reduce(list, String::concat);
        }
        return list;
    }
    
    /**
     * Get all the user info.
     * 
     * @param email of the user.
     * @return an instance of user.
     */
    public User getUserData(String email){
        return userRepository.find(email);
    }

}
