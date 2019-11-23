package com.cn.controller;

import com.cn.EssayService;
import com.cn.UserService;
import com.cn.pojo.UserDetail;
import com.cn.pojo.RestCode;
import com.cn.entity.*;
import com.cn.pojo.UserVO;
import com.cn.util.RestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


/**
 * 会员控制层
 *
 * @author ngcly
 * @date 2018-01-05 12:08
 */
@Api(value = "UserController", tags = "用户相关API",authorizations = @Authorization(value = "user"))
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    EssayService essayService;
    @Autowired
    UserService userService;

    @ApiOperation(value = "用户信息", notes = "获取用户详情信息")
    @GetMapping("/info")
    public ModelMap userInfo(Authentication authentication) {
        UserDetail user = (UserDetail) authentication.getPrincipal();
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user,vo);
        return RestUtil.success(vo);
    }

    /**
     * 获取用户的文章
     * @return
     */
    @ApiOperation(value = "我的文章", notes = "获取用户写的文章")
    @GetMapping("/essay/{page}/{size}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数",paramType = "path",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "size",value = "条数",paramType = "path",dataType = "int",defaultValue = "10"),
    })
    public ModelMap getUserEssay(Authentication authentication,@PathVariable("page")Integer page,
                                 @PathVariable("size")Integer size){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        return essayService.getUserEssayList(PageRequest.of(page - 1, size),user.getId());
    }

    @ApiOperation(value = "写文章", notes = "用户发表文章")
    @PostMapping("/essay")
    public ModelMap createEssay(Authentication authentication, @RequestBody Essay essay){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        return essayService.createEssay(user,essay);
    }

    @ApiOperation(value = "修改文章", notes = "用户修改文章")
    @PutMapping("/essay")
    public ModelMap updateEssay(@RequestBody Essay essay){
        return essayService.updateEssay(essay);
    }

    @ApiOperation(value = "删文章", notes = "根据文章ID删除用户文章")
    @DeleteMapping("/essay/{id}")
    public ModelMap deleteEssay(Authentication authentication,@PathVariable("id")String id){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        return essayService.delUserEssay(user.getId(),id);
    }

    @ApiOperation(value = "评论文章", notes = "用户评论文章")
    @PostMapping("/comment")
    public ModelMap commentEssay(Authentication authentication,@RequestBody Comment comment){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        return essayService.addComments(user.getId(),comment);
    }

    @ApiOperation(value = "获取用户消息", notes = "获取当前用户的所有消息")
    @GetMapping("/new")
    public ModelMap getMyNews(Authentication authentication){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        //TODO
        return null;
    }

    @ApiOperation(value = "发送消息", notes = "给某人发送消息")
    @PostMapping("/new")
    public ModelMap sendNews(Authentication authentication, @RequestBody News news){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户点赞的文章", notes = "获取当前用户点赞的所有文章")
    @GetMapping("/star")
    public ModelMap getMyStar(Authentication authentication){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        //TODO
        return null;
    }

    @ApiOperation(value = "点赞", notes = "用户点赞文章")
    @PostMapping("/star")
    public ModelMap star(Authentication authentication,@RequestBody String essayId){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        userService.addUserFaves(user.getId(),essayId, UserFaves.点赞);
        return RestUtil.success();
    }

    @ApiOperation(value = "取消点赞", notes = "用户取消点赞文章")
    @DeleteMapping("/star/{essayId}")
    public ModelMap cancelStar(Authentication authentication,@PathVariable("essayId") String essayId){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        userService.delUserFaves(user.getId(),essayId, UserFaves.点赞);
        return RestUtil.success();
    }

    @ApiOperation(value = "获取用户收藏的文章", notes = "获取当前用户收藏的所有文章")
    @GetMapping("/collect")
    public ModelMap getMyCollect(Authentication authentication){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        //TODO
        return null;
    }

    @ApiOperation(value = "收藏", notes = "用户收藏文章")
    @PostMapping("/collect")
    public ModelMap collect(Authentication authentication,@RequestBody String essayId){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        userService.addUserFaves(user.getId(),essayId, UserFaves.收藏);
        return RestUtil.success();
    }

    @ApiOperation(value = "取消收藏", notes = "用户取消收藏文章")
    @DeleteMapping("/collect/{essayId}")
    public ModelMap cancelCollect(Authentication authentication,@PathVariable("essayId") String essayId){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        userService.delUserFaves(user.getId(),essayId, UserFaves.收藏);
        return RestUtil.success();

    }

    @ApiOperation(value = "获取关注我的用户", notes = "获取关注当前用户的所有人")
    @GetMapping("/follow")
    public ModelMap getFollowMe(Authentication authentication){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        //TODO
        return null;
    }

    @ApiOperation(value = "获取用户关注的人", notes = "获取当前用户关注的所有人")
    @GetMapping("/watch")
    public ModelMap getMyWatches(Authentication authentication){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        //TODO
        return null;
    }

    @ApiOperation(value = "关注", notes = "关注某个用户")
    @PostMapping("/watch")
    public ModelMap watch(Authentication authentication,@RequestBody String userId){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        userService.addUserFollow(user.getId(),userId);
        return RestUtil.success();
    }

    @ApiOperation(value = "取消关注", notes = "取消关注某个用户")
    @DeleteMapping("/watch/{userId}")
    public ModelMap cancelWatch(Authentication authentication,@PathVariable("userId") String userId){
        UserDetail user = (UserDetail) authentication.getPrincipal();
        userService.delUserFollow(user.getId(),userId);
        return RestUtil.success();
    }
}