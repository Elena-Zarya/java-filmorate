package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage;
    private User user;

    @BeforeEach
    void createUser() {
        user = new User();
        user.setLogin("dolore");
        user.setBirthday(LocalDate.parse("1946-08-20"));
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
    }


    @Test
    void addUserTest() {
        userDbStorage.addUser(user);
        User userTest = userDbStorage.getUser(user.getId());
        assertThat(userTest.getName()).isEqualTo("Nick Name");
        assertThat(userTest.getEmail()).isEqualTo("mail@mail.ru");
        assertThat(userTest.getLogin()).isEqualTo("dolore");
        assertThat(userTest.getBirthday()).isEqualTo("1946-08-20");
        deleteUser(user.getId());
    }

    @Test
    void updateUserTest() {
        userDbStorage.addUser(user);
        user.setLogin("alba");
        userDbStorage.updateUser(user);
        User userTest = userDbStorage.getUser(user.getId());
        assertThat(userTest.getLogin()).isEqualTo("alba");
        deleteUser(user.getId());
    }

    @Test
    void deleteUserTest() {
        userDbStorage.addUser(user);
        userDbStorage.deleteUser(user.getId());
        Collection<User> users = userDbStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    void getAllUsersTest() {
        userDbStorage.addUser(user);
        Collection<User> users = userDbStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(1);
        deleteUser(user.getId());
    }

    @Test
    void getUserTest() {
        userDbStorage.addUser(user);
        User userTest = userDbStorage.getUser(user.getId());
        assertThat(user).isEqualTo(userTest);
        deleteUser(user.getId());
    }

    @Test
    void getAllFriendsUserTest() {
        User user2 = addUser();
        userDbStorage.addUser(user);
        userDbStorage.addUser(user2);
        userDbStorage.addFriend(user.getId(), user2.getId());
        Collection<User> friends = userDbStorage.getAllFriendsUser(user.getId());
        assertThat(friends.size()).isEqualTo(1);
        deleteUser(user.getId());
        deleteUser(user2.getId());
    }

    @Test
    void addFriendTest() {
        User user2 = addUser();
        userDbStorage.addUser(user);
        userDbStorage.addUser(user2);
        userDbStorage.addFriend(user.getId(), user2.getId());
        Collection<User> userFriendList = userDbStorage.getAllFriendsUser(user.getId());
        User userTest = userFriendList.iterator().next();
        assertThat(userTest).isEqualTo(user2);
        deleteUser(user.getId());
        deleteUser(user2.getId());
    }

    @Test
    void deleteFriendTest() {
        User user2 = addUser();
        userDbStorage.addUser(user);
        userDbStorage.addUser(user2);
        userDbStorage.addFriend(user.getId(), user2.getId());
        userDbStorage.deleteFriend(user.getId(), user2.getId());
        Collection<User> userFriendList = userDbStorage.getAllFriendsUser(user.getId());
        assertThat(userFriendList.size()).isEqualTo(0);
        deleteUser(user.getId());
        deleteUser(user2.getId());
    }

    private void deleteUser(long id) {
        userDbStorage.deleteUser(id);
    }

    private User addUser() {
        User user = new User();
        user.setLogin("friend");
        user.setName("friend adipisicing");
        user.setEmail("friend@mail.ru");
        user.setBirthday(LocalDate.parse("1976-08-20"));
        return user;
    }
}
