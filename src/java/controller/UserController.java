package controller;

import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.service.NotificationService;
import domain.service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
@WebServlet(name = "UserController", urlPatterns = {"/api/user/*"})
@MultipartConfig
public class UserController extends HttpServlet {

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
        ObjectMapper mapper = new ObjectMapper();
        UserService userService = UserService.getInstance();
        PrintWriter out = response.getWriter();
        User user = null;
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        }

        if (pathInfo.matches("^/JustRead/api/user/modify$") && user != null) {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String address = request.getParameter("address");
            System.out.println(name + " ---- " + surname + " ---- " + address);
            /*Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = filePart.getInputStream();
            System.out.println(fileName);
            BufferedImage image = ImageIO.read(fileContent);*/
            
            String json = mapper.writeValueAsString(userService.modifyProfile(user, name, surname, address));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
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
        String pathInfo = request.getRequestURI();
        ObjectMapper mapper = new ObjectMapper();
        NotificationService notificationsService = NotificationService.getInstance();
        PrintWriter out = response.getWriter();
        User user = null;
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        }

        if (pathInfo.matches("^/JustRead/api/user/notifications$") && user != null) {
            String json = mapper.writeValueAsString(notificationsService.getNotification(user.getEmail()));
            response.setContentType("application/json");
            out.print(json);
            out.flush();
        } else if (pathInfo.matches("^/JustRead/api/user/info$") && user != null) {
            String json = mapper.writeValueAsString(notificationsService.getUserData(user.getEmail()));
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
        return "User controller";
    }

}
