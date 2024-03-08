package edu.java.bot.model.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LinkUpdateRequest extends MyRequest {
    private long id;
    private String url;
    private String description;
    private List<Integer> tgChatIds;
}
