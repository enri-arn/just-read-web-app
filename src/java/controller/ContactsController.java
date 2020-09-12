package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.service.LibraryContactService;
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
@WebServlet(name = "ContactsController", urlPatterns = {"/api/contacts/*"})
public class ContactsController extends HttpServlet {

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
        LibraryContactService contactService = LibraryContactService.getInstance();
        
        PrintWriter out = response.getWriter();

        String pathInfo = request.getRequestURI();
        if (pathInfo.matches("^/JustRead/api/contacts/rule$")) {
            String json = mapper.writeValueAsString(contactService.getContacts(1));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/contacts$")) {
            String bh[] = contactService.gerBuisnessHour(1).split(",");
            String json = mapper.writeValueAsString(bh);
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
        return "Contacts controller";
    }

}
