package filters;

/**
 * Created by Aleks on 03.08.2017.
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class Filter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/login";
        String requestedUri = request.getRequestURI();

        boolean loggedIn = session != null && session.getAttribute("username") != null;
        boolean loginRequest = requestedUri.equals(loginURI);

        if (loggedIn || loginRequest) {
            filterChain.doFilter(request, response);
        }else if (requestedUri.matches(".*(css|jpg|png|gif|js)")){
            filterChain.doFilter(request, response);
        }else {
            response.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {

    }
}
