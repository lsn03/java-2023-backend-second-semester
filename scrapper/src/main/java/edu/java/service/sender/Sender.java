package edu.java.service.sender;

import edu.java.model.scrapper.dto.request.LinkUpdateRequest;

public interface Sender {
    void send(LinkUpdateRequest update);
}
