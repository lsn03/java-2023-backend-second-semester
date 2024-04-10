package edu.java.service.parser;

import edu.java.model.StackOverFlowQuestionUriDto;
import edu.java.model.UriDto;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackOverFlowHandler implements Handler {
    private static final Pattern STACKOVERFLOW_QUESTION_PATTERN =
        Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)/[^/]*");

    @Override
    public boolean canHandle(URI uri) {
        Matcher matcher = STACKOVERFLOW_QUESTION_PATTERN.matcher(uri.toString());
        return matcher.matches();
    }

    @Override
    public UriDto handle(URI uri) {
        Matcher matcher = STACKOVERFLOW_QUESTION_PATTERN.matcher(uri.toString());
        if (matcher.matches()) {
            Integer id = Integer.parseInt(matcher.group(1));

            return new StackOverFlowQuestionUriDto(id);
        } else {

            throw new IllegalArgumentException();
        }
    }
}
