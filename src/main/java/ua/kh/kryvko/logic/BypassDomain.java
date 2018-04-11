package ua.kh.kryvko.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BypassDomain {

    private static final String REG_EXP = "(?<=href=['\"])https?:\\/\\/.+?(?=([\\?\\#].*)?['\"])";

    private static final String RELATIVE_REG_EXP = "(?<=href=['\"])\\/.*?(?=([\\?\\#].*)?['\"])";

    private final String domain;
    private final String startUrl;
    private final Set<Link> uniqueUrls;
    private final Set<Link> brokenUrls;
    private final Pattern hrefPattern;
    private final Pattern domainPattern;
    private final Pattern relativePattern;

    public BypassDomain(String domain, String startUrl) {
        this.domain = domain;
        this.startUrl = startUrl;
        this.uniqueUrls = new HashSet<>();
        this.brokenUrls = new HashSet<>();
        this.hrefPattern = Pattern.compile(REG_EXP);
        this.relativePattern = Pattern.compile(RELATIVE_REG_EXP);
        this.domainPattern = Pattern.compile(domain);
    }

    public void startBypass() {
        bypassing(this.startUrl, null);
    }

    private void bypassing(String url, Link previousLink) {
        Link link = new Link(url, previousLink);

        if(uniqueUrls.contains(link)) {
            return;
        }

        uniqueUrls.add(link);
        String html;
        try {
            html = getHtmlByUrl(url);
        } catch (IOException e) {
            brokenUrls.add(link);
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
        return this.domainPattern.matcher(url).find();
    }

    private Set<String> getAllUrlsFromHtml(String html) {
        Set<String> links = new HashSet<>();
        Matcher matcher = this.hrefPattern.matcher(html);
        while (matcher.find()) {
            links.add(matcher.group());
        }
        Matcher relativeMatcher = this.relativePattern.matcher(html);
        while (relativeMatcher.find()) {
            links.add("http://" + this.domain + relativeMatcher.group());
        }
        return links;
    }

    private static String getHtmlByUrl(String url) throws IOException {
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        StringBuilder html = new StringBuilder();
        while (in.ready()) {
            html.append(in.readLine());
        }

        in.close();
        return html.toString();
    }

    public Set<Link> getUniqueUrls() {
        return uniqueUrls;
    }

    public Set<Link> getBrokenUrls() {
        return brokenUrls;
    }
}
