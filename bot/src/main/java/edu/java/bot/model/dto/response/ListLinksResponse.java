package edu.java.bot.model.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListLinksResponse extends MyResponse {
    List<LinkResponse> lists;
    Integer size;
}
