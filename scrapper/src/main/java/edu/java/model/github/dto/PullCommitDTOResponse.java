package edu.java.model.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.model.github.dto.info.UserInfoDTO;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PullCommitDTOResponse {
    private String sha;

    @JsonProperty("html_url")
    private String htmlUrl;

    private CommitDetail commit;

    private UserInfoDTO committer;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class CommitDetail {
        private CommitDetailInfo committer;
        private String message;

        public record CommitDetailInfo(String name, String email, OffsetDateTime date) {

        }

    }

}
