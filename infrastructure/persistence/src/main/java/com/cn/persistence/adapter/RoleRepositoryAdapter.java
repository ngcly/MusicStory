package com.cn.persistence.adapter;
 
import com.cn.dao.RoleRepository;
import com.cn.enums.UserTypeEnum;
import com.cn.persistence.mapper.UserMapper;
import com.cn.user.domain.Role;
import com.cn.user.domain.repository.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
 
/**
 * 角色仓储端口适配器 (Repository Adapter)
 *
 * @author ngcly
 */
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {
    private final RoleRepository roleRepository;
 
    @Override
    public List<Role> getActiveRolesByType(UserTypeEnum type) {
        return roleRepository.getAllByAvailableIsTrueAndRoleType(type).stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }
}
