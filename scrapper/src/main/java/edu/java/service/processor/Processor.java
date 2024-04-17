package edu.java.service.processor;

import edu.java.domain.model.LinkDto;
import edu.java.model.UriDto;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import java.util.List;

public interface Processor {
    List<LinkUpdateRequest> processUriDTO(LinkDto linkDTO, UriDto uriDto);
}
