package com.github.tanyueran.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.entity.PageQueryDto;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
public interface CakeUserRoleService extends IService<CakeUserRole> {
    /**
     * 添加角色
     *
     * @param cakeUserRole
     * @return
     * @throws Exception
     */
    Boolean addRole(CakeUserRole cakeUserRole) throws Exception;

    /**
     * 根据id删除角色
     *
     * @param roleId
     * @return
     */
    Boolean removeRoleById(String roleId);

    /**
     * 编辑角色
     *
     * @param cakeUserRole
     * @return
     */
    Boolean editRole(CakeUserRole cakeUserRole) throws Exception;


    /**
     * 分页查询角色信息
     *
     * @param pageQueryDto
     * @return
     */
    Page<CakeUserRole> getRoleByPage(PageQueryDto pageQueryDto);
}
