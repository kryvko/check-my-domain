package ua.kh.kryvko.logic;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BypassDomain {

    private static final String REG_EXP = "(?<=href=('|\"))https?:\\/\\/.+?(?=(#.*?)?\\1)";

    private static final String RELATIVE_REG_EXP = "(?<=href=('|\"))\\/.*?(?=(#.*?)?\\1)";

    private static final String PROTOCOL = "http://";

    private static final String CONTENT_TYPE = "text/html";

    private static final Logger LOGGER = Logger.getLogger("error");

    private final String domain;
    private final String startUrl;
    private final Set<String> uniqueUrls;
    private final Map<String, Set<URL>> brokenUrls;
    private final Pattern hrefPattern;
    private final Pattern relativePattern;

    public BypassDomain(String domain, String startUrl) {
        this.domain = domain;
        this.startUrl = startUrl;
        this.uniqueUrls = new HashSet<>();
        this.brokenUrls = new HashMap<>();
        this.hrefPattern = Pattern.compile(REG_EXP);
        this.relativePattern = Pattern.compile(RELATIVE_REG_EXP);
    }

    public void startBypass() {
        bypassing(this.startUrl, null);
    }

    private void bypassing(String url, URL previousLink) {

        try {
            URL link = null;
            String html = null;

            try {
                link = new URL(url);

                if (uniqueUrls.contains(url)) {
                    return;
                }

                if (brokenUrls.containsKey(url)) {
                    brokenUrls.get(url).add(previousLink);
                    return;
                }

                html = getHtmlByUrl(link);
                uniqueUrls.add(url);
            } catch (Exception e) {
                if (!brokenUrls.containsKey(url)) {
                    brokenUrls.put(url, new HashSet<URL>());
                }
                brokenUrls.get(url).add(previousLink);
                return;
            }

            if (!hasDomainInUrl(url)) {
                return;
            }

            if (html == null) {
                return;
            }

            Set<String> foundUrls = getAllUrlsFromHtml(html);
            html = null;

            for (String foundUrl : foundUrls) {
                bypassing(foundUrl, link);
            }
        } catch (Error e) {
            LOGGER.log(Level.ERROR, url, e);
            throw e;
        }
    }

    private boolean hasDomainInUrl(String url) {
        return url.contains(this.domain);
    }

    private Set<String> getAllUrlsFromHtml(String html) {
        Set<String> links = new HashSet<>();
        Matcher matcher = this.hrefPattern.matcher(html);
        while (matcher.find()) {
            links.add(matcher.group());
        }
        Matcher relativeMatcher = this.relativePattern.matcher(html);
        while (relativeMatcher.find()) {
            links.add(PROTOCOL + this.domain + relativeMatcher.group());
        }
        return links;
    }

    private static String getHtmlByUrl(URL url) throws IOException {

        String html = null;
        URLConnection con = url.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        if (con.getContentType() == null || con.getContentType().contains(CONTENT_TYPE)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            StringBuilder htmlBuffer = new StringBuilder();
            while (in.ready()) {
                htmlBuffer.append(in.readLine());
            }

            in.close();
            html = htmlBuffer.toString();
        }
        return html;
    }

    public Set<String> getUniqueUrls() {
        return uniqueUrls;
    }

    public Map<String, Set<URL>> getBrokenUrls() {
        return brokenUrls;
    }
}
