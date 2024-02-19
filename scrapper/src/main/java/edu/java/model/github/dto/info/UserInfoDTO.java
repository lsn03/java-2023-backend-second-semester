package edu.java.model.github.dto.info;

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
public class UserInfoDTO {
    private String login;
    private Long id;
}
