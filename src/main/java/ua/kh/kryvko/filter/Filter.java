package ua.kh.kryvko.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Filter implements javax.servlet.Filter {

    private FilterConfig config;
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        ServletContext servletContext = config.getServletContext();
        if((boolean) servletContext.getAttribute("isLoading")) {
            ((HttpServletResponse) resp).sendRedirect("/check-my-domain/loading");
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

}
