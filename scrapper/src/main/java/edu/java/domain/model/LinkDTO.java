package edu.java.domain.model;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LinkDTO {
    private URI uri;
    private Long tgChatId;
    private Long linkId;
}
