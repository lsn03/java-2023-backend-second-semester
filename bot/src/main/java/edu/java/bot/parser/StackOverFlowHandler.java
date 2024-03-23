package edu.java.bot.parser;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackOverFlowHandler implements ResourceHandler {
    private static final Pattern STACKOVERFLOW_QUESTION_PATTERN =
        Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)/.*");

    @Override
    public boolean canHandle(URI uri) {
        Matcher matcher = STACKOVERFLOW_QUESTION_PATTERN.matcher(uri.toString());
        return matcher.matches();
    }

}
