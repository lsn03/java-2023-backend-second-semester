package edu.java.bot.service;

import edu.java.bot.exception.UnsupportedSiteException;
import edu.java.bot.parser.ResourceHandler;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkParserService {
    private List<ResourceHandler> handlers;

    @Autowired
    public LinkParserService(List<ResourceHandler> handlers) {
        this.handlers = handlers;
    }

    public void process(String url) {
        for (ResourceHandler handler : handlers) {
            if (handler.canHandle(url)) {
                return;
            }
        }
        throw new UnsupportedSiteException("WebSite " + url + " is not supported.");

    }
}
