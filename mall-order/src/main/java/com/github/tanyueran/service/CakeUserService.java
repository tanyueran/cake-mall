package com.github.tanyueran.service;

import com.github.tanyueran.dto.GetMoneyDto;
import com.github.tanyueran.dto.PayDto;
import com.github.tanyueran.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "mall-admin", path = "/user")
public interface CakeUserService {

    @PostMapping("/pay")
    ResponseResult pay(@RequestBody PayDto payDto);

    @PostMapping("/getMoney")
    ResponseResult getMoney(@RequestBody GetMoneyDto getMoneyDto) throws Exception;
}
