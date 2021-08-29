package org.thebreak.roombooking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thebreak.roombooking.model.Dictionary;

@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary, String> {
    Dictionary findByName(String name);

    Page<Dictionary> findAll(Pageable pageable);
}
