package edu.java.model.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.info.UserInfoDTO;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PullCommentDTOResponse {

    private Long id;

    private String body;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    private UserInfoDTO user;

}
