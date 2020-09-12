package dao;

import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public interface Repository<T, ID> {
    
    /**
     * Finds all the elements of type T in the database.
     * @return list of instances of T.
     */
    List<T> findAll(); 
    
    /**
     * Find the T elem with id passed.
     * @param id: id of element to search.
     * @return if exists, T elem with id past.
     */
    T find(ID id);
    
    /**
     * Insert or update T elem.
     * @param elem: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    boolean save(T elem);
    
    /**
     * Delete T element with id passed.
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    boolean delete(ID id);
    
}
