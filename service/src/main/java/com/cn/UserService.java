package com.cn;

import cn.hutool.core.map.MapUtil;
import com.cn.config.RabbitConfig;
import com.cn.dao.RoleRepository;
import com.cn.dao.UserFavesRepository;
import com.cn.dao.UserFollowRepository;
import com.cn.dao.UserRepository;
import com.cn.entity.*;
import com.cn.pojo.UserDetail;
import com.cn.util.RestUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 会员 service 类
 *
 * @author chen
 * @date 2018-01-02 18:20
 */

@Service
public class UserService implements UserDetailsService {
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
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    SimpMessageSendingOperations messageTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username,username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return user;
    }

    /**
     * 注册账户
     * @param signUpUser 用户信息
     * @return string
     */
    public ModelMap signUp(User signUpUser){
        if(userRepository.existsByUsername(signUpUser.getUsername())) {
            return RestUtil.failure(300,"用户名已存在");
        }

        if(userRepository.existsByEmail(signUpUser.getEmail())) {
            return RestUtil.failure(300,"该邮箱已注册");
        }

        signUpUser.setPassword(passwordEncoder.encode(signUpUser.getPassword()));
        signUpUser.setState((byte) 0);
        User result = userRepository.save(signUpUser);
        //生成激活码
        String code = System.currentTimeMillis()%100+result.getUsername();
        //以激活码为KEY 将用户ID保存到redis 有效期三小时
        redisTemplate.opsForValue().set(code,result.getId(),3,TimeUnit.HOURS);
        Map<String,String> map = MapUtil.newHashMap(3);
        map.put("to",result.getEmail());
        map.put("subject","来自音书网站的激活邮件");
        map.put("context","感谢注册音书网站！<br/>请完成激活进行使用:<a href=\"https://api.ngcly.cn/active/"+code+"\">点击激活</a><br/>激活有效期为3小时");
        //通过MQ 异步进行邮件发送
        rabbitTemplate.convertAndSend(RabbitConfig.ACTIVE_QUEUE,map);
        //通过延迟队列 清除过期未激活账号
        rabbitTemplate.convertAndSend(RabbitConfig.DELAY_EXCHANGE,RabbitConfig.DELAY_ROUTING_KEY,result.getId(),msg->{
            msg.getMessageProperties().setHeader("x-delay", 3*60*60*1000);
            return msg;
        });
        return RestUtil.success(result.getUsername());
    }

    /**
     * 用户注册激活
     */
    @Transactional(rollbackFor = Exception.class)
    public ModelMap activeUser(String code){
        //根据激活码 从redis获取用户ID信息
        String id = redisTemplate.opsForValue().get(code);
        if(StringUtils.hasLength(id)){
            return RestUtil.failure(500,"激活码已过期或无效");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            return RestUtil.failure(500,"激活码错误");
        }
        User user = userOptional.get();
        user.setState((byte) 1);
        userRepository.save(user);
        redisTemplate.delete(code);
        return RestUtil.success("激活成功！请前往登录页面登录");
    }

    /**
     * 修改用户
     * @param user 用户信息
     */
    public void altUser(User user){
        User user1 = userRepository.getById(user.getId());
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
        return userRepository.getById(userId);
    }

    /**
     * 删除用户
     * @param userId 用户id
     */
    public void delUser(String userId){
        userRepository.deleteById(userId);
    }

    /**
     * 清除过期未激活的用户
     * @param userId 用户Id
     */
    public void delUnActiveUser(String userId){
        User user = userRepository.findById(userId).orElse(null);
        if(null!=user&&0==user.getState()){
            userRepository.delete(user);
        }
    }

    /**
     * websocket 通知
     */
    public void notifyUser(@NotNull News news){
        //TODO 待完善
        //群发
        messageTemplate.convertAndSend("/topic/notify", "webSocket消息测试");
        //对某个用户发
        messageTemplate.convertAndSendToUser("ngcly","/queue/notify",news.getContent());
    }

    /**
     * 点赞/收藏
     * @param userId   用户ID
     * @param essayId  文章ID
     * @param type     类型
     */
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public void delUserFaves(String userId,String essayId,byte type){
        userFavesRepository.deleteUserFavesByUserIdAndAndEssayIdAndFaveType(userId,essayId,type);
    }

    /**
     * 关注
     * @param userId   用户ID
     * @param followId 被关注ID
     */
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public void delUserFollow(String userId,String followId){
        userFollowRepository.deleteUserFollowByUserIdAndAndFollowId(userId,followId);
    }

}
