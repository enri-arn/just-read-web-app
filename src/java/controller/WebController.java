package controller;

import bean.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
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
@WebServlet(name = "WebController", urlPatterns = {"/app/*"})
public class WebController extends HttpServlet {

    /**
     * Regex that allows to all the pages to redirect in main page(user.jsp)
     */
    private final String MAIN_REGEX_URL = "^/JustRead/app/(|"
            + "(catalog|borrowing|history|favourites|faq|profile|contacts|about|application)|"
            + "((catalog|borrowing|history|favourites)/\\w{3,15}/\\d{13})|"
            + "(catalog/\\w{3,15}))$";

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
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.getLevel() == 1) {
                request.setAttribute("user-level", "admin");
            } else {
                request.setAttribute("user-level", "user");
            }
            request.setAttribute("user-name", user.getName());
        }

        String pathInfo = request.getRequestURI();

        if (pathInfo.matches("^/JustRead/app/login$")) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/view/login.jsp");
            rd.forward(request, response);
        } else if (pathInfo.matches("^/JustRead/app/register$")) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/view/register.jsp");
            rd.forward(request, response);
        } else if (pathInfo.matches("^/JustRead/app/admin$")) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/view/admin.jsp");
            rd.forward(request, response);
        } else if (pathInfo.matches(MAIN_REGEX_URL)) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/view/user.jsp");
            rd.forward(request, response);
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
        return "Web controller";
    }

}
