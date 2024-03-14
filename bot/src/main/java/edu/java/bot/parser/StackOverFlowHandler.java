package edu.java.bot.parser;

import org.springframework.stereotype.Component;

@Component
public class StackOverFlowHandler implements ResourceHandler {
    @Override
    public boolean canHandle(String url) {
        return url.startsWith("https://stackoverflow.com/");
    }

}
