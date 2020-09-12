package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.service.BookService;
import domain.service.SearchableService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
@WebServlet(name = "SearchableController", urlPatterns = {"/api/search", "/api/search/active", "/api/search/expired","/api/search/archived", "/api/search/user", "/api/search/book"})
public class SearchableController extends HttpServlet {

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
        SearchableService searchableService = SearchableService.getInstance();
        PrintWriter out = response.getWriter();
        String pathInfo = request.getRequestURI();
        
        if (pathInfo.matches("^/JustRead/api/search/active")) {
            String query = request.getParameter("search");
            System.out.println("Sono in search con search act = " + query);
            String json = mapper.writeValueAsString(searchableService.findActiveBorrowing(query));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        }else if (pathInfo.matches("^/JustRead/api/search/expired")) {
            String query = request.getParameter("search");
            System.out.println("Sono in search con search exp = " + query);
            String json = mapper.writeValueAsString(searchableService.findExpiredBorrowing(query));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        }else if (pathInfo.matches("^/JustRead/api/search/archived")) {
            String query = request.getParameter("search");
            System.out.println("Sono in search con search arch = " + query);
            String json = mapper.writeValueAsString(searchableService.findArchivedBorrowing(query));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        }else if (pathInfo.matches("^/JustRead/api/search/user")) {
            String query = request.getParameter("search");
            System.out.println("Sono in search con search user = " + query);
            String json = mapper.writeValueAsString(searchableService.findUser(query));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        }else if (pathInfo.matches("^/JustRead/api/search/book")) {
            String query = request.getParameter("search");
            System.out.println("Sono in search con search book = " + query);
            String json = mapper.writeValueAsString(searchableService.findBook(query));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        }else if (pathInfo.matches("^/JustRead/api/search")) {
            String query = request.getParameter("search");
            String json = mapper.writeValueAsString(searchableService.findBookBy(query));
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
        return "Searchable Controller";
    }

}
