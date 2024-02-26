package edu.java.model.scrapper.dto.response;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkResponse extends MyResponse {
    private Long id;
    private URI url;
}
