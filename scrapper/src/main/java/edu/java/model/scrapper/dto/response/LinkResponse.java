package edu.java.model.scrapper.dto.response;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LinkResponse extends MyResponse {
    private Long id;
    private URI url;
}
