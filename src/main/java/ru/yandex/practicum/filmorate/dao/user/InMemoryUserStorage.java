package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
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
            throw new NotFoundException("Пользователь с id: " + id + "не найден");
        }
    }

    @Override
    public void deleteUser(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id: " + id + "не найден");
        }
        users.remove(id);
    }


    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
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
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getAllFriendsUser(long userId) {
        return null;
    }

    @Override
    public User addFriend(long userId, long friendId) {
        return null;
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        return null;
    }
}
