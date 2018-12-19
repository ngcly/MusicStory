package com.cn.controller;

import com.cn.config.CurrentUser;
import com.cn.config.CustomerDetail;
import com.cn.util.RestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 会员控制层
 *
 * @author chen
 * @date 2018-01-05 12:08
 */
@Api(value = "UserController", description = "用户相关API")
@RestController
@RequestMapping("/user")
public class UserController {
    //<a href="https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101386962&redirect_uri=http://www.ictgu.cn/login/qq&state=test" class="btn btn-primary btn-block">QQ登录</a>

    /**
     * 获取 用户信息
     * @param customerDetail
     * @return
     */
    @ApiOperation(value = "用户信息", notes = "获取用户详情信息")
    @GetMapping("/info")
    public ModelMap userInfo(@CurrentUser CustomerDetail customerDetail) {
        return RestUtil.Success(customerDetail);
    }

    /**
     * 获取用户的文章
     * @return
     */
    @ApiOperation(value = "我的文章", notes = "获取用户写的文章")
    @GetMapping("/essay")
    public ModelMap getUserEssay(@CurrentUser CustomerDetail customerDetail){
        return RestUtil.Success("文字");
    }

    //TODO 创建文章，删文章

    //TODO 获取用户消息 发送消息

    //TODO 获取用户的收藏 点赞文章  点赞 收藏 取消点赞/收藏

    //TODO 获取用户关注的人 及关注用户的人  关注  取消关注
}