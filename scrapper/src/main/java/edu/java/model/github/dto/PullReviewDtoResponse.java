package edu.java.model.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.info.UserInfoDto;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PullReviewDtoResponse {
    private Long id;

    @JsonProperty("html_url")
    private String htmlUrl;

    private UserInfoDto user;

    private String body;

    @JsonProperty("submitted_at")
    private OffsetDateTime submittedAt;

}
