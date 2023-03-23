package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

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
}