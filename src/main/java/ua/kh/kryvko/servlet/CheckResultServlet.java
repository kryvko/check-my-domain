package ua.kh.kryvko.servlet;

import ua.kh.kryvko.logic.BypassDomain;
import ua.kh.kryvko.logic.Link;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.PrintWriter;

public class CheckResultServlet extends HttpServlet {

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

        try(PrintWriter writer = response.getWriter()) {
            writer.write("<html><body>");
            writer.write("Count of unique links: " + bypassDomain.getUniqueUrls().size() + "<br>");
            writer.write("<br>Broken links: " + bypassDomain.getBrokenUrls().size() + "<br>");
            for(Link link: bypassDomain.getBrokenUrls()) {
                Link tmpLink = link;
                writer.write("<h3 style='color: red;'><a href='>" + link.getUrl() + "'/></h3>");
                writer.write("<ul>");
                while (tmpLink != null) {
                    writer.write("<li><a href='>" + tmpLink.getUrl() + "'/></li><br>");
                    tmpLink = tmpLink.getPrevious();
                }
                writer.write("</ul><br>");
            }
            writer.write("</body></html>");
        } catch (IOException e) {

        }
    }
}
