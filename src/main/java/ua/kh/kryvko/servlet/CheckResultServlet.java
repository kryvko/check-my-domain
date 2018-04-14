package ua.kh.kryvko.servlet;

import ua.kh.kryvko.logic.BypassDomain;
import ua.kh.kryvko.name.RequestParameterNames;
import ua.kh.kryvko.name.ServletContextNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class CheckResultServlet extends HttpServlet {

    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.servletContext = config.getServletContext();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String domain = request.getParameter(RequestParameterNames.DOMAIN);
        String startUrl = request.getParameter(RequestParameterNames.START_URL);

        if(domain == null || startUrl == null) {
            response.setStatus(400);
            return;
        }

        this.servletContext.setAttribute(ServletContextNames.IS_LOADING, true);
        this.servletContext.setAttribute(ServletContextNames.DOMAIN, domain);

        try {
            BypassDomain bypassDomain = new BypassDomain(domain, startUrl);

            bypassDomain.startBypass();

            this.servletContext.setAttribute(ServletContextNames.UNIQUE_LINKS_COUNT, bypassDomain.getUniqueUrls().size());
            this.servletContext.setAttribute(ServletContextNames.BROKEN_LINKS, bypassDomain.getBrokenUrls());
            this.servletContext.setAttribute(ServletContextNames.LAST_DOMAIN, this.servletContext.getAttribute(ServletContextNames.DOMAIN));
        } catch (Error error) {
            this.servletContext.setAttribute(ServletContextNames.HAS_ERROR, true);
        } finally {
            this.servletContext.setAttribute(ServletContextNames.IS_LOADING, false);
        }
        response.sendRedirect(request.getContextPath() + "/lastResult");
    }
}
