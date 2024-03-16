package edu.java.service.processor;

import edu.java.domain.model.LinkDTO;
import edu.java.model.UriDTO;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import java.util.Map;

public interface Processor {
    Map<String, LinkUpdateRequest> processUriDTO(LinkDTO linkDTO, UriDTO uriDto);
}
