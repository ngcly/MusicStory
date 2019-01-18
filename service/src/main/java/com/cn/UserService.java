package com.cn;

import com.cn.dao.RoleRepository;
import com.cn.dao.UserFavesRepository;
import com.cn.dao.UserFollowRepository;
import com.cn.dao.UserRepository;
import com.cn.entity.Role;
import com.cn.entity.User;
import com.cn.entity.UserFaves;
import com.cn.entity.UserFollow;
import com.cn.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

/**
 * 会员 service 类
 *
 * @author chen
 * @date 2018-01-02 18:20
 */

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserFavesRepository userFavesRepository;
    @Autowired
    UserFollowRepository userFollowRepository;

    /**
     * 注册账户
     * @param signUpUser 用户信息
     * @return string
     */
    public ModelMap signUp(User signUpUser){
        if(userRepository.existsByUsername(signUpUser.getUsername())) {
            return RestUtil.Error(300,"用户名已存在");
        }

        if(userRepository.existsByEmail(signUpUser.getEmail())) {
            return RestUtil.Error(300,"该邮箱已注册");
        }

        User user = new User();
        user.setRealName(signUpUser.getRealName());
        user.setUsername(signUpUser.getUsername());
        user.setEmail(signUpUser.getEmail());
        user.setPassword(passwordEncoder.encode(signUpUser.getPassword()));
        User result = userRepository.save(user);
        //设置前台注册成功跳转页面
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return RestUtil.Success();
    }

    /**
     * 修改用户
     * @param user 用户信息
     */
    public void altUser(User user){
        User user1 = userRepository.getOne(user.getId());
        user1.setState(user.getState());
        Set<Role> allRole = roleRepository.getAllByAvailableIsTrueAndRoleType((byte) 2);
        if(user.getRoleIds()!=null){
            for(String roleId:user.getRoleIds()){
                allRole.stream().filter(role -> String.valueOf(role.getId()).equals(roleId)).forEach(user1.getRoleList()::add);
            }
        }else{
            user1.setRoleList(null);
        }
        userRepository.save(user1);
    }

    /**
     * 根据条件动态查询用户列表
     * @param pageable 分页信息
     * @param user 查询条件
     * @return Page<User>
     */
    public Page<User> getUserList(Pageable pageable, User user){
        return userRepository.findAll(UserRepository.getUserList(user.getUsername(),user.getNickName(),
                user.getPhone(),user.getEmail(),user.getState()),pageable);
    }

    /**
     * 获取用户详情
     * @param userId 用户ID
     * @return user
     */
    public User getUserDetail(String userId){
        return userRepository.getOne(userId);
    }

    /**
     * 删除用户
     * @param userId 用户id
     */
    public void delUser(String userId){
        userRepository.deleteById(userId);
    }

    /**
     * 点赞/收藏
     * @param userId   用户ID
     * @param essayId  文章ID
     * @param type     类型
     */
    @Transactional
    public void addUserFaves(String userId,String essayId,byte type){
        UserFaves userFaves = new UserFaves();
        userFaves.setUserId(userId);
        userFaves.setEssayId(essayId);
        userFaves.setFaveType(type);
        userFavesRepository.save(userFaves);
    }

    /**
     * 取消点赞/收藏
     * @param userId   用户ID
     * @param essayId  文章ID
     * @param type     类型
     */
    @Transactional
    public void delUserFaves(String userId,String essayId,byte type){
        userFavesRepository.deleteUserFavesByUserIdAndAndEssayIdAndFaveType(userId,essayId,type);
    }

    /**
     * 关注
     * @param userId   用户ID
     * @param followId 被关注ID
     */
    @Transactional
    public void addUserFollow(String userId,String followId){
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setFollowId(followId);
        userFollowRepository.save(userFollow);
    }

    /**
     * 取消关注
     * @param userId    用户ID
     * @param followId  被关注ID
     */
    @Transactional
    public void delUserFollow(String userId,String followId){
        userFollowRepository.deleteUserFollowByUserIdAndAndFollowId(userId,followId);
    }

}
