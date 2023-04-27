package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    void deleteUser(long id);

    Collection<User> getAllUsers();

    User getUser(long userId);

    Collection<User> getAllFriendsUser(long userId);

    User addFriend(long userId, long friendId);

    User deleteFriend(long userId, long friendId);
}
