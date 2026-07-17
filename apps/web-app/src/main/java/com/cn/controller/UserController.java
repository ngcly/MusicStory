package com.cn.controller;
 
import com.cn.content.EssayService;
import com.cn.relation.UserRelatedService;
import com.cn.user.UserService;
import com.cn.enums.FaveTypeEnum;
import com.cn.model.EssayDTO;
import com.cn.entity.*;
import com.cn.model.UserVO;
import com.cn.service.StorageService;
import com.cn.exception.GlobalException;
import com.cn.model.RestCode;
import com.cn.config.SecurityUser;
import com.cn.user.domain.User;
import com.cn.persistence.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    private final StorageService storageService;
 
    @Operation(summary = "用户信息", description = "获取用户详情信息")
    @GetMapping("/info")
    public UserVO userInfo(@AuthenticationPrincipal SecurityUser securityUser) {
        User user = securityUser.getUser();
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    /**
     * 绑定三方账号
     *
     * @param source  三方类型标识
     * @param request 请求request
     */
    @Operation(summary = "绑定账号", description = "用户绑定三方账号")
    @GetMapping("/binding/{source}")
    public ResponseEntity<Void> bindingSocial(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("source") String source,
                                              HttpServletRequest request) {
        User user = securityUser.getUser();
        userService.socialBinding(source, request.getParameter("code"), request.getParameter("state"), user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "我的文章", description = "获取用户写的文章")
    @GetMapping("/essay/{page}/{size}")
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    @Parameter(name = "size", description = "条数", in = ParameterIn.PATH)
    public List<Essay> getUserEssay(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("page") Integer page,
                                            @PathVariable("size") Integer size) {
        User user = securityUser.getUser();
        return essayService.getUserEssayList(PageRequest.of(page - 1, size), user.getId());
    }

    @Operation(summary = "写文章", description = "用户保存草稿")
    @PostMapping("/essay")
    public Long createEssay(@AuthenticationPrincipal SecurityUser securityUser, @Valid @RequestBody EssayDTO essayDTO) {
        User user = securityUser.getUser();
        Essay essay = new Essay();
        BeanUtils.copyProperties(essayDTO, essay);
        essay.setUser(UserMapper.toEntity(user));
        return essayService.createEssay(essayDTO.getClassifyId(), essay);
    }

    @Operation(summary = "发表文章", description = "用户发表文章")
    @PutMapping("/essay")
    public ResponseEntity<Void> updateEssay(@AuthenticationPrincipal SecurityUser securityUser, @Valid @RequestBody EssayDTO essayDTO) {
        User user = securityUser.getUser();
        Essay essay = new Essay();
        BeanUtils.copyProperties(essayDTO, essay);
        essay.setUser(UserMapper.toEntity(user));
        essayService.updateEssay(user.getId(), essayDTO.getClassifyId(), essay);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "删文章", description = "根据文章ID删除用户文章")
    @DeleteMapping("/essay/{id}")
    public ResponseEntity<Void> deleteEssay(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("id") Long id) {
        User user = securityUser.getUser();
        if (!essayService.delUserEssay(user.getId(), id)) {
            throw new GlobalException(RestCode.SERVER_ERROR.code, "删除失败");
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "评论文章", description = "用户评论文章")
    @PostMapping("/comment")
    public ResponseEntity<Void> commentEssay(@AuthenticationPrincipal SecurityUser securityUser, @Valid @RequestBody Comment comment) {
        User user = securityUser.getUser();
        essayService.addComments(user.getId(), comment);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取用户消息", description = "获取当前用户的所有消息")
    @GetMapping("/new")
    public List<News> getMyNews(@AuthenticationPrincipal SecurityUser securityUser) {
        //TODO
        return null;
    }

    @Operation(summary = "发送消息", description = "给某人发送消息")
    @PostMapping("/new")
    public ResponseEntity<Void> sendNews(@AuthenticationPrincipal SecurityUser securityUser, @RequestBody News news) {
        //TODO
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取用户点赞的文章", description = "获取当前用户点赞的所有文章")
    @GetMapping("/star/{page}/{size}")
    public Page<Essay> getMyStar(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size) {
        User user = securityUser.getUser();
        return essayService.getUserFavesEssay(user.getId(), FaveTypeEnum.LIKE, PageRequest.of(page - 1, size));
    }

    @Operation(summary = "点赞", description = "用户点赞文章")
    @PostMapping("/star")
    public ResponseEntity<Void> star(@AuthenticationPrincipal SecurityUser securityUser, @RequestBody Long essayId) {
        User user = securityUser.getUser();
        userRelatedService.addUserFaves(user.getId(), essayId, FaveTypeEnum.LIKE);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "取消点赞", description = "用户取消点赞文章")
    @DeleteMapping("/star/{essayId}")
    public ResponseEntity<Void> cancelStar(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("essayId") Long essayId) {
        User user = securityUser.getUser();
        userRelatedService.delUserFaves(user.getId(), essayId, FaveTypeEnum.LIKE);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取用户收藏的文章", description = "获取当前用户收藏的所有文章")
    @GetMapping("/collect/{page}/{size}")
    public Page<Essay> getMyCollect(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("page") Integer page,
                                            @PathVariable("size") Integer size) {
        User user = securityUser.getUser();
        return essayService.getUserFavesEssay(user.getId(), FaveTypeEnum.COLLECT, PageRequest.of(page - 1, size));
    }

    @Operation(summary = "收藏", description = "用户收藏文章")
    @PostMapping("/collect")
    public ResponseEntity<Void> collect(@AuthenticationPrincipal SecurityUser securityUser, @RequestBody Long essayId) {
        User user = securityUser.getUser();
        userRelatedService.addUserFaves(user.getId(), essayId, FaveTypeEnum.COLLECT);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "取消收藏", description = "用户取消收藏文章")
    @DeleteMapping("/collect/{essayId}")
    public ResponseEntity<Void> cancelCollect(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("essayId") Long essayId) {
        User user = securityUser.getUser();
        userRelatedService.delUserFaves(user.getId(), essayId, FaveTypeEnum.COLLECT);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取关注我的用户", description = "获取关注当前用户的所有人")
    @GetMapping("/follow/{page}/{size}")
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    @Parameter(name = "size", description = "条数", in = ParameterIn.PATH)
    public List<Map<String, Object>> getFollowMe(@AuthenticationPrincipal SecurityUser securityUser,
                                                         @PathVariable("page") Integer page,
                                                         @PathVariable("size") Integer size) {
        User user = securityUser.getUser();
        return userRelatedService.getFollowMeList(user.getId(), PageRequest.of(page - 1, size));
    }

    @Operation(summary = "获取用户关注的人", description = "获取当前用户关注的所有人")
    @GetMapping("/watch/{page}/{size}")
    @Parameter(name = "page", description = "页数", in = ParameterIn.PATH)
    @Parameter(name = "size", description = "条数", in = ParameterIn.PATH)
    public List<Map<String, Object>> getMyWatches(@AuthenticationPrincipal SecurityUser securityUser,
                                                          @PathVariable("page") Integer page,
                                                          @PathVariable("size") Integer size) {
        User user = securityUser.getUser();
        return userRelatedService.getMyFollowList(user.getId(), PageRequest.of(page - 1, size));
    }

    @Operation(summary = "关注", description = "关注某个用户")
    @PostMapping("/watch")
    public ResponseEntity<Void> watch(@AuthenticationPrincipal SecurityUser securityUser, @RequestBody Long userId) {
        User user = securityUser.getUser();
        userRelatedService.addUserFollow(user.getId(), userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "取消关注", description = "取消关注某个用户")
    @DeleteMapping("/watch/{userId}")
    public ResponseEntity<Void> cancelWatch(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable("userId") Long userId) {
        User user = securityUser.getUser();
        userRelatedService.delUserFollow(user.getId(), userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "文件上传", description = "文件上传接口")
    @PostMapping("/upload/{dir}")
    @Parameter(name = "dir", description = "oss分类名", in = ParameterIn.PATH)
    public String uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("dir") String dir) {
        if (file.isEmpty()) {
            throw new GlobalException(RestCode.PARAM_ERROR.code, "文件为空");
        }
        return storageService.uploadFile(file, dir);
    }

}
