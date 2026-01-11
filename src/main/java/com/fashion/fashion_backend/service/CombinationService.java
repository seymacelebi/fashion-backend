package com.fashion.fashion_backend.service;

import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.entity.dto.CombinationRequest;
import com.fashion.fashion_backend.entity.dto.CombinationResponse;

import java.util.List;

public interface CombinationService {

    CombinationResponse createCombination(CombinationRequest request, User user);


    List<CombinationResponse> getUserCombinations(User user);


    void deleteCombination(Long id, User user);


    CombinationResponse getCombinationById(Long id, User user);
}
