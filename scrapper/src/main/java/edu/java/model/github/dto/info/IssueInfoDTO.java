package edu.java.model.github.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class IssueInfoDTO {
    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
    private UserInfoDTO user;

    @JsonProperty("body")
    private String body;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    @JsonProperty("closed_at")
    private OffsetDateTime closedAt;

    @JsonProperty("closed_by")
    private UserInfoDTO closedBy;

}
