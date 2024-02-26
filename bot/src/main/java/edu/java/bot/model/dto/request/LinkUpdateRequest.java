package edu.java.bot.model.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkUpdateRequest extends MyRequest {
    private long id;
    private String url;
    private String description;
    private List<Integer> tgChatIds;
}
