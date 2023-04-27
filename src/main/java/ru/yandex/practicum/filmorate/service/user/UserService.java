package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {

    User addFriend(long userId, long friendId);

    User deleteFriend(long userId, long friendId);

    Collection<User> getFriendsList(long userId);

    Collection<User> getFriendsCommon(long userId, long otherId);

    User addUser(User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    User getUser(long userId);

}