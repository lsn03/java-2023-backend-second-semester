package edu.java.model.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.info.UserInfoDTO;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PullReviewDTOResponse {
    private Long id;

    @JsonProperty("html_url")
    private String htmlUrl;

    private UserInfoDTO user;

    private String body;

    @JsonProperty("submitted_at")
    private OffsetDateTime submittedAt;

}
