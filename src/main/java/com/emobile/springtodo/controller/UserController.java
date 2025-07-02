package com.emobile.springtodo.controller;

import com.emobile.springtodo.api.swagger.IUserController;
import com.emobile.springtodo.dto.input.UserRequest;
import com.emobile.springtodo.dto.mapper.UserRequestMapper;
import com.emobile.springtodo.dto.mapper.UserResponseMapper;
import com.emobile.springtodo.dto.output.UserResponse;
import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserResponse, UserRequest> implements IUserController {

    public UserController(UserRequestMapper requestMapper, UserResponseMapper responseMapper,
                          UserService userService, MeterRegistry meterRegistry) {
        super(requestMapper, responseMapper, meterRegistry, userService);
    }

    @GetMapping("/{id}")
    @Override
    public UserResponse findById(@PathVariable long id) {
        return super.findById(id);
    }

    @GetMapping
    @Override
    public List<UserResponse> findAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return super.findAll(page, size);
    }

    @PostMapping
    @Override
    public void create(@RequestBody @Valid UserRequest userRequest) {
        super.create(userRequest);
    }


    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable long id) {
        super.delete(id);
    }

    @PutMapping("/{id}")
    @Override
    public void update(@PathVariable long id, @RequestBody @Valid UserRequest userRequest) {
        super.update(id, userRequest);
    }
}
