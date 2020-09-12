package domain.service;

import bean.Stat;
import bean.User;
import dao.BookRepository;
import dao.BorrowingRepository;
import dao.UserRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class UserService {
    
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private static UserService instance;

    public UserService() {
        this.userRepository = new UserRepository();
        this.bookRepository = new BookRepository();
        this.borrowingRepository = new BorrowingRepository();
    }
    
    /**
     * Get an instance of this service of not exist.
     *
     * @return an instance of AuthenticationService.
     */
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    /**
     * Finds all the user.
     * 
     * @return a list of instance of User.
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    /**
     * Calculate how many users have borrowed at least one book.
     * 
     * @return an instance o Stat.
     */
    public Stat calculateActiveUsers(){
        double active = userRepository.countAllActive();
        System.out.println(active);
        double users = userRepository.countAll();
        System.out.println(users);
        Stat s = new Stat();
        s.setValue((active*100)/users);
        s.setDescription("active readers");
        return s;
    }
    
    /**
     * Calculate the average number of archived borrowing by active user.
     * 
     * @return an instance o Stat.
     */
    public Stat calculateBorrowingAVG(){
        List<User> actives = userRepository.getAllActive();
        int borrowings = 0;
        borrowings = actives.stream().map((active) -> userRepository.countArchivedBorrowingByUser(active.getEmail())).reduce(borrowings, Integer::sum);
        Stat s = new Stat();
        s.setValue(borrowings/actives.size());
        s.setDescription("AVG x active user");
        return s;
    }
    
    /**
     * User that had never borred a book.
     * 
     * @return an instance of Stats.
     */
    public Stat calculateNewerUser(){
        Stat s = new Stat();
        s.setDescription("Newer user");
        return s;
    }
    
    /**
     * How many books had never return home?
     * 
     * @return an insatnce of Stats.
     */
    public Stat calculateBookUndelivered(){
        double allBooks = bookRepository.findAll().size();
        double expiredBooks = borrowingRepository.findAllExpired().size();
        Stat s = new Stat();
        s.setValue((expiredBooks*100)/allBooks);
        s.setDescription("undelivered books");
        return s;
    }
    
    public Stat calculateActiveBorrowing(){
        double nBooks = bookRepository.findAll().size();
        double nActiveBorrowing = borrowingRepository.findAllActive().size();
        Stat s = new Stat();
        System.out.println(nBooks + " -- " + nActiveBorrowing + "  -- " + (nActiveBorrowing*100)/nBooks);
        s.setValue((nActiveBorrowing*100)/nBooks);
        s.setDescription("active borrowing");
        return s;
    }
    
    /**
     * Change the role of the selected user.
     * 
     * @param email
     * @return true id role was changed.
     */
    public boolean changeRole(String email){
        int newRole = userRepository.find(email).getLevel()==0 ? 1 : 0;
        System.out.println(newRole);
        User u = userRepository.find(email);
        u.setLevel(newRole);
        return userRepository.save(u);
    }
    
    /**
     * Modify the profile of the user.
     * 
     * @param user to modify.
     * @param name of the user.
     * @param surname of the user.
     * @param address of the user.
     * @return true if modify was successful.
     */
    public boolean modifyProfile(User user, String name, String surname, String address){
        if(user != null && name != null && !name.isEmpty() && surname != null 
                && !surname.isEmpty() && address != null && !address.isEmpty()){
            user.setName(name);
            user.setSurname(surname);
            user.setAddress(address);
            return userRepository.save(user);
        }
        return true;
    }
    
    /**
     * Not implemented yet.
     * Set the image of the user by uploading and save on the server.
     * 
     * @param image to save.
     * @return true if image was successfully saved.
     * @throws IOException 
     */
    public boolean setUserImage(BufferedImage image) throws IOException{
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizeImagePng = ImageOperationService.resizeImage(image, type, 100, 100);
        ImageIO.write(resizeImagePng, "png", new File("C:\\xampp\\htdocs\\img\\user2.png"));
        return true;
    }
    
    /**
     * Return a user by email.
     * 
     * @param email
     * @return an instance of User.
     */
    public User getUser(String email){
        return userRepository.find(email);
    }
    
}
