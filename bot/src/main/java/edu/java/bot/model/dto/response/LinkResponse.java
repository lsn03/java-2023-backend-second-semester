package edu.java.bot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.net.URI;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkResponse extends MyResponse {
    private Long id;
    private URI url;
}
