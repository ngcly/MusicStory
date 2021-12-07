package com.cn.controller;

import com.cn.EssayService;
import com.cn.UserRelatedService;
import com.cn.UserService;
import com.cn.pojo.EssayDTO;
import com.cn.entity.*;
import com.cn.pojo.UserVO;
import com.cn.util.RestUtil;
import com.cn.util.UploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 会员控制层
 * @author ngcly
 * @since 2018-01-05 12:08
 */
@Tag(name = "UserController", description = "用户相关API")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRelatedService userRelatedService;
    private final EssayService essayService;

    @Operation(summary = "用户信息", description = "获取用户详情信息")
    @GetMapping("/info")
    public ModelMap userInfo(@AuthenticationPrincipal User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return RestUtil.success(vo);
    }

    /**
     * 绑定三方账号
     * @param source 三方类型标识
     * @param request 请求request
     * @return ModelMap
     */
    @Operation(summary = "登录", description = "三方用户登录")
    @GetMapping("/binding/{source}")
    public ModelMap bindingSocial(@AuthenticationPrincipal User user, @PathVariable("source") String source,
                                  HttpServletRequest request){
        userService.socialBinding(source, request.getParameter("code"), request.getParameter("state"), user);
        return RestUtil.success();
    }

    @Operation(summary = "我的文章", description = "获取用户写的文章")
    @GetMapping("/essay/{page}/{size}")
    @Parameters({
            @Parameter(name = "page", description = "页数", in = ParameterIn.PATH),
            @Parameter(name = "size", description = "条数", in = ParameterIn.PATH),
    })
    public ModelMap getUserEssay(@AuthenticationPrincipal User user, @PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size) {
        return RestUtil.success(essayService.getUserEssayList(PageRequest.of(page - 1, size), user.getId()));
    }

    @Operation(summary = "写文章", description = "用户保存草稿")
    @PostMapping("/essay")
    public ModelMap createEssay(@AuthenticationPrincipal User user, @Valid @RequestBody EssayDTO essayDTO) {
        Essay essay = new Essay();
        BeanUtils.copyProperties(essayDTO,essay);
        essay.setUser(user);
        return RestUtil.success(essayService.createEssay(essayDTO.getClassifyId(), essay));
    }

    @Operation(summary = "发表文章", description = "用户发表文章")
    @PutMapping("/essay")
    public ModelMap updateEssay(@AuthenticationPrincipal User user, @Valid @RequestBody EssayDTO essayDTO) {
        Essay essay = new Essay();
        BeanUtils.copyProperties(essayDTO,essay);
        essay.setUser(user);
        essayService.updateEssay(essayDTO.getClassifyId(), essay);
        return RestUtil.success();
    }

    @Operation(summary = "删文章", description = "根据文章ID删除用户文章")
    @DeleteMapping("/essay/{id}")
    public ModelMap deleteEssay(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        return essayService.delUserEssay(user.getId(), id)?RestUtil.success():RestUtil.failure(500,"删除失败");
    }

    @Operation(summary = "评论文章", description = "用户评论文章")
    @PostMapping("/comment")
    public ModelMap commentEssay(@AuthenticationPrincipal User user, @Valid @RequestBody Comment comment) {
        essayService.addComments(user.getId(), comment);
        return RestUtil.success();
    }

    @Operation(summary = "获取用户消息", description = "获取当前用户的所有消息")
    @GetMapping("/new")
    public ModelMap getMyNews(@AuthenticationPrincipal User user) {
        //TODO
        return null;
    }

    @Operation(summary = "发送消息", description = "给某人发送消息")
    @PostMapping("/new")
    public ModelMap sendNews(@AuthenticationPrincipal User user, @RequestBody News news) {
        //TODO
        return null;
    }

    @Operation(summary = "获取用户点赞的文章", description = "获取当前用户点赞的所有文章")
    @GetMapping("/star/{page}/{size}")
    public ModelMap getMyStar(@AuthenticationPrincipal User user,@PathVariable("page") Integer page,
                              @PathVariable("size") Integer size) {
        return RestUtil.success(essayService.getUserFavesEssay(user.getId(),UserFaves.FAVE_TYPE_LIKE,PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "点赞", description = "用户点赞文章")
    @PostMapping("/star")
    public ModelMap star(@AuthenticationPrincipal User user, @RequestBody Long essayId) {
        userRelatedService.addUserFaves(user.getId(), essayId, UserFaves.FAVE_TYPE_LIKE);
        return RestUtil.success();
    }

    @Operation(summary = "取消点赞", description = "用户取消点赞文章")
    @DeleteMapping("/star/{essayId}")
    public ModelMap cancelStar(@AuthenticationPrincipal User user, @PathVariable("essayId") Long essayId) {
        userRelatedService.delUserFaves(user.getId(), essayId, UserFaves.FAVE_TYPE_LIKE);
        return RestUtil.success();
    }

    @Operation(summary = "获取用户收藏的文章", description = "获取当前用户收藏的所有文章")
    @GetMapping("/collect/{page}/{size}")
    public ModelMap getMyCollect(@AuthenticationPrincipal User user,@PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size) {
        return RestUtil.success(essayService.getUserFavesEssay(user.getId(),UserFaves.FAVE_TYPE_COLLECT,PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "收藏", description = "用户收藏文章")
    @PostMapping("/collect")
    public ModelMap collect(@AuthenticationPrincipal User user, @RequestBody Long essayId) {
        userRelatedService.addUserFaves(user.getId(), essayId, UserFaves.FAVE_TYPE_COLLECT);
        return RestUtil.success();
    }

    @Operation(summary = "取消收藏", description = "用户取消收藏文章")
    @DeleteMapping("/collect/{essayId}")
    public ModelMap cancelCollect(@AuthenticationPrincipal User user, @PathVariable("essayId") Long essayId) {
        userRelatedService.delUserFaves(user.getId(), essayId, UserFaves.FAVE_TYPE_COLLECT);
        return RestUtil.success();

    }

    @Operation(summary = "获取关注我的用户", description = "获取关注当前用户的所有人")
    @GetMapping("/follow/{page}/{size}")
    @Parameters({
            @Parameter(name = "page", description = "页数", in = ParameterIn.PATH),
            @Parameter(name = "size", description = "条数", in = ParameterIn.PATH),
    })
    public ModelMap getFollowMe(@AuthenticationPrincipal User user,@PathVariable("page") Integer page,
                                @PathVariable("size") Integer size) {
        return RestUtil.success(userRelatedService.getFollowMeList(user.getId(),PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "获取用户关注的人", description = "获取当前用户关注的所有人")
    @GetMapping("/watch/{page}/{size}")
    @Parameters({
            @Parameter(name = "page", description = "页数", in = ParameterIn.PATH),
            @Parameter(name = "size", description = "条数", in = ParameterIn.PATH),
    })
    public ModelMap getMyWatches(@AuthenticationPrincipal User user, @PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size) {
        return RestUtil.success(userRelatedService.getMyFollowList(user.getId(),PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "关注", description = "关注某个用户")
    @PostMapping("/watch")
    public ModelMap watch(@AuthenticationPrincipal User user, @RequestBody Long userId) {
        userRelatedService.addUserFollow(user.getId(), userId);
        return RestUtil.success();
    }

    @Operation(summary = "取消关注", description = "取消关注某个用户")
    @DeleteMapping("/watch/{userId}")
    public ModelMap cancelWatch(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId) {
        userRelatedService.delUserFollow(user.getId(), userId);
        return RestUtil.success();
    }

    @Operation(summary = "文件上传", description = "文件上传接口")
    @PostMapping("/upload/{dir}")
    @Parameters({
            @Parameter(name = "dir", description = "oss分类名", in = ParameterIn.PATH),
    })
    public ModelMap uploadAvatar(@RequestParam("file") MultipartFile file,@PathVariable("dir")String dir){
        if(file.isEmpty()){
            return RestUtil.failure(222,"文件为空");
        }
        String path = UploadUtil.uploadFileByAli(file,dir);
        return RestUtil.success(path);
    }
}