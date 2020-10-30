package com.github.tanyueran.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.tanyueran.dto.RolePageQueryDto;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.service.CakeUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @PostMapping("/getByPage")
    public IPage<CakeUserRole> getRoleListByPage(@RequestBody @Valid RolePageQueryDto rolePageQueryDto) {
        return cakeUserRoleService.getRoleByPage(rolePageQueryDto);
    }

    @ApiOperation("查询所有的角色")
    @GetMapping("/all")
    public List<CakeUserRole> getAllRoles() {
        return cakeUserRoleService.getAllRoles();
    }
}
