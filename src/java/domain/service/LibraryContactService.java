package domain.service;

import bean.Rule;
import dao.RuleRepository;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class LibraryContactService {

    private static LibraryContactService instance;
    private final RuleRepository ruleRepository;

    /**
     * Get an instance of this service of not exist.
     *
     * @return an instance of AuthenticationService.
     */
    public static LibraryContactService getInstance() {
        if (instance == null) {
            instance = new LibraryContactService();
        }
        return instance;
    }
    
    public LibraryContactService() {
        this.ruleRepository = new RuleRepository();
    }
    
    /**
     * Finds all the library contact.
     * 
     * @param id: id of the library.
     * @return an instance of Rule.
     */
    public Rule getContacts(int id){
        return ruleRepository.find(id);
    }
    
    /**
     * Get the buisness hour of the library.
     * 
     * @param id of the library.
     * @return a string rapresent the buisness hour.
     */
    public String gerBuisnessHour(int id){
        return ruleRepository.getBuisnessHour(id);
    }
}
