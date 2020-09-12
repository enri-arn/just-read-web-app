package dao;

import bean.Rating;
import bean.User;
import database.DatabaseContract;
import database.DatabaseUtils;
import domain.data.ReviewsData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class RatingRepository extends AbstractRepository<Rating, Integer> {

    /**
     * Finds all the elements of type Rating in the database.
     *
     * @return list of instances of Rating.
     */
    @Override
    public List<Rating> findAll() {
        ArrayList<Rating> ratings = new ArrayList<>();
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table));
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Rating rating = new Rating();
                rating.setId(this.resultSet.getInt(DatabaseContract.RatingTable.COL_ID_RATING));
                BookRepository bookRepository = new BookRepository();
                rating.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                rating.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_USER)));
                rating.setLittleHeart(this.resultSet.getBoolean(DatabaseContract.RatingTable.COL_LITTLE_HEART));
                rating.setRate(this.resultSet.getFloat(DatabaseContract.RatingTable.COL_RATE));
                rating.setComment(this.resultSet.getString(DatabaseContract.RatingTable.COL_COMMENT));
                ratings.add(rating);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return ratings;
    }

    /**
     * Find the Rating elem with id passed.
     *
     * @param email: email of the user.
     * @param isbn: isbn of the book.
     * @return if exists, Rating elem with id past.
     */
    public Rating findByUserBook(String email, String isbn) {
        Rating rating = new Rating();
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.RatingTable.COL_BOOK + "='" + isbn + "'"
                    + " AND " + DatabaseContract.RatingTable.COL_USER + "="
                    + "'" + email + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                rating.setId(this.resultSet.getInt(DatabaseContract.RatingTable.COL_ID_RATING));
                BookRepository bookRepository = new BookRepository();
                rating.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                rating.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_USER)));
                rating.setLittleHeart(this.resultSet.getBoolean(DatabaseContract.RatingTable.COL_LITTLE_HEART));
                rating.setRate(this.resultSet.getFloat(DatabaseContract.RatingTable.COL_RATE));
                rating.setComment(this.resultSet.getString(DatabaseContract.RatingTable.COL_COMMENT));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return rating;
    }

    /**
     * Insert or update Rating elem.
     *
     * @param rate: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Rating rate) {
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RatingTable.COL_BOOK,
                DatabaseContract.RatingTable.COL_USER,
                DatabaseContract.RatingTable.COL_LITTLE_HEART,
                DatabaseContract.RatingTable.COL_RATE,
                DatabaseContract.RatingTable.COL_COMMENT
            };
            int littleHeart = rate.isLittleHeart() ? 1 : 0;
            String[] values = {
                rate.getBook().getIsbn(), rate.getUser().getEmail(),
                String.valueOf(littleHeart),
                String.valueOf(rate.getRate()), rate.getComment()
            };
            connect(DatabaseUtils.insertUpdateQuery(table, fields, values));
            this.statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

    /**
     * Delete Rating element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String field = DatabaseContract.RatingTable.COL_ID_RATING;
            connect(DatabaseUtils.deleteQuery(table, field, String.valueOf(id)));
            this.statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

    /**
     * Find all the ratings of the book selected.
     *
     * @param isbn: isn of the book.
     * @return list of instances of Rating.
     */
    public List<Rating> findByBook(String isbn) {
        ArrayList<Rating> ratings = new ArrayList<>();
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.RatingTable.TABLE_NAME + "."
                    + DatabaseContract.RatingTable.COL_BOOK + "='" + isbn + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Rating rating = new Rating();
                rating.setId(this.resultSet.getInt(DatabaseContract.RatingTable.COL_ID_RATING));
                BookRepository bookRepository = new BookRepository();
                rating.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                rating.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_USER)));
                rating.setLittleHeart(this.resultSet.getBoolean(DatabaseContract.RatingTable.COL_LITTLE_HEART));
                rating.setRate(this.resultSet.getFloat(DatabaseContract.RatingTable.COL_RATE));
                rating.setComment(this.resultSet.getString(DatabaseContract.RatingTable.COL_COMMENT));
                ratings.add(rating);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return ratings;
    }

    /**
     * Save or update a favourite book updating or inserting in rating table the
     * little heart column.
     *
     * @param rate: rate
     * @return true if updating was succesful, false otherwise.
     */
    public boolean saveFavourite(Rating rate) {
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RatingTable.COL_ID_RATING,
                DatabaseContract.RatingTable.COL_BOOK,
                DatabaseContract.RatingTable.COL_USER,
                DatabaseContract.RatingTable.COL_LITTLE_HEART,
                DatabaseContract.RatingTable.COL_RATE,
                DatabaseContract.RatingTable.COL_COMMENT
            };
            String[] values = {
                String.valueOf(rate.getId()), rate.getBook().getIsbn(), rate.getUser().getEmail(),
                String.valueOf(rate.isLittleHeart() ? 1 : 0),
                String.valueOf(rate.getRate()), rate.getComment()
            };
            System.out.println(DatabaseUtils.insertUpdateQuery(table, fields, values));
            connect(DatabaseUtils.insertUpdateQuery(table, fields, values));
            this.statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

    /**
     * Get the average of the rate of the selected book.
     *
     * @param isbn: isbn of the book.
     * @return a float rapresent the rate of the book, the default value is '0'.
     */
    public float getRateOfBook(String isbn) {
        float rate = 0f;
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String[] fields = {
                "AVG(" + DatabaseContract.RatingTable.COL_RATE + ") AS RATE"
            };
            String query = DatabaseUtils.selectQuery(table, fields) + " WHERE "
                    + DatabaseContract.RatingTable.TABLE_NAME + "."
                    + DatabaseContract.RatingTable.COL_BOOK + "='" + isbn + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                rate = this.resultSet.getFloat(DatabaseContract.RatingTable.COL_RATE);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 0f;
        } finally {
            disconnect();
        }
        return rate;
    }

    /**
     * Count number of one star from rating of the selected book.
     *
     * @param isbn: isbn of the book.
     * @param start: inferior limit.
     * @param end: superior limit.
     * @return an integer rapresent the number of star.
     */
    public int countOneStar(String isbn, int start, int end) {
        int count = 0;
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String[] fields = {
                "COUNT(" + DatabaseContract.RatingTable.COL_RATE + ") AS RATE"
            };
            String query = DatabaseUtils.selectQuery(table, fields) + " WHERE "
                    + DatabaseContract.RatingTable.TABLE_NAME + "."
                    + DatabaseContract.RatingTable.COL_BOOK + "='" + isbn + "' AND "
                    + DatabaseContract.RatingTable.COL_RATE + " >=" + start + " AND "
                    + DatabaseContract.RatingTable.COL_RATE + " <= " + end;
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                count = this.resultSet.getInt(DatabaseContract.RatingTable.COL_RATE);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } finally {
            disconnect();
        }
        return count;
    }

    /**
     * Get all the reviews of a selected book
     * 
     * @param isbn of the book.
     * @return a list of instance of ReviewsData.(More easy to manipulate with ObjectMapper)
     */
    public List<ReviewsData> getReviews(String isbn) {
        ArrayList<ReviewsData> reviews = new ArrayList<>();
        UserRepository userRepository = new UserRepository();
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RatingTable.COL_USER,
                DatabaseContract.RatingTable.COL_COMMENT
            };
            String query = DatabaseUtils.selectQuery(table, fields)
                    + " WHERE " + DatabaseContract.RatingTable.COL_BOOK
                    + "='" + isbn + "' AND " + DatabaseContract.RatingTable.COL_COMMENT
                    + " <> 'null'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                ReviewsData review = new ReviewsData();
                User tmp = userRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_USER));
                review.setName(tmp.getName());
                review.setSurname(tmp.getSurname());
                review.setProfileImage(tmp.getProfileImage());
                review.setComment(this.resultSet.getString(DatabaseContract.RatingTable.COL_COMMENT));
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return reviews;
    }

    /**
     * Find a Rating by id.
     * 
     * @param id of the rating
     * @return an instance of Rating.
     */
    @Override
    public Rating find(Integer id) {
        Rating rating = new Rating();
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE '"
                    + DatabaseContract.RatingTable.COL_ID_RATING + "=" + id;
            connect(query);
            this.resultSet = this.statement.executeQuery();
            if (this.resultSet != null) {
                rating.setId(this.resultSet.getInt(DatabaseContract.RatingTable.COL_ID_RATING));
                BookRepository bookRepository = new BookRepository();
                rating.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                rating.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.RatingTable.COL_USER)));
                rating.setLittleHeart(this.resultSet.getBoolean(DatabaseContract.RatingTable.COL_LITTLE_HEART));
                rating.setRate(this.resultSet.getFloat(DatabaseContract.RatingTable.COL_RATE));
                rating.setComment(this.resultSet.getString(DatabaseContract.RatingTable.COL_COMMENT));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return rating;
    }

    /**
     * Get the number of voter of the book.
     * 
     * @param isbn of the book.
     * @return an int rapresent the number of voters.
     */
    public int getNumVoters(String isbn) {
        int count = 0;
        try {
            String table = DatabaseContract.RatingTable.TABLE_NAME;
            String[] fields = {
                "COUNT(" + DatabaseContract.RatingTable.COL_RATE + ") AS RATE"
            };
            String query = DatabaseUtils.selectQuery(table, fields) + " WHERE "
                    + DatabaseContract.RatingTable.COL_BOOK + "='" + isbn + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                count = this.resultSet.getInt(DatabaseContract.RatingTable.COL_RATE);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } finally {
            disconnect();
        }
        return count;
    }

}
