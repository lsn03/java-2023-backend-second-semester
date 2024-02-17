package edu.java.model.github.dto;

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
public class PullCommitDTO {
    private String sha;

    @JsonProperty("html_url")
    private String htmlUrl;

    private CommitDetail commit;

    private UserInfoDTO author;

    private UserInfoDTO committer;

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class CommitDetail {
        private CommitDetailInfo author;
        private CommitDetailInfo committer;
        private String message;

        record CommitDetailInfo(String name, String email, OffsetDateTime date) {

        }

    }

}
