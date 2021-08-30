package org.thebreak.roombooking.dictionary.service;


import org.springframework.data.domain.Page;
import org.thebreak.roombooking.dictionary.model.Dictionary;

public interface DictionaryService {
    Dictionary addDictionary(String name);

    Dictionary updateById(String id, String name);

    Dictionary addValueById(String id, String value);

    Dictionary deleteValue(String id, String value);

    Page<Dictionary> findPage(Integer page, Integer size);

    Dictionary findByName(String name);

    Dictionary findById(String id);

    void deleteById(String id);

}
