package com.cn.user.domain.repository;
 
import com.cn.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
 
/**
 * 用户仓储端口接口 (Repository Port)
 *
 * @author ngcly
 */
public interface UserRepositoryPort {
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
    Optional<User> findById(Long id);
    User getReferenceById(Long id);
    Page<User> findUserList(String username, String nickName, String email, String phone, String state, Pageable pageable);
    void deleteById(Long id);
    void delete(User user);
}
