package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.entity.PageQueryDto;
import com.github.tanyueran.service.CakeUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@RestController
@RequestMapping("/role")
@Api(tags = "CakeUserRoleController", description = "蛋糕角色操作")
public class CakeUserRoleController {

    @Autowired
    private CakeUserRoleService cakeUserRoleService;

    @ApiOperation("添加角色")
    @PostMapping("/add")
    public Boolean addRole(@RequestBody @Valid CakeUserRole cakeUserRole) throws Exception {
        Boolean isSuccess = cakeUserRoleService.addRole(cakeUserRole);
        return isSuccess;
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/delete/{roleId}")
    public Boolean removeRoleById(@PathVariable("roleId") String roleId) {
        Boolean isSuccess = cakeUserRoleService.removeRoleById(roleId);
        return isSuccess;
    }

    @ApiOperation("编辑角色")
    @PutMapping("/update")
    public Boolean updateRole(@RequestBody CakeUserRole cakeUserRole) throws Exception {
        Boolean isSuccess = cakeUserRoleService.editRole(cakeUserRole);
        return isSuccess;
    }

    @ApiOperation("查询分页查询角色")
    @GetMapping("/getByPage")
    public Page<CakeUserRole> getRoleListByPage(@RequestBody @Valid PageQueryDto pageQueryDto) {
        return cakeUserRoleService.getRoleByPage(pageQueryDto);
    }
}
