package edu.java.bot.parser;

import java.net.URI;

public interface ResourceHandler {
    boolean canHandle(URI url);

}
