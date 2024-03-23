package edu.java.bot.service;

import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.parser.ResourceHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkParserService {
    private List<ResourceHandler> handlers;
    private String template = "WebSite %s is not supported.";

    @Autowired
    public LinkParserService(List<ResourceHandler> handlers) {
        this.handlers = handlers;
    }

    public void process(String url) {
        URI uri = null;
        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            if (e.getCause() instanceof URISyntaxException) {
                throw new UnsupportedSiteException(String.format(template, url));
            }
        }
        for (ResourceHandler handler : handlers) {
            if (handler.canHandle(uri)) {
                return;
            }
        }
        throw new UnsupportedSiteException(String.format(template, url));

    }
}
