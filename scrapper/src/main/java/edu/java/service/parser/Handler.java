package edu.java.service.parser;

import edu.java.model.UriDto;
import java.net.URI;

public interface Handler {
    boolean canHandle(URI uri);

    UriDto handle(URI uri);
}
