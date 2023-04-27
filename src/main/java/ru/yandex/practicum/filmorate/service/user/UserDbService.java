package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserDbService implements UserService {
    private final UserStorage userStorage;

    public UserDbService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(long userId, long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        return userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public Collection<User> getFriendsList(long userId) {
        return userStorage.getAllFriendsUser(userId);
    }

    @Override
    public Collection<User> getFriendsCommon(long userId, long otherId) {
        Collection<User> friendsCommonList = new ArrayList<>();
        Collection<User> userFriends = userStorage.getAllFriendsUser(userId);
        Collection<User> otherFriends = userStorage.getAllFriendsUser(otherId);
        for (User friend : userFriends) {
            if (otherFriends.contains(friend)) {
                friendsCommonList.add(friend);
            }
        }
        return friendsCommonList;
    }

    public User addUser(User user) {
        nameSet(user);
        userStorage.addUser(user);
        return userStorage.getUser(user.getId());
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        return userStorage.getUser(user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    private void nameSet(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }
}
