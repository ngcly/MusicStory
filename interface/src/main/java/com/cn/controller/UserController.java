package com.cn.controller;

import com.cn.config.CurrentUser;
import com.cn.config.CustomerDetail;
import com.cn.entity.Essay;
import com.cn.entity.News;
import com.cn.entity.User;
import com.cn.util.RestUtil;
import io.swagger.annotations.Api;
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
        //TODO
        return RestUtil.Success("文字");
    }

    @ApiOperation(value = "写文章", notes = "用户发表文章")
    @PostMapping("/essay")
    public ModelMap createEssay(@RequestBody Essay essay){
        //TODO
        return null;
    }

    @ApiOperation(value = "修改文章", notes = "用户修改文章")
    @PutMapping("/essay")
    public ModelMap updateEssay(@RequestBody Essay essay){
        //TODO
        return null;
    }

    @ApiOperation(value = "删文章", notes = "根据文章ID删除用户文章")
    @DeleteMapping("/essay/{id}")
    public ModelMap deleteEssay(@PathVariable("id")String id){
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户消息", notes = "获取当前用户的所有消息")
    @GetMapping("/new")
    public ModelMap getMyNews(@CurrentUser User user){
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户消息", notes = "获取当前用户的所有消息")
    @PostMapping("/new")
    public ModelMap sendNews(@CurrentUser User user, @RequestBody News news){
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户点赞的文章", notes = "获取当前用户点赞的所有文章")
    @GetMapping("/star")
    public ModelMap getMyStar(@CurrentUser User user){
        //TODO
        return null;
    }

    @ApiOperation(value = "点赞", notes = "用户点赞文章")
    @PostMapping("/star")
    public ModelMap star(@CurrentUser User user,@RequestBody String essayId){
        //TODO
        return null;
    }

    @ApiOperation(value = "取消点赞", notes = "用户取消点赞文章")
    @DeleteMapping("/star")
    public ModelMap cancelStar(@CurrentUser User user,@RequestBody String essayId){
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户收藏的文章", notes = "获取当前用户收藏的所有文章")
    @GetMapping("/collect")
    public ModelMap getMyCollect(@CurrentUser User user){
        //TODO
        return null;
    }

    @ApiOperation(value = "收藏", notes = "用户收藏文章")
    @PostMapping("/collect")
    public ModelMap collect(@CurrentUser User user,@RequestBody String essayId){
        //TODO
        return null;
    }

    @ApiOperation(value = "取消收藏", notes = "用户取消收藏文章")
    @DeleteMapping("/star")
    public ModelMap cancelCollect(@CurrentUser User user,@RequestBody String essayId){
        //TODO
        return null;
    }

    @ApiOperation(value = "获取关注我的用户", notes = "获取关注当前用户的所有人")
    @GetMapping("/follow")
    public ModelMap getFollowMe(@CurrentUser User user){
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户关注的人", notes = "获取当前用户关注的所有人")
    @GetMapping("/watch")
    public ModelMap getMyWatches(@CurrentUser User user){
        //TODO
        return null;
    }

    @ApiOperation(value = "关注", notes = "关注某个用户")
    @PostMapping("/watch")
    public ModelMap watch(@CurrentUser User user,@RequestBody String userId){
        //TODO
        return null;
    }

    @ApiOperation(value = "取消关注", notes = "取消关注某个用户")
    @DeleteMapping("/watch")
    public ModelMap cancelWatch(@CurrentUser User user,@RequestBody String userId){
        //TODO
        return null;
    }
}