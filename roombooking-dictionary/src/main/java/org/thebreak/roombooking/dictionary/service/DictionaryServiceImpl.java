package org.thebreak.roombooking.dictionary.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thebreak.roombooking.common.Constants;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.ResponseResult;
import org.thebreak.roombooking.dictionary.dao.DictionaryRepository;
import org.thebreak.roombooking.dictionary.model.Dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public Dictionary addDictionary(String name) {

        checkNullOrEmpty(name);

        Dictionary dictionary = dictionaryRepository.findByName(name.toLowerCase());

        if (null != dictionary) {
            CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
        }

        // Add new dictionary to db;
        Dictionary dictionary1 = new Dictionary();
        dictionary1.setName(name.toLowerCase());
        List<String> values = new ArrayList<>();
        dictionary1.setValues(values);

        return dictionaryRepository.save(dictionary1);
    }

    @Override
    public Dictionary updateById(String id, String name) {

        checkNullOrEmpty(id);
        checkNullOrEmpty(name);
        // check if id exist;
        Dictionary dictionary = this.findById(id);

        Dictionary dictionary1 = dictionaryRepository.findByName(name.toLowerCase());

        if (dictionary1 != null) {
            CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
        }

        dictionary.setName(name.toLowerCase());

        return dictionaryRepository.save(dictionary);
    }

    @Override
    public Dictionary addValueById(String id, String value) {

        // check if dictionary exist, if not, findById method will throw exception;
        Dictionary dictionary = this.findById(id);
        value = value.toLowerCase();

        // check if value already exist in the values list;
        for (String value1 : dictionary.getValues()) {
            if (value.equals(value1)) {
                CustomException.cast(CommonCode.DB_ENTRY_ALREADY_EXIST);
            }
        }
        dictionary.getValues().add(value);

        return dictionaryRepository.save(dictionary);
    }

    @Override
    public Dictionary deleteValue(String id, String value) {
        // check if dictionary exist, if not, findById method will throw exception;
        Dictionary dictionary = this.findById(id);

        // check if value already exist in the dValue list;
        if (!dictionary.getValues().contains(value.toLowerCase())) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }

        dictionary.getValues().removeIf(value1 -> value1.equals(value.toLowerCase()));

        return dictionaryRepository.save(dictionary);
    }

    @Override
    public Page<Dictionary> findPage(Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        // mongo page start with 0;
        page = page - 1;

        if (size == null) {
            size = Constants.DEFAULT_PAGE_SIZE;
        }
        if (size > Constants.MAX_PAGE_SIZE) {
            size = Constants.MAX_PAGE_SIZE;
        }

        // dictPage by page and size; default sort by ascending;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Dictionary> dictPage = dictionaryRepository.findAll(pageable);

        // check if target not exist or dictPage is empty;
        if (dictPage == null) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        if (dictPage.getContent().size() == 0) {
            CustomException.cast(CommonCode.DB_EMPTY_LIST);
        }
        return dictPage;
    }

    @Override
    public Dictionary findByName(String name) {
        checkNullOrEmpty(name);
        Dictionary dict = dictionaryRepository.findByName(name.toLowerCase());

        if (dict == null) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return dict;
    }

    @Override
    public Dictionary findById(String id) {
        checkNullOrEmpty(id);
        Optional<Dictionary> optional = dictionaryRepository.findById(id);
        if (!optional.isPresent()) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }
        return optional.get();
    }

    @Override
    public void deleteById(String id) {
        checkNullOrEmpty(id);
        Dictionary dictionary = this.findById(id);
        if (dictionary == null) {
            CustomException.cast(CommonCode.DB_ENTRY_NOT_FOUND);
        }

        dictionaryRepository.deleteById(id);
    }

    private void checkNullOrEmpty(String string) {
        if (string == null) {
            CustomException.cast(CommonCode.REQUEST_FIELD_MISSING);
        }
        if (StringUtils.isEmpty(string)) {
            CustomException.cast(CommonCode.REQUEST_FIELD_EMPTY);
        }
    }
}
