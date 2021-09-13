package org.thebreak.roombooking.dictionary.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.dictionary.model.Dictionary;


@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary, String> {
    Dictionary findByNameIgnoreCase(String name);
    Dictionary findByName(String name);

    Page<Dictionary> findAll(Pageable pageable);
}
