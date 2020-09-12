package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final String[] protectedPaths = {
        "borrowing",
        "favourites",
        "history", 
        "profile",
        "admin"
    };

    private final static String CTX_PATH = "/JustRead/";
    private final static String APP_PATH = "app";
    private final static String API_PATH = "api";

    private final static String regex = CTX_PATH + "(" + API_PATH + "|" + APP_PATH + ")/";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    /**
     * Check the url path. If path contains a protected area that is
     * not allowed to the user or a user try to enter in a restricted
     * area Filter redirects the user to the login page.
     * If request is allowed Filter do nothing on it.
     * 
     * @param req servletrequest
     * @param res servlet response
     * @param chain  is an object provided by the servlet container to the developer 
     * giving a view into the invocation chain of a filtered request for a resource. 
     * Filters use the FilterChain to invoke the next filter in the chain, or if 
     * the calling filter is the last filter in the chain, to invoke the resource 
     * at the end of the chain.
     * 
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String requestPath = request.getRequestURI();
        if (requestPath.equals(CTX_PATH)) {
            response.sendRedirect(request.getContextPath() + "/" + APP_PATH + "/");
        } else if (needsAuthentication(requestPath) && (session == null || session.getAttribute("user") == null)) {
            response.sendRedirect(request.getContextPath() + "/" + APP_PATH + "/login");
        } else {
            chain.doFilter(req, res);
        }

    }

    @Override
    public void destroy() {
        //
    }

    /**
     * check the url passing whit regex protectedPaths.
     * 
     * @param url: String to validate.
     * @return true if path is correct/possible, false otherwise.
     */
    private boolean needsAuthentication(String url) {
        String validation = regex + "(";
        for (int i = 0; i < protectedPaths.length; i++) {
            validation += protectedPaths[i];
            if (i < protectedPaths.length - 1) {
                validation += "|";
            }
        }
        validation += ")";
        return url.matches(validation);
    }
}
