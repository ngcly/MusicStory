package com.cn;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.cn.config.GlobalException;
import com.cn.config.RabbitConfig;
import com.cn.dao.*;
import com.cn.entity.*;
import com.cn.enums.SocialEnum;
import com.cn.enums.SocialParamEnum;
import com.cn.util.MailUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户 service 类
 * @author chen
 * @date 2018-01-02 18:20
 */
@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private SocialInfoRepository socialInfoRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private RedisTemplate<String,Long> redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private MailUtil mailUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username,username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return user;
    }

    /**
     * 注册账户
     * @param newUser 用户信息
     * @return string
     */
    public String signUp(User newUser){
        if(userRepository.existsByUsername(newUser.getUsername())) {
            throw new GlobalException(300,"用户名已存在");
        }

        if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new GlobalException(300,"该邮箱已注册");
        }

        newUser.setState(User.STATE_INITIALIZE);
        User result = userRepository.save(newUser);
        //生成激活码
        String code = System.currentTimeMillis()%100+result.getUsername();
        //以激活码为KEY 将用户ID保存到redis 有效期三小时
        redisTemplate.opsForValue().set(code,result.getId(),3,TimeUnit.HOURS);

        String subject="来自[音书]网站的激活邮件";
        String templateName = "activation.html";
        Dict context = Dict.create().set("username",result.getUsername()).set("code",code);
        //发送激活邮件
        mailUtil.sendAsyncMail(result.getEmail(),subject,templateName,context);
        //通过延迟队列 清除过期未激活账号
        rabbitTemplate.convertAndSend(RabbitConfig.DELAY_EXCHANGE,RabbitConfig.DELAY_ROUTING_KEY,result.getId(),msg->{
            msg.getMessageProperties().setHeader("x-delay", 3*60*60*1000);
            return msg;
        });
        return result.getUsername();
    }

    /**
     * 注册用户进行激活
     * @param code 激活码
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    public String activeUser(String code){
        //根据激活码 从redis获取用户ID信息
        Long id = redisTemplate.opsForValue().get(code);
        if(Objects.isNull(id)){
            throw new GlobalException(500, "激活码已过期或无效");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new GlobalException(500, "激活码错误");
        }
        User user = userOptional.get();
        user.setState(User.STATE_NORMAL);
        userRepository.save(user);
        redisTemplate.delete(code);
        return "激活成功！请前往登录页面登录";
    }

    /**
     * 用户登录 (三方登录)
     * @param source 三方资源名称
     * @param authCode 三方授权码
     * @param state 防 csrf 标识符
     * @return UserDetail
     */
    @Transactional(rollbackFor = Exception.class)
    public User socialLogin(String source,String authCode,String state){
        if(!SocialEnum.STATE.equals(state)){
            throw new GlobalException("state不一致");
        }
        SocialEnum socialEnum = SocialEnum.valueOf(source.toUpperCase());
        SocialInfo socialInfo = getSocialInfo(socialEnum,authCode);
        socialInfo.setSource(source);

        //根据OpenId 从数据库中查找是否有该用户信息 若有则直接使用用户信息 进行token生成操作
        SocialInfo thirdUser = socialInfoRepository.findByOpenId(socialInfo.getOpenId());
        if(Objects.isNull(thirdUser)){
            thirdUser = socialInfo;
            //若未登录过，则需要获取用户信息进行注册
            User user = getThirdUserInfo(socialInfo,socialEnum);
            //如果名称已存在 则添加随机数
            String username = user.getNickName();
            while (userRepository.existsByUsername(username)){
                username = user.getNickName() + RandomUtil.randomString(3);
            }
            user.setUsername(username);
            userRepository.save(user);

            thirdUser.setUser(user);
            socialInfoRepository.save(thirdUser);
        }
        User userDetail = socialInfo.getUser();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return userDetail;
    }

    /**
     * 三方账号绑定
     * @param source 三方资源名
     * @param authCode 三方授权码
     * @param state 防 csrf 标识符
     * @param currentUser 当前登陆用户
     */
    public void socialBinding(String source, String authCode, String state, User currentUser){
        if(!SocialEnum.STATE.equals(state)){
            throw new GlobalException("state不一致");
        }
        SocialEnum socialEnum = SocialEnum.valueOf(source.toUpperCase());
        SocialInfo thirdUser = getSocialInfo(socialEnum,authCode);
        thirdUser.setSource(source);
        thirdUser.setUser(currentUser);
        socialInfoRepository.save(thirdUser);
    }

    /**
     * 获取三方 access_token,open_id等信息
     * @param socialEnum 三方枚举
     * @param authCode 授权码
     * @return SocialInfo
     */
    public SocialInfo getSocialInfo(SocialEnum socialEnum,String authCode){
        SocialInfo social = new SocialInfo();
        if(SocialEnum.QQ.equals(socialEnum)){
            //通过Authorization Code获取Access Token
            String url = String.format("https://graph.qq.com/oauth2.0/token"+
                    "?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",socialEnum.getAppId(),socialEnum.getAppSecret(),authCode,SocialEnum.APP_REDIRECT+socialEnum.getSource());
            JSONObject tokenResult = restTemplate.getForObject(url, JSONObject.class);
            if(Objects.isNull(tokenResult)){
                throw new GlobalException("QQ获取access_token失败");
            }
            if(Objects.nonNull(tokenResult.getInt(SocialParamEnum.code.name()))){
                throw new GlobalException(tokenResult.getInt(SocialParamEnum.code.name()),tokenResult.getStr(SocialParamEnum.msg.name()));
            }
            social.setAccessToken(tokenResult.getStr(SocialParamEnum.access_token.name()));
            social.setExpireIn(tokenResult.getInt(SocialParamEnum.expires_in.name()));
            social.setRefreshToken(tokenResult.getStr(SocialParamEnum.refresh_token.name()));

            //获取回调后的 openid 值
            url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s",social.getAccessToken());
            JSONObject openidResult = restTemplate.getForObject(url, JSONObject.class);
            if(Objects.isNull(openidResult)){
                throw new GlobalException("QQ获取openid失败");
            }
            if(Objects.nonNull(openidResult.getInt(SocialParamEnum.code.name()))){
                throw new GlobalException(openidResult.getInt(SocialParamEnum.code.name()),openidResult.getStr(SocialParamEnum.msg.name()));
            }
            String openid = openidResult.getStr(SocialParamEnum.openid.name());
            social.setOpenId(openid);
        }else{
            //微信 通过code获取access_token
            String wechatUrl = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",socialEnum.getAppId(),socialEnum.getAppSecret(),authCode);
            JSONObject json = restTemplate.getForObject(wechatUrl,JSONObject.class);
            if(Objects.isNull(json)){
                throw new GlobalException("微信获取access_token失败");
            }
            if(Objects.nonNull(json.getInt(SocialParamEnum.errcode.name()))){
                throw new GlobalException(json.getInt(SocialParamEnum.errcode.name()),json.getStr(SocialParamEnum.errmsg.name()));
            }
            social.setAccessToken(json.getStr(SocialParamEnum.access_token.name()));
            social.setExpireIn(json.getInt(SocialParamEnum.expires_in.name()));
            social.setOpenId(json.getStr(SocialParamEnum.openid.name()));
            social.setScope(json.getStr(SocialParamEnum.scope.name()));
        }
        return social;
    }

    /**
     * 获取三方用户信息
     * @param social 三方 access_token open_id等信息
     * @param socialEnum 三方枚举
     * @return User
     */
    public User getThirdUserInfo(SocialInfo social,SocialEnum socialEnum){
        User user = new User();
        user.setState(User.STATE_NORMAL);
        String userInfoUrl;
        JSONObject userInfo;
        if(SocialEnum.QQ.equals(socialEnum)){
            //获取QQ用户信息
            userInfoUrl = String.format("https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s",social.getAccessToken(),socialEnum.getAppId(),social.getOpenId());
            userInfo = restTemplate.getForObject(userInfoUrl,JSONObject.class);
            if(Objects.isNull(userInfo)){
                throw new GlobalException("获取QQ用户信息失败");
            }
            if(userInfo.getInt(SocialParamEnum.ret.name())!=0){
                throw new GlobalException(userInfo.getInt(SocialParamEnum.ret.name()),userInfo.getStr(SocialParamEnum.msg.name()));
            }
            user.setNickName(userInfo.getStr(SocialParamEnum.nickname.name()));
            user.setAvatar(userInfo.getStr(SocialParamEnum.figureurl_qq_1.name()));
            user.setGender(userInfo.getByte(SocialParamEnum.gender.name()));
        }else{
            //获取微信用户信息
            userInfoUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",social.getAccessToken(),social.getOpenId());
            userInfo = restTemplate.getForObject(userInfoUrl,JSONObject.class);
            if(Objects.isNull(userInfo)){
                throw new GlobalException("获取微信用户信息失败");
            }
            if(Objects.nonNull(userInfo.getInt(SocialParamEnum.errcode.name()))){
                throw new GlobalException(userInfo.getInt(SocialParamEnum.errcode.name()),userInfo.getStr(SocialParamEnum.errmsg.name()));
            }
            social.setUnionId(userInfo.getStr(SocialParamEnum.unionid.name()));

            user.setNickName(userInfo.getStr(SocialParamEnum.nickname.name()));
            user.setGender(userInfo.getByte(SocialParamEnum.sex.name()));
            user.setAvatar(userInfo.getStr(SocialParamEnum.headimgurl.name()));
            user.setAddress(userInfo.getStr(SocialParamEnum.country.name())+userInfo.getStr(SocialParamEnum.province.name())+userInfo.getStr(SocialParamEnum.city.name()));
        }
        return user;
    }

    /**
     * 获取三方授权跳转的 url
     * @param source 三方标志
     * @return String url地址
     */
    public String getSocialRedirectUrl(String source){
        SocialEnum socialEnum = SocialEnum.valueOf(source.toUpperCase());
        String url;
        if(SocialEnum.QQ.equals(socialEnum)){
            url = String.format("https://graph.qq.com/oauth2.0/authorize?" +
                    "response_type=code&client_id=%s&redirect_uri=%s&state=%s", socialEnum.getAppId(), SocialEnum.APP_REDIRECT+source, SocialEnum.STATE);
        }else{
            url = String.format("https://open.weixin.qq.com/connect/qrconnect?" +
                    "appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s",socialEnum.getAppId(), SocialEnum.APP_REDIRECT+source, SocialEnum.STATE);
        }
        return url;
    }

    /**
     * 解绑三方用户信息
     * @param openId openid
     */
    public void revokeSocial(String openId){
        socialInfoRepository.deleteByOpenId(openId);
    }

    /**
     * 修改用户
     * @param user 用户信息
     */
    public void altUser(User user){
        User altUser = userRepository.getById(user.getId());
        altUser.setState(user.getState());
        Set<Role> allRole = roleRepository.getAllByAvailableIsTrueAndRoleType(Role.ROLE_TYPE_CUSTOMER);
        if(Objects.nonNull(user.getRoleIds())){
            for(Long roleId:user.getRoleIds()){
                allRole.stream().filter(role -> role.getId().equals(roleId)).forEach(altUser.getRoleList()::add);
            }
        }
        userRepository.save(altUser);
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
    public User getUserDetail(Long userId){
        return userRepository.getById(userId);
    }

    /**
     * 删除用户
     * @param userId 用户id
     */
    public void delUser(Long userId){
        userRepository.deleteById(userId);
    }

    /**
     * 清除过期未激活的用户
     * @param userId 用户Id
     */
    public void delUnActiveUser(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(Objects.nonNull(user) && User.STATE_INITIALIZE == user.getState()){
            userRepository.delete(user);
        }
    }

}
