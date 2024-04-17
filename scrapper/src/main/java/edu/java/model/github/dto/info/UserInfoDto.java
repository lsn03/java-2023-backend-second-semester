package edu.java.model.github.dto.info;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserInfoDto {
    private String login;
    private Long id;
}
