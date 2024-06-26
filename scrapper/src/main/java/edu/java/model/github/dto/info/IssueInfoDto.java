package edu.java.model.github.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IssueInfoDto {
    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
    private UserInfoDto user;

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
    private UserInfoDto closedBy;

}
