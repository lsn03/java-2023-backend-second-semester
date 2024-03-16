package edu.java.domain.model;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LinkDTO {
    private URI uri;
    private Long tgChatId;
    private Long linkId;
    private String hash;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdate;
}
