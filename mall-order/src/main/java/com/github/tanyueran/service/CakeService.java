package com.github.tanyueran.service;

import com.github.tanyueran.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mall-cake", path = "/product")
public interface CakeService {

    @GetMapping("/detail/{id}")
    ResponseResult getDetailById(@PathVariable("id") String cakeId);
}
