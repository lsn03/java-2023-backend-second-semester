package edu.java.service.processor;

import edu.java.domain.model.LinkDTO;
import edu.java.model.UriDTO;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import java.util.List;

public interface Processor {
    List<LinkUpdateRequest> processUriDTO(LinkDTO linkDTO, UriDTO uriDto);
}
