package com.friendbook.controller;

import com.friendbook.dto.GenderDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GenderController extends BaseReponse {
    @Autowired
    private GenderService genderService;

    @GetMapping("/gender")
    public ResponseEntity<?> getAllGenders() {
        try {
            return getResponseEntity(genderService.listGender());
        } catch (Exception e) {
            return getErrorResponseEntity("Error while list gender", 500);
        }
    }

    @PostMapping("/gender")
    public ResponseEntity<?> addGender(@RequestBody GenderDTO genderDTO) {
        try {
            return getResponseEntity(genderService.addGender(genderDTO));
        }catch (Exception e) {
            return getErrorResponseEntity("Error while add gender", 500);
        }
    }

    @PutMapping("/gender")
    public ResponseEntity<?> updateGender(@RequestParam long genderId,
                                          @RequestBody GenderDTO genderDTO) {
        try {
            return getResponseEntity(genderService.updateGender(genderId, genderDTO));
        }catch (Exception e) {
            return getErrorResponseEntity("Error while update gender", 500);
        }
    }
    @DeleteMapping("/gender")
    public ResponseEntity<?> deleteGender(@RequestParam long genderId) {
        try {
            genderService.deleteGender(genderId);
            return getResponseEntity("Gender deleted successfully");
        }catch (Exception e) {
            return getErrorResponseEntity("Error while delete gender", 500);
        }
    }
}
