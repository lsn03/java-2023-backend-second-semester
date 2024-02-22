package edu.java.model.scrapper.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListLinksResponse {
    List<LinkResponse> lists;
    Integer size;
}
