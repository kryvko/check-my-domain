package ua.kh.kryvko.logic;

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

    private static final String REG_EXP = "(?<=href=('|\"))https?:\\/\\/.+?(?=\\1)";

    private static final String RELATIVE_REG_EXP = "(?<=href=('|\"))\\/.*?(?=\\1)";

    private static final String PROTOCOL = "http://";

    private final String domain;
    private final String startUrl;
    private final Set<URL> uniqueUrls;
    private final Map<URL, Set<URL>> brokenUrls;
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

        URL link = null;
        String html = null;

        try {
            link = new URL(url);

            if(uniqueUrls.contains(link)) {
                return;
            }

            if(brokenUrls.containsKey(link)) {
                brokenUrls.get(link).add(previousLink);
                return;
            }

            html = getHtmlByUrl(url);
            uniqueUrls.add(link);
        } catch (IOException e) {
            brokenUrls.put(link, new HashSet<URL>());
            brokenUrls.get(link).add(previousLink);
            return;
        }

        if(!hasDomainInUrl(url)) {
            return;
        }

        Set<String> foundUrls = getAllUrlsFromHtml(html);
        html = null;

        for(String foundUrl: foundUrls) {
            bypassing(foundUrl, link);
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

    private static String getHtmlByUrl(String url) throws IOException {

        URL oracle = new URL(url);
        URLConnection con = oracle.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        StringBuilder html = new StringBuilder();
        while (in.ready()) {
            html.append(in.readLine());
        }

        in.close();
        return html.toString();
    }

    public Set<URL> getUniqueUrls() {
        return uniqueUrls;
    }

    public Map<URL, Set<URL>> getBrokenUrls() {
        return brokenUrls;
    }
}
