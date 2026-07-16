package com.cn.persistence.adapter;
 
import com.cn.dao.UserRepository;
import com.cn.enums.UserStatusEnum;
import com.cn.persistence.mapper.UserMapper;
import com.cn.user.domain.User;
import com.cn.user.domain.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Optional;
 
/**
 * 用户数据仓储端口适配器 (Repository Adapter)
 *
 * @author ngcly
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;
 
    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .map(UserMapper::toDomain);
    }
 
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
 
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
 
    @Override
    public User save(User user) {
        com.cn.entity.User entity = UserMapper.toEntity(user);
        return UserMapper.toDomain(userRepository.save(entity));
    }
 
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDomain);
    }
 
    @Override
    public User getReferenceById(Long id) {
        return UserMapper.toDomain(userRepository.getReferenceById(id));
    }
 
    @Override
    public Page<User> findUserList(String username, String nickName, String email, String phone, String state, Pageable pageable) {
        UserStatusEnum statusEnum = state != null ? UserStatusEnum.valueOf(state) : null;
        var spec = UserRepository.getUserList(username, nickName, phone, email, statusEnum);
        return userRepository.findAll(spec, pageable)
                .map(UserMapper::toDomain);
    }
 
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
 
    @Override
    public void delete(User user) {
        userRepository.delete(UserMapper.toEntity(user));
    }
}
