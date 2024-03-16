package edu.java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GitHubPullRequestUriDTO extends UriDTO {
    private String owner;
    private String repo;
    private Integer pullNumber;

}
