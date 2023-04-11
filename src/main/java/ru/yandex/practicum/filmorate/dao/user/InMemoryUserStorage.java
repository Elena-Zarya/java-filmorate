package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public void addUser(User user) {
        user.setId(generateId());
        nameSet(user);
        users.put(id, user);
    }

    @Override
    public void updateUser(User user) {
        long id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
        } else {
            throw new UserNotFoundException("Пользователь с id: " + id + "не найден");
        }
    }

    @Override
    public void deleteUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id: " + id + "не найден");
        }
        users.remove(id);
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    private long generateId() {
        return ++id;
    }

    private void nameSet(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User getUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден");
        }
        return users.get(id);
    }
}
