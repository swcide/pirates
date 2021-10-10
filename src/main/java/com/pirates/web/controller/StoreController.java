package com.pirates.web.controller;


import com.pirates.service.StoreService;
import com.pirates.web.dto.request.StoreRequestDto;
import com.pirates.web.dto.response.StoreArrivesDateResponseDto;
import com.pirates.web.dto.response.StoreDetailResponseDto;
import com.pirates.web.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("")
    public Long createStore(@RequestBody StoreRequestDto storeRequestDto){
        return storeService.createStore(storeRequestDto);
    }

    @GetMapping("")
    public List<StoreResponseDto> getALlStore(){
        return storeService.getALlStore();
    }

    @GetMapping("/{id}")
    public StoreDetailResponseDto getStoreDetail(@PathVariable Long id){
        return storeService.getStoreDetail(id);
    }

    @GetMapping("/arrives/{id}")
    public List<StoreArrivesDateResponseDto> getStoreArrivesDate(@PathVariable Long id){
        return storeService.getStoreArrivesDate(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable Long id){
        storeService.deleteStore(id);
    }

}
