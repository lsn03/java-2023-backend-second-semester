package edu.java.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GitHubPullRequestUriDTO extends UriDTO {
    private String owner;
    private String repo;
    private Integer pullNumber;

}
