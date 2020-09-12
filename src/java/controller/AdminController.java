package controller;

import bean.Book;
import bean.Publisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.PublisherRepository;
import domain.service.BookService;
import domain.service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Year;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
@WebServlet(name = "AdminController", urlPatterns = {"/api/admin/*"})
public class AdminController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getRequestURI();

        ObjectMapper mapper = new ObjectMapper();
        UserService userService = UserService.getInstance();
        BookService bookService = BookService.getInstance();
        PrintWriter out = response.getWriter();

        if (pathInfo.matches("^/JustRead/api/admin$")) {
            String stat1 = mapper.writeValueAsString(userService.calculateActiveUsers());
            String stat2 = mapper.writeValueAsString(userService.calculateActiveBorrowing());
            String stat3 = mapper.writeValueAsString(userService.calculateBookUndelivered());
            String stat4 = mapper.writeValueAsString(userService.calculateBorrowingAVG());
            String json = "[" + stat1 + "," + stat2 + "," + stat3 + "," + stat4 + "]";
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/active$")) {
            String json = mapper.writeValueAsString(bookService.getAllActiveBorrowing());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/expired$")) {
            String json = mapper.writeValueAsString(bookService.getAllExpiredBorrowing());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/archive$")) {
            String json = mapper.writeValueAsString(bookService.getAllArchivedBorrowing());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/newbook$")) {
            String publishers = mapper.writeValueAsString(bookService.getAllPublishers());
            String categories = mapper.writeValueAsString(bookService.getAllCategories());
            String json = "[" + publishers + "," + categories + "]";
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/allusers$")) {
            String json = mapper.writeValueAsString(userService.getAllUsers());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/allbooks$")) {
            String json = mapper.writeValueAsString(bookService.getAllBooksInfo());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/allauthors$")) {
            String json = mapper.writeValueAsString(bookService.getAllAuthors());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/allcategories$")) {
            String json = mapper.writeValueAsString(bookService.getAllCategories());
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getRequestURI();

        BookService bookService = BookService.getInstance();
        UserService userService = UserService.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        PublisherRepository publisherRepository = new PublisherRepository();

        if (pathInfo.matches("^/JustRead/api/admin/newbook$")) {
            String ISBN = request.getParameter("ISBN");
            String title = request.getParameter("title");
            String cover = request.getParameter("cover");
            String[] authors = request.getParameterValues("authors[]");
            
            String language = request.getParameter("language");
            String[] categories = request.getParameterValues("categories[]");
            
            String publisher = request.getParameter("publisher");
            String plot = request.getParameter("plot");
            int numpages = Integer.parseInt(request.getParameter("numpages"));
            Year year = Year.parse(request.getParameter("year"));
            System.out.println(ISBN + " " + title+ " cover " + cover+ " autori " + Arrays.toString(authors)+ " " + language+ " " + Arrays.toString(categories)+ " " + publisher+ " " + plot+ " " + numpages+ " " + year.toString());
            if (bookService.addBook(ISBN, title, categories, publisher, language, plot, cover, numpages, year, authors)) {
                response.getWriter().print("true");
            } else {
                response.getWriter().print("false");
            }
            out.close();
        } else if (pathInfo.matches("^/JustRead/api/admin/newcategory$")) {
            String name = request.getParameter("category");
            String icon = request.getParameter("icon");
            String color = request.getParameter("color");
            if (bookService.addCategory(name, icon, color)) {
                out.println("Category has been correctly added to the database.");
            } else {
                out.println("Error");
            }
            out.close();
        } else if (pathInfo.matches("^/JustRead/api/admin/newauthor$")) {
            String[] author = request.getParameterValues("author[]");
            if (bookService.addAuthor(author[0], author[1])) {
                out.println("Author has been correctly added to the database.");
            } else {
                out.println("Error");
            }
            out.close();
        } else if (pathInfo.matches("^/JustRead/api/admin/user$")) {
            String email = request.getParameter("email");
            String json = mapper.writeValueAsString(userService.getUser(email));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/admin/modifybook$")) {
            Book book = new Book();
            
            String ISBN = request.getParameter("ISBN");
            String title = request.getParameter("title");
            String cover = request.getParameter("cover");
            String language = request.getParameter("language");
            String pub = request.getParameter("publisher");
            
            Publisher publisher = publisherRepository.findByName(pub);
            if (publisher == null) {
                publisher = new Publisher();
                publisher.setName(pub);
                publisherRepository.save(publisher);
                publisher = publisherRepository.findByName(pub);
            }
            String plot = request.getParameter("plot");
            int numpages = Integer.parseInt(request.getParameter("numpages"));
            Year year = Year.parse(request.getParameter("year"));
            
            book.setIsbn(ISBN);
            book.setTitle(title);
            book.setCover(cover);
            book.setLanguage(language);
            book.setPages(numpages);
            book.setPublisher(publisher);
            book.setPlot(plot);
            book.setYearsOfPublication(year);
            book.setUnavailable(false);
            
            if (bookService.updateBook(book)) {
                out.println("Book has been correctly update to the database.");
            } else {
                out.println("Error");
            }
        }else if (pathInfo.matches("^/JustRead/api/admin/deletebook$")) {
            String ISBN = request.getParameter("ISBN");
            if (bookService.deleteBook(ISBN)) {
                out.println("Book has been correctly delete from the database.");
            } else {
                out.println("The book cannot be removed.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }

    /**
     * Handles the HTTP <code>DELETE</code> method.
     * 
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getRequestURI();
        
        BookService bookService = BookService.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        
        if (pathInfo.matches("^/JustRead/api/admin/deletebook/(\\d{13})?$")) {
            resp.setContentType("application/json");
            if (bookService.deleteBook(getISBN(pathInfo, "^/JustRead/api/admin/deletebook/"))) {
                resp.getWriter().print("true");
            } else {
                resp.getWriter().print("false");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }

    /**
     * Handles the HTTP <code>PUT</code> method.
     * 
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getRequestURI();

        BookService bookService = BookService.getInstance();
        UserService userService = UserService.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        
        System.out.println("put");

        if (pathInfo.matches("^/JustRead/api/admin/promoteuser/\\S+@\\S+\\.\\S+$")) {
            String email = getUserRole(pathInfo, "^/JustRead/api/admin/promoteuser/");
            String json = mapper.writeValueAsString(userService.changeRole(email));
            out.print(json);
            out.close();
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }
    
    /**
     * Get the isbn of the book from the url path.
     * 
     * @param path: url path
     * @return a string rapresent the isbn of the book.
     */
    private String getUserRole(String path, String regex){
        String[] user = path.split(regex);
        System.out.println(Arrays.toString(user));
        return user[1];
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Admin controller";
    }

    private String getISBN(String path, String regex) {
        String[] book = path.split(regex);
        System.out.println(Arrays.toString(book));
        return book[1];
    }

}
