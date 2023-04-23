package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {


    private final UserStorage userStorage;

    public UserServiceImpl(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    public User deleteFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> getFriendsList(long userId) {
        List<User> friendsList = new ArrayList<>();
        Set<Long> friends = userStorage.getUser(userId).getFriends();
        for (Long friendId : friends) {
            friendsList.add(userStorage.getUser(friendId));
        }
        return friendsList;
    }

    public List<User> getFriendsCommon(long userId, long otherId) {
        List<User> friendsCommonList = new ArrayList<>();
        Set<Long> friendsUser = userStorage.getUser(userId).getFriends();
        Set<Long> friendsOther = userStorage.getUser(otherId).getFriends();
        for (Long friendId : friendsUser) {
            if (friendsOther.contains(friendId)) {
                friendsCommonList.add(userStorage.getUser(friendId));
            }
        }
        return friendsCommonList;
    }

    @Override
    public User addUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUser(long userId) {
        return null;
    }
}