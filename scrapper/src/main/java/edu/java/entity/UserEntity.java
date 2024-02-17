package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    private Long chatId;
    private Set<String> trackedUrls;
}
