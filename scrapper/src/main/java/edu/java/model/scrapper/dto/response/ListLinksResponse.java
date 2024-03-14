package edu.java.model.scrapper.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListLinksResponse extends MyResponse {
    List<LinkResponse> lists;
    Integer size;
}
