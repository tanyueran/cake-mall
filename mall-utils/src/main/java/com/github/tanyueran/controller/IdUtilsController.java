package com.github.tanyueran.controller;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/id")
@Slf4j
public class IdUtilsController {

    @Autowired
    private Snowflake snowflake;

    @GetMapping("/{num}")
    public List<String> getIds(@PathVariable("num") Integer num) {
        if (num <= 0) {
            throw new RuntimeException("参数不可小于1");
        }
        List<String> list = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            Long l = snowflake.nextId();
            list.add(l.toString());
        }
        return list;
    }
}
