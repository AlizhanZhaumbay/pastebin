package org.example.paste;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasteRepository extends MongoRepository<Paste, String> {

    Optional<Paste> findByShortLink(String shortLink);

    boolean existsByShortLink(String shortLink);
}
