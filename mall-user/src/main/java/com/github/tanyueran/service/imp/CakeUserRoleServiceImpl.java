package com.github.tanyueran.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tanyueran.entity.CakeUserRole;
import com.github.tanyueran.entity.PageQueryDto;
import com.github.tanyueran.mapper.CakeUserRoleMapper;
import com.github.tanyueran.service.CakeUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tanxin
 * @since 2020-10-23
 */
@Service
@Transactional
public class CakeUserRoleServiceImpl extends ServiceImpl<CakeUserRoleMapper, CakeUserRole> implements CakeUserRoleService {

    @Resource
    private CakeUserRoleMapper cakeUserRoleMapper;

    @Override
    public Boolean addRole(CakeUserRole cakeUserRole) throws Exception {
        cakeUserRole.setUpdateTime(null);
        cakeUserRole.setCreateTime(null);
        QueryWrapper<CakeUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", cakeUserRole.getRoleCode());
        CakeUserRole role = cakeUserRoleMapper.selectOne(wrapper);
        if (role != null) {
            throw new Exception("角色code已被使用，请更换");
        }
        int insert = cakeUserRoleMapper.insert(cakeUserRole);
        return insert == 1;
    }

    @Override
    public Boolean removeRoleById(String roleId) {
        int i = cakeUserRoleMapper.deleteById(roleId);
        return i == 1;
    }

    @Override
    public Boolean editRole(CakeUserRole cakeUserRole) throws Exception {
        cakeUserRole.setCreateTime(null);
        cakeUserRole.setUpdateTime(null);
        // 先通过id查出数据中的数据
        CakeUserRole roleInDB = cakeUserRoleMapper.selectById(cakeUserRole.getId());
        // 判断数据中的code，是否改变，
        // ---改变，则判断是否可用
        // ---没有改变，直接更新
        if (!roleInDB.getRoleCode().equals(cakeUserRole.getRoleCode())) {
            QueryWrapper<CakeUserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("role_code", cakeUserRole.getRoleCode());
            CakeUserRole role = cakeUserRoleMapper.selectOne(wrapper);
            if (role != null) {
                throw new Exception("角色code已被使用，请更换");
            }
        }
        int i = cakeUserRoleMapper.updateById(cakeUserRole);
        return i == 1;
    }

    @Override
    public Page<CakeUserRole> getRoleByPage(PageQueryDto pageQueryDto) {
        Page<CakeUserRole> page = new Page<>(pageQueryDto.getPage(), pageQueryDto.getSize());
        return cakeUserRoleMapper.selectPage(page, null);
    }
}
