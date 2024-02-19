package edu.java.model.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.info.UserInfoDTO;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class IssueCommentDTO {
    @JsonProperty("html_url")
    private String htmlUrl;

    private Long id;

    private UserInfoDTO user;

    private String body;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

}
