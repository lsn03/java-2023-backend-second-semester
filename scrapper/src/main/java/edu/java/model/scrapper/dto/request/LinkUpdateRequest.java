package edu.java.model.scrapper.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LinkUpdateRequest extends MyRequest {
    private Long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
