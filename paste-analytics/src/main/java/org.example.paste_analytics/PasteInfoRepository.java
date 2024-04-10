package org.example.paste_analytics;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PasteInfoRepository extends JpaRepository<PasteInfo, Integer> {

    PasteInfo findByPasteShortLink(String pasteShortLink);

    @Transactional
    @Modifying
    @Query(value = "UPDATE paste_info set visited = visited + 1 where id=:id", nativeQuery = true)
    void increaseVisitedSequence(Integer id);


    @Query(value = "select id from paste_info where paste_short_link=:pasteShortLink", nativeQuery = true)
    Integer getIdByPasteShortLink(String pasteShortLink);

    boolean existsByPasteShortLink(String pasteShortLink);

    @Transactional
    @Modifying
    void deleteByPasteShortLink(String shortLink);

    @Query(value = "select pasteShortLink from PasteInfo")
    List<String> getAllPasteShortLinks();

    @Transactional
    @Modifying
    @Query(value = "delete from PasteInfo where pasteShortLink in (:pasteShortLinks)")
    void deleteAllInBatchByPasteShortLinks(List<String> pasteShortLinks);

    @Query(value = "select distinct :pasteShortLinks from paste_info where paste_short_link not in (:pasteShortLinks)",
            nativeQuery = true)
    List<String> findAllNotExistingPasteShortLinks(List<String> pasteShortLinks);
}
