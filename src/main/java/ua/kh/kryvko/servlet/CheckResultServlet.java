package ua.kh.kryvko.servlet;

import ua.kh.kryvko.logic.BypassDomain;

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
        this.servletContext.setAttribute("isLoading", true);
    }

    @Override
    public void destroy() {
        this.servletContext.setAttribute("isLoading", false);
        super.destroy();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String domain = request.getParameter("domain");
        String startUrl = request.getParameter("startUrl");

        if(domain == null || startUrl == null) {
            response.setStatus(400);
            return;
        }

        BypassDomain bypassDomain = new BypassDomain(domain, startUrl);

        bypassDomain.startBypass();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><body>");
        stringBuilder.append("Count of unique links: " + bypassDomain.getUniqueUrls().size() + "<br>");
        stringBuilder.append("<br>Broken links: " + bypassDomain.getBrokenUrls().size() + "<br>");
        for(URL link: bypassDomain.getBrokenUrls().keySet()) {
            stringBuilder.append("<h3><a style='color: red;' href='" + link + "'>" + link + "</a></h3>");
            stringBuilder.append("<ul>");
            for (URL prevLink : bypassDomain.getBrokenUrls().get(link)) {
                stringBuilder.append("<li><a href='" + prevLink + "'>" + prevLink + "</a></li><br>");
            }
            stringBuilder.append("</ul><br>");
        }
        stringBuilder.append("</body></html>");
        String result = stringBuilder.toString();
        this.servletContext.setAttribute("lastResult", result);
        response.sendRedirect("/check-my-domain/lastResult");
    }
}
