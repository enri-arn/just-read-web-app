package domain.service;

import bean.User;
import dao.UserRepository;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class AuthenticationService {

    private static AuthenticationService instance;
    private final UserRepository userRepository;

    public AuthenticationService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Get an instance of this service of not exist.
     *
     * @return an instance of AuthenticationService.
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Check if it's possible to authenticate user by email and password passed.
     *
     * @param email: email of the user.
     * @param password: password of the user.
     * @return if authenticate has been succesful the user, null otherwise.
     * @throws java.lang.Exception
     */
    public User authenticate(String email, String password) throws Exception {
        User user = null;
        if (email != null && password != null) {
            if (!password.isEmpty() && !email.isEmpty()) {
                System.out.println("email e password - ok");
                user = userRepository.find(email);
                if (user != null) {
                    System.out.println("user - ok");
                    TrippleDes td= TrippleDes.getInstance();
                    String decrypted = td.decrypt(user.getToken());
                    user = (decrypted.equals(password)) ? user : null;
                }
            }
        }
        return user;
    }

    /**
     * Check the validation of String passed. If validation is correct check if
     * exist a user with the email passed. If not exists, user is registered.
     *
     * @param email: email of new user.
     * @param password: password of new user.
     * @param name: name of new user.
     * @param surname: surname of new user.
     * @return true if registration was succesful, false otherwise.
     * @throws java.lang.Exception
     */
    public boolean registerUser(String email, String password, String name, String surname) throws Exception {
        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty()) {
            System.out.println(email + " " + password + " " + name + " " + surname);
            if (isValidEmail(email) && isValidPassword(password) && isValidName(name) && isValidName(surname)) {
                User user = new User();
                user.setEmail(email);
                TrippleDes td= TrippleDes.getInstance();
                String encrypted=td.encrypt(password);
                user.setToken(encrypted);
                user.setName(name);
                user.setSurname(surname);
                user.setAddress("address not present, modify your profile to add it.");
                System.out.println("DATA VALID");
                if (userRepository.find(email) == null) {
                    System.out.println("TRUE RETURN");
                    return userRepository.save(user);
                }
            }
        }
        return false;
    }

    /**
     * Validation of email with regex roule.
     *
     * @param email: email to validate.
     * @return true if validation was succesful, false otherwise.
     */
    private boolean isValidEmail(String email) {
        System.out.println(email.matches("\\S+@\\S+\\.\\S+"));
        return email.matches("\\S+@\\S+\\.\\S+");
    }

    /**
     * Validation of password with regex roule. Password must contain: 
     * - min 8 characters; 
     * - at least one uppercase letter; 
     * - at least one lowercase letter; 
     * - at least one number; 
     * - at least one special characters;
     *
     * @param password: password to validate.
     * @return true if validation was succesful, false otherwise.
     */
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$");
    }

    /**
     * Validationn of the name and surname.
     *
     * @param name: name or surname to validate.
     * @return true if validation was succesful, false otherwise.
     */
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\\\s]+");
    }
}
