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
public class PullCommentDTO {

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
