package edu.java.service.handler;

import edu.java.exception.exception.IncorrectParametersException;
import edu.java.model.StackOverFlowQuestionUriDTO;
import edu.java.model.UriDTO;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.java.util.Utils;
import org.springframework.stereotype.Component;

@Component
public class StackOverFlowHandler implements Handler {
    private static final Pattern STACKOVERFLOW_QUESTION_PATTERN =
        Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)/[^/]*");

    @Override
    public int getId() {
        return Utils.STACKOVERFLOW_SITE_TYPE_ID;
    }

    @Override
    public boolean canHandle(URI uri) {
        Matcher matcher = STACKOVERFLOW_QUESTION_PATTERN.matcher(uri.toString());
        return matcher.matches();
    }

    @Override
    public UriDTO handle(URI uri) {
        Matcher matcher = STACKOVERFLOW_QUESTION_PATTERN.matcher(uri.toString());
        if (matcher.matches()) {
            Integer id = Integer.parseInt(matcher.group(1));

            return new StackOverFlowQuestionUriDTO(id);
        } else {

            throw new IncorrectParametersException("Site: " + uri + " not supported");
        }
    }
}
