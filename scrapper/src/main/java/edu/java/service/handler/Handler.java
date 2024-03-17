package edu.java.service.handler;

import edu.java.model.UriDTO;
import java.net.URI;

public interface Handler {

    boolean canHandle(URI uri);

    UriDTO handle(URI uri);
}
