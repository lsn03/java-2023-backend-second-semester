package edu.java.domain.repository;

import edu.java.domain.model.ChatDTO;
import edu.java.domain.model.LinkDTO;
import java.util.List;

public interface LinkRepository {
    void add(LinkDTO linkDTO);
    void remove(LinkDTO linkDTO);
    void remove(Long id);
    void remove(String url);
    List<LinkDTO> findAll();
}
