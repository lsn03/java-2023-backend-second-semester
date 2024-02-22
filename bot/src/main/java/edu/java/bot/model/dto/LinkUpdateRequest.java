package edu.java.bot.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkUpdateRequest {
    private long id;
    private String url;
    private String description;
    private List<Integer> tgChatIds;
}
