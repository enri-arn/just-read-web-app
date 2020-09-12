package database;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class DatabaseContract {
    
    public static class UserTable{
        public static final String TABLE_NAME       = "USER";
        
        public static final String COL_EMAIL        = "EMAIL";
        public static final String COL_TOKEN        = "TOKEN";
        public static final String COL_NAME         = "NAME";
        public static final String COL_SURNAME      = "SURNAME";
        public static final String COL_LEVEL        = "LEVEL";
        public static final String COL_PROFILE_IMG  = "PROFILE_IMAGE";
        public static final String COL_ADDRESS      = "ADDRESS";
    }
    
    public static class BookTable{
        public static final String TABLE_NAME       = "BOOK";
        
        public static final String COL_ISBN         = "ISBN";
        public static final String COL_PUBLISHER    = "PUBLISHER";
        public static final String COL_LANGUAGE     = "LANGUAGE";
        public static final String COL_TITLE        = "TITLE";
        public static final String COL_PLOT         = "PLOT";
        public static final String COL_COVER_LD     = "COVER_LD";
        public static final String COL_COVER_HD     = "COVER_HD";
        public static final String COL_PAGES        = "PAGES";
        public static final String COL_PUBLICATION  = "YEARS_OF_PUBLICATION";
        public static final String COL_UNAVAILABLE  = "UNAVAILABLE";
    }
    
    public static class BookCategoryTable{
        public static final String TABLE_NAME       = "BOOK_CATEGORY";
        
        public static final String COL_ID_BOOK      = "ID_BOOK";
        public static final String COL_ID_CATEGORY  = "ID_CATEGORY";
    }
    
    public static class AuthorTable{
        public static final String TABLE_NAME       = "AUTHOR";
        
        public static final String COL_ID_AUTHOR    = "ID_AUTHOR";
        public static final String COL_NAME         = "NAME";
        public static final String COL_SURNAME      = "SURNAME";
    }
    
    public static class BookAuthorTable{
        public static final String TABLE_NAME       = "BOOK_AUTHOR";
        
        public static final String COL_ID_AUTHOR    = "ID_AUTHOR";
        public static final String COL_ID_BOOK      = "ID_BOOK";
    }
    
    public static class BorrowingTable{
        public static final String TABLE_NAME       = "BORROWING";
        
        public static final String COL_ID_BORROW    = "ID_BORROW";
        public static final String COL_BOOK         = "BOOK";
        public static final String COL_USER         = "USER";
        public static final String COL_START_DATE   = "START_DATE";
        public static final String COL_END_DATE     = "END_DATE";
        public static final String COL_RENEWAL      = "RENEWAL";
        public static final String COL_IS_DELIVER   = "IS_DELIVER";
    }
    
    public static class CategoryTable{
        public static final String TABLE_NAME       = "CATEGORY";
        
        public static final String COL_ID_CATEGORY  = "ID_CATEGORY";
        public static final String COL_NAME         = "NAME_CATEGORY";
        public static final String COL_ICON         = "ICON";
    }
    
    public static class PublisherTable{
        public static final String TABLE_NAME       = "PUBLISHER";
        
        public static final String COL_ID_PUBLISHER = "ID_PUBLISHER";
        public static final String COL_NAME         = "NAME_PUBLISHER";
    }
    
    public static class RatingTable{
        public static final String TABLE_NAME       = "RATING";
        
        public static final String COL_ID_RATING    = "ID_RATING";
        public static final String COL_BOOK         = "BOOK";
        public static final String COL_USER         = "USER";
        public static final String COL_LITTLE_HEART = "LITTLE_HEART";
        public static final String COL_RATE         = "RATE";
        public static final String COL_COMMENT      = "COMMENT";
    }
    
    public static class RuleTable{
        public static final String TABLE_NAME       = "RULE";
        
        public static final String COL_ID_RULE      = "ID_RULE";
        public static final String COL_BORROW_DAYS  = "BORROW_DAYS";
        public static final String COL_ADDRESS      = "ADDRESS_LIBRARY";
        public static final String COL_PHONE_NUMBER = "LIBRARY_PHONE_NUMBER";
        public static final String COL_NAME         = "NAME_LIBRARY";
        public static final String COL_BUSINESS_HR  = "BUSINESS_HOURS";
        public static final String COL_MAX_RENEWAL  = "MAX_RENEWAL";
        public static final String COL_LOW_RENEWAL  = "LOWER_RENEWAL_DAYS";
        public static final String COL_MAX_BORROWING= "MAX_BORROWING";
    }
}
