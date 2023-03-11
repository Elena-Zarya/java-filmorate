package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id;

    private long generateId() {
        return ++id;
    }

    private void nameSet(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void addUser(User user) {
        user.setId(generateId());
        nameSet(user);
        users.put(id, user);
    }

    public void updateUser(User user) {
        long id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
        } else {
            throw new RuntimeException("Пользователь с id: " + id + "не найден");
        }
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
