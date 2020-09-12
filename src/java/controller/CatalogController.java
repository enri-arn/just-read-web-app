package controller;

import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.service.BookService;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "CatalogController", urlPatterns = {"/api/catalog/*"})
public class CatalogController extends HttpServlet {

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
        BookService bookService = new BookService();
        PrintWriter out = response.getWriter();
        String pathInfo = request.getRequestURI();
        HttpSession session = request.getSession();
        String user = null;
        if (session != null && session.getAttribute("user") != null) {
            User u = (User) session.getAttribute("user");
            user = u.getEmail();
        } 

        if (pathInfo.matches("^/JustRead/api/catalog/(\\w{3,15})/(\\d{13})$")) {
            String bookPath = getBook(pathInfo);
            String json = mapper.writeValueAsString(bookService.getBookInfo(user, bookPath));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/catalog/(\\w{3,15})$")) {
            String pathGenre = getCategory(pathInfo);
            String json = mapper.writeValueAsString(bookService.getBooksByCategory(pathGenre));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/catalog$")) {
            String json = mapper.writeValueAsString(bookService.getAllCategories());
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
    private String getBook(String path) {
        String[] book = path.split("^/JustRead/api/catalog/(\\w{3,15})/");
        return book[1];
    }

    /**
     * Get the category name from url path
     *
     * @param path: url path.
     * @return a string rapresent the name of the category
     */
    private String getCategory(String path) {
        String[] category = path.split("^/JustRead/api/catalog/");
        return category[1];
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        BookService bookService = new BookService();
        PrintWriter out = response.getWriter();
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/catalog/addreview/(\\d{13})$")) {
            HttpSession session = request.getSession();
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                String bookPath = getBook(pathInfo);
                String comment = request.getParameter("review");
                String rateString = request.getParameter("rate");
                float rate = Float.valueOf(rateString);
                String littleHeart = request.getParameter("littleHeart");
                boolean favourite = !littleHeart.equals("false");
                if (bookService.addRate(user.getEmail(), bookPath, comment, rate, favourite)) {
                    response.getWriter().print("true");
                }
            } else {
                response.getWriter().print("false");
            }
        } else if (pathInfo.matches("^/JustRead/api/catalog/(\\w{3,15})/(\\d{13})$")) {
            String bookPath = getBook(pathInfo);
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Catalog controller";
    }

}
