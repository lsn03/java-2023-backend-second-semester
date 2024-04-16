package edu.java.domain.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LinkDto {
    private URI uri;
    private Long tgChatId;
    private Long linkId;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkDto linkDTO = (LinkDto) o;
        return Objects.equals(uri, linkDTO.uri) && Objects.equals(tgChatId, linkDTO.tgChatId)
            && Objects.equals(linkId, linkDTO.linkId) && Objects.equals(createdAt, linkDTO.createdAt)
            && Objects.equals(lastUpdate, linkDTO.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, tgChatId, linkId);
    }
}
