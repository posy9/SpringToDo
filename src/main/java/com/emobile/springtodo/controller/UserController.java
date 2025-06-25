package com.emobile.springtodo.controller;

import com.emobile.springtodo.api.swagger.IUserController;
import com.emobile.springtodo.dto.input.UserRequest;
import com.emobile.springtodo.dto.mapper.UserRequestMapper;
import com.emobile.springtodo.dto.mapper.UserResponseMapper;
import com.emobile.springtodo.dto.output.UserResponse;
import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserResponse, UserRequest> implements IUserController {

    public UserController(UserRequestMapper requestMapper, UserResponseMapper responseMapper,
                          UserService userService, MeterRegistry meterRegistry) {
        super(requestMapper, responseMapper, meterRegistry, userService);
    }
}
