package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class TrackedUrlEntity {
    private String url;
    private Set<Long> users;
}
