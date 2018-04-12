package ua.kh.kryvko.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Filter implements javax.servlet.Filter {

    private FilterConfig config;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse){
            ServletContext servletContext = config.getServletContext();
            if ((boolean) servletContext.getAttribute("isLoading")) {
                ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() + "/loading");
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

}
