package controller;

import bean.User;
import domain.service.AuthenticationService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "AuthenticationController", urlPatterns = {"/api/login/*", "/api/register/*", "/api/logout/*"})
public class AuthenticationController extends HttpServlet {

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
        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/logout$")) {
            AuthenticationService service = AuthenticationService.getInstance();
            HttpSession session = request.getSession();
            if (session != null && session.getAttribute("user") != null) {
                session.invalidate();
                response.getWriter().print("true");
            }
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

        if (pathInfo.matches("^/JustRead/api/login$")) {
            AuthenticationService service = AuthenticationService.getInstance();
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            System.out.println("email= " + email + " password= " + password);
            User user = null;
            try {
                user = service.authenticate(email, password);
            } catch (Exception ex) {
                Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.getWriter().print("true");
            } else {
                request.setAttribute("user-name", "nessuno");
                response.getWriter().print("false");
            }
        } else if (pathInfo.matches("^/JustRead/api/register$")) {
            AuthenticationService service = AuthenticationService.getInstance();
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            System.out.println("email= " + email + " password= " + password);
            try {
                if (service.registerUser(email, password, name, surname)) {
                    response.getWriter().print("true");
                } else {
                    response.getWriter().print("false");
                }
            } catch (Exception ex) {
                response.getWriter().print("false");
                Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        return "Short description";
    }

}
