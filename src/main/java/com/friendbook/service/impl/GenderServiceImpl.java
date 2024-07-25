package com.friendbook.service.impl;

import com.friendbook.dto.GenderDTO;
import com.friendbook.entity.Gender;
import com.friendbook.repository.GenderRepository;
import com.friendbook.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenderServiceImpl implements GenderService {
    @Autowired
    private GenderRepository genderRepository;

    @Override
    public List<Gender> listGender() {
        List<Gender> list = genderRepository.findAll();
        return list;
    }

    @Override
    public Gender addGender(GenderDTO genderDTO) {
        Gender gender = new Gender();
        gender.setGenderName(genderDTO.getGenderName());
        return genderRepository.save(gender);
    }

    @Override
    public Gender updateGender(long genderId, GenderDTO genderDTO) {
        Gender existingGender = findGenderById(genderId);

        existingGender.setGenderName(genderDTO.getGenderName());
        return genderRepository.save(existingGender);
    }

    @Override
    public void deleteGender(long genderId) {
        genderRepository.deleteById(genderId);
    }

    @Override
    public Gender findGenderById(long genderId) {
        return genderRepository.findById(genderId)
                .orElseThrow(() -> new RuntimeException("Gender not found"));
    }
}
