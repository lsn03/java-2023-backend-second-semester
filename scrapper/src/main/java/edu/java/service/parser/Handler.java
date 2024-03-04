package edu.java.service.parser;

import edu.java.model.UriDTO;
import java.net.URI;

public interface Handler {
    boolean canHandle(URI uri);

    UriDTO handle(URI uri);
}
