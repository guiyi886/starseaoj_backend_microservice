package com.starseaoj.userservice.controller.inner;

import com.starseaoj.model.entity.User;
import com.starseaoj.serviceclient.UserFeignClient;
import com.starseaoj.userservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 内部调用服务
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据id获取用户列表
     *
     * @param userIdList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("userIdList") Collection<Long> userIdList) {
        return userService.listByIds(userIdList);
    }
}
