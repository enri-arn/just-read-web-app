package controller;

import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.service.BookService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
@WebServlet(name = "FavouriteController", urlPatterns = {"/api/favourites/*"})
public class FavouriteController extends HttpServlet {

    /**
     * Handles the HTTP <code>DELETE</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bookService = BookService.getInstance();
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/favourites/(\\d{13})?$")) {
            System.out.println(" ............... sono nella delete ..... ");
            String book = pathInfo.substring(25);
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            response.setContentType("application/json");
            if (bookService.updateFavourite(book, user.getEmail(), 0)) {
                response.getWriter().print("true");
            } else {
                response.getWriter().print("false");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }

    /**
     * Handles the HTTP <code>PUT</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        BookService bookService = new BookService();
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/favourites/(\\d{13})?$")) {
            String book = pathInfo.substring(25);
            HttpSession session = request.getSession();
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                response.setContentType("application/json");
                if (bookService.updateFavourite(book, user.getEmail(), 1)) {
                    response.getWriter().print("true");
                }
            } else {
                response.getWriter().print("false");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }

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
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        BookService bookService = new BookService();
        HttpSession session = request.getSession();
        String user = null;
        if (session != null && session.getAttribute("user") != null) {
            User u = (User) session.getAttribute("user");
            user = u.getEmail();
        }
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/favourites/(\\d{13})?$")) {
            String bookPath = pathInfo.substring(25);
            String json = mapper.writeValueAsString(bookService.getBookInfo(user, bookPath));
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
        ObjectMapper mapper = new ObjectMapper();
        BookService bookService = new BookService();
        PrintWriter out = response.getWriter();
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/favourites$")) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            String json = mapper.writeValueAsString(bookService.getFavouriteByUser(user.getEmail()));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/favourites/(\\d{13})$")) {
            String bookPath = getBook(pathInfo, "/JustRead/api/favourites/");
            String info = request.getParameter("info");
            String json = null;
            if (info.equals("categories")) {
                json = mapper.writeValueAsString(bookService.getCategoryOfBook(bookPath));
            } else if (info.equals("reviews")) {
                json = mapper.writeValueAsString(bookService.getReviewsOfBook(bookPath));
            }
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verify your URL and refresh the page");
        }
    }

        /**
     * Get the isbn of the book from the url path.
     *
     * @param path: url path
     * @return a string rapresent the isbn of the book.
     */
    private String getBook(String path, String regex) {
        String[] book = path.split(regex);
        System.out.println(Arrays.toString(book));
        return book[1];
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Favourite controller";
    }

}
