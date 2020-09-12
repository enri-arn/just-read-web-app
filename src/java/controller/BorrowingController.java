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
@WebServlet(name = "BookController", urlPatterns = {"/api/history/*", "/api/borrowing/*"})
public class BorrowingController extends HttpServlet {

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
        if (pathInfo.matches("^/JustRead/api/borrowing/(\\d{13})?$")) {
            String book = getBook(pathInfo, "^/JustRead/api/borrowing/");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            response.setContentType("application/json");
            if (bookService.deliverBorrowing(user.getEmail(), book)) {
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
        BookService bookService = BookService.getInstance();
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/borrowing/increase/(\\d{13})?$")) {
            String book = getBook(pathInfo, "^/JustRead/api/borrowing/increase/");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            response.setContentType("application/json");
            if (bookService.increaseBorrowing(user.getEmail(), book)) {
                response.getWriter().print("true");
            } else {
                response.getWriter().print("false");
            }
        } else if (pathInfo.matches("^/JustRead/api/borrowing/book/(\\d{13})?$")) {
            String book = getBook(pathInfo, "/JustRead/api/borrowing/book/");
            HttpSession session = request.getSession();
            response.setContentType("application/json");
            if (session == null || session.getAttribute("user") == null) {
                response.getWriter().print("false");
            } else {
                User user = (User) session.getAttribute("user");
                if (bookService.addBorrowing(user.getEmail(), book, false)) {
                    response.getWriter().print("true");
                    System.out.println("book true borrowing");
                } else {
                    response.getWriter().print("false");
                    System.out.println("book false borrowing");
                }
            }
        } else if (pathInfo.matches("^/JustRead/api/borrowing/send/(\\d{13})?$")) {
            String book = getBook(pathInfo, "/JustRead/api/borrowing/send/");
            HttpSession session = request.getSession();
            response.setContentType("application/json");
            if (session == null || session.getAttribute("user") == null) {
                response.getWriter().print("false");
            } else {
                User user = (User) session.getAttribute("user");
                if (bookService.addBorrowing(user.getEmail(), book, true)) {
                    response.getWriter().print("true");
                } else {
                    response.getWriter().print("false");
                }
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
        BookService bookService = BookService.getInstance();
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String user = null;
        if (session != null && session.getAttribute("user") != null) {
            User u = (User) session.getAttribute("user");
            user = u.getEmail();
        }
        System.out.println("user in get borrowing controller: " + user);
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/borrowing/(\\d{13})?$")) {
            String bookPath = getBook(pathInfo, "^/JustRead/api/borrowing/");
            String json = mapper.writeValueAsString(bookService.getBookInfo(user, bookPath));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/history/(\\d{13})?$")) {
            String bookPath = getBook(pathInfo, "^/JustRead/api/history/");
            String json = mapper.writeValueAsString(bookService.getBookInfo(user, bookPath));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/borrowing$")) {
            String jsonInString = mapper.writeValueAsString(bookService.getBorrowingByUser(user));
            System.out.println(jsonInString);
            response.setContentType("application/json");
            out.print(jsonInString);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/history$")) {
            String jsonInString = mapper.writeValueAsString(bookService.getHistoryByUser(user));
            System.out.println(jsonInString);
            response.setContentType("application/json");
            out.print(jsonInString);
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
        BookService bookService = BookService.getInstance();
        PrintWriter out = response.getWriter();
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/borrowing/(\\d{13})$")) {
            String bookPath = getBook(pathInfo, "/JustRead/api/borrowing/");
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
        } else if (pathInfo.matches("^/JustRead/api/history/(\\d{13})$")) {
            String bookPath = getBook(pathInfo, "/JustRead/api/history/");
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
        return book[1];
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Borrowing controller";
    }
}
