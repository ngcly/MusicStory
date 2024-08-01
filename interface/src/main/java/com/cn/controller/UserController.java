package com.cn.controller;

import com.cn.EssayService;
import com.cn.UserRelatedService;
import com.cn.UserService;
import com.cn.enums.FaveTypeEnum;
import com.cn.model.EssayDTO;
import com.cn.entity.*;
import com.cn.model.UserVO;
import com.cn.util.Result;
import com.cn.util.UploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * 会员控制层
 *
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
    public Result<UserVO> userInfo(@AuthenticationPrincipal User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return Result.success(vo);
    }

    /**
     * 绑定三方账号
     *
     * @param source  三方类型标识
     * @param request 请求request
     * @return ModelMap
     */
    @Operation(summary = "绑定账号", description = "用户绑定三方账号")
    @GetMapping("/binding/{source}")
    public Result<Void> bindingSocial(@AuthenticationPrincipal User user, @PathVariable("source") String source,
                                      HttpServletRequest request) {
        userService.socialBinding(source, request.getParameter("code"), request.getParameter("state"), user);
        return Result.success();
    }

    @Operation(summary = "我的文章", description = "获取用户写的文章")
    @GetMapping("/essay/{page}/{size}")
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    @Parameter(name = "size", description = "条数", in = ParameterIn.PATH)
    public Result<List<Essay>> getUserEssay(@AuthenticationPrincipal User user, @PathVariable("page") Integer page,
                                            @PathVariable("size") Integer size) {
        return Result.success(essayService.getUserEssayList(PageRequest.of(page - 1, size), user.getId()));
    }

    @Operation(summary = "写文章", description = "用户保存草稿")
    @PostMapping("/essay")
    public Result<Long> createEssay(@AuthenticationPrincipal User user, @Valid @RequestBody EssayDTO essayDTO) {
        Essay essay = new Essay();
        BeanUtils.copyProperties(essayDTO, essay);
        essay.setUser(user);
        return Result.success(essayService.createEssay(essayDTO.getClassifyId(), essay));
    }

    @Operation(summary = "发表文章", description = "用户发表文章")
    @PutMapping("/essay")
    public Result<Void> updateEssay(@AuthenticationPrincipal User user, @Valid @RequestBody EssayDTO essayDTO) {
        Essay essay = new Essay();
        BeanUtils.copyProperties(essayDTO, essay);
        essay.setUser(user);
        essayService.updateEssay(essayDTO.getClassifyId(), essay);
        return Result.success();
    }

    @Operation(summary = "删文章", description = "根据文章ID删除用户文章")
    @DeleteMapping("/essay/{id}")
    public Result<Void> deleteEssay(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        return essayService.delUserEssay(user.getId(), id) ? Result.success() : Result.failure(500, "删除失败");
    }

    @Operation(summary = "评论文章", description = "用户评论文章")
    @PostMapping("/comment")
    public Result<Void> commentEssay(@AuthenticationPrincipal User user, @Valid @RequestBody Comment comment) {
        essayService.addComments(user.getId(), comment);
        return Result.success();
    }

    @Operation(summary = "获取用户消息", description = "获取当前用户的所有消息")
    @GetMapping("/new")
    public Result<List<News>> getMyNews(@AuthenticationPrincipal User user) {
        //TODO
        return null;
    }

    @Operation(summary = "发送消息", description = "给某人发送消息")
    @PostMapping("/new")
    public Result<Void> sendNews(@AuthenticationPrincipal User user, @RequestBody News news) {
        //TODO
        return null;
    }

    @Operation(summary = "获取用户点赞的文章", description = "获取当前用户点赞的所有文章")
    @GetMapping("/star/{page}/{size}")
    public Result<Page<Essay>> getMyStar(@AuthenticationPrincipal User user, @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size) {
        return Result.success(essayService.getUserFavesEssay(user.getId(), FaveTypeEnum.LIKE, PageRequest.of(page - 1
                , size)));
    }

    @Operation(summary = "点赞", description = "用户点赞文章")
    @PostMapping("/star")
    public Result<Void> star(@AuthenticationPrincipal User user, @RequestBody Long essayId) {
        userRelatedService.addUserFaves(user.getId(), essayId, FaveTypeEnum.LIKE);
        return Result.success();
    }

    @Operation(summary = "取消点赞", description = "用户取消点赞文章")
    @DeleteMapping("/star/{essayId}")
    public Result<Void> cancelStar(@AuthenticationPrincipal User user, @PathVariable("essayId") Long essayId) {
        userRelatedService.delUserFaves(user.getId(), essayId, FaveTypeEnum.LIKE);
        return Result.success();
    }

    @Operation(summary = "获取用户收藏的文章", description = "获取当前用户收藏的所有文章")
    @GetMapping("/collect/{page}/{size}")
    public Result<Page<Essay>> getMyCollect(@AuthenticationPrincipal User user, @PathVariable("page") Integer page,
                                            @PathVariable("size") Integer size) {
        return Result.success(essayService.getUserFavesEssay(user.getId(), FaveTypeEnum.COLLECT,
                PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "收藏", description = "用户收藏文章")
    @PostMapping("/collect")
    public Result<Void> collect(@AuthenticationPrincipal User user, @RequestBody Long essayId) {
        userRelatedService.addUserFaves(user.getId(), essayId, FaveTypeEnum.COLLECT);
        return Result.success();
    }

    @Operation(summary = "取消收藏", description = "用户取消收藏文章")
    @DeleteMapping("/collect/{essayId}")
    public Result<Void> cancelCollect(@AuthenticationPrincipal User user, @PathVariable("essayId") Long essayId) {
        userRelatedService.delUserFaves(user.getId(), essayId, FaveTypeEnum.COLLECT);
        return Result.success();

    }

    @Operation(summary = "获取关注我的用户", description = "获取关注当前用户的所有人")
    @GetMapping("/follow/{page}/{size}")
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    @Parameter(name = "size", description = "条数", in = ParameterIn.PATH)
    public Result<List<Map<String, Object>>> getFollowMe(@AuthenticationPrincipal User user,
                                                         @PathVariable("page") Integer page,
                                                         @PathVariable("size") Integer size) {
        return Result.success(userRelatedService.getFollowMeList(user.getId(), PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "获取用户关注的人", description = "获取当前用户关注的所有人")
    @GetMapping("/watch/{page}/{size}")
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    @Parameter(name = "size", description = "条数", in = ParameterIn.PATH)
    public Result<List<Map<String, Object>>> getMyWatches(@AuthenticationPrincipal User user,
                                                          @PathVariable("page") Integer page,
                                                          @PathVariable("size") Integer size) {
        return Result.success(userRelatedService.getMyFollowList(user.getId(), PageRequest.of(page - 1, size)));
    }

    @Operation(summary = "关注", description = "关注某个用户")
    @PostMapping("/watch")
    public Result<Void> watch(@AuthenticationPrincipal User user, @RequestBody Long userId) {
        userRelatedService.addUserFollow(user.getId(), userId);
        return Result.success();
    }

    @Operation(summary = "取消关注", description = "取消关注某个用户")
    @DeleteMapping("/watch/{userId}")
    public Result<Void> cancelWatch(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId) {
        userRelatedService.delUserFollow(user.getId(), userId);
        return Result.success();
    }

    @Operation(summary = "文件上传", description = "文件上传接口")
    @PostMapping("/upload/{dir}")
    @Parameter(name = "dir", description = "oss分类名", in = ParameterIn.PATH)
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("dir") String dir) {
        if (file.isEmpty()) {
            return Result.failure(222, "文件为空");
        }
        String path = UploadUtil.uploadFileByAli(file, dir);
        return Result.success(path);
    }
}