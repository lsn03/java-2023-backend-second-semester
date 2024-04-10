package edu.java.model.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.info.UserInfoDto;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Data
public class PullCommitDtoResponse {
    private String sha;

    @JsonProperty("html_url")
    private String htmlUrl;

    private CommitDetail commit;

    private UserInfoDto committer;

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class CommitDetail {
        private CommitDetailInfo committer;
        private String message;

        public record CommitDetailInfo(String name, String email, OffsetDateTime date) {

        }

    }

}
