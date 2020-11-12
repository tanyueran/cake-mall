package com.github.tanyueran.service;

import com.github.tanyueran.dto.PayDto;
import com.github.tanyueran.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "mall-admin", path = "/user")
public interface CakeUserService {

    @PostMapping("/pay")
    ResponseResult pay(@RequestBody @Valid PayDto payDto);
}
