package edu.java.service.handler;

import edu.java.model.UriDto;
import java.net.URI;

public interface Handler {

    boolean canHandle(URI uri);

    UriDto handle(URI uri);
}
