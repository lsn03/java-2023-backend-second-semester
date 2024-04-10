package edu.java.service.process;

import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface LinkUpdaterService {
    List<LinkUpdateRequest> update() throws NoSuchAlgorithmException;
}
