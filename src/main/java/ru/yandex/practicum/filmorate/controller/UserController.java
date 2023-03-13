package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    @Autowired
    private UserStorage userStorage;

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.getUsers().values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userStorage.addUser(user);
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        userStorage.updateUser(user);
        log.info("Обновлены данные пользователя: {}", user.getName());
        return user;
    }
}