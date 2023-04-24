package ru.yandex.practicum.filmorate.dao.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


@Component
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO users (user_name, email, login, birthday) VALUES (?, ?, ?, ?)",
                user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM users " +
                "WHERE user_name = ? and email = ?", user.getName(), user.getEmail());
        if (userRows.next()) {
            user.setId(userRows.getInt("user_id"));
        } else {
            log.info("ID пользователя " + user.getName() + " не найден.");
            throw new NotFoundException("ID пользователя " + user.getName() + " не найден");
        }
    }

    @Override
    public void updateUser(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", user.getId());
        if (userRows.next()) {
            jdbcTemplate.update("UPDATE users SET user_name = ?, email = ?, login = ?, birthday = ? " +
                            "WHERE user_id = ? ",
                    user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
            log.info("Обновлены данные пользователя: {} {}", user.getId(), user.getName());
        } else {
            log.info("Пользователь с ID {} не найден.", user.getId());
            throw new NotFoundException("Пользователь с id: " + user.getId() + " не найден");
        }
    }

    @Override
    public void deleteUser(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", id);
        if (userRows.next()) {
            jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
            log.info("Удален пользователь с ID: {} ", id);
        } else {
            log.info("Пользователь с ID {} не найден.", id);
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT user_id, user_name, email, login, birthday FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUsers);
    }

    @Override
    public User getUser(long userId) {
        try {
            String sqlQuery = "SELECT* FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUsers, userId);
        } catch (Exception e) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
    }

    @Override
    public Collection<User> getAllFriendsUser(long userId) {
        String sqlQuery = "SELECT u.user_id, u.user_name, u.login, u.email, u.birthday from user_friends uf " +
                "JOIN users u ON u.user_id = uf.friend_id where uf.user_id = ?";
        Collection<User> userFriends = jdbcTemplate.query(sqlQuery, this::mapRowToUsers, userId);
        if (userFriends.isEmpty()) {
            return Collections.emptyList();
        }
        return userFriends;
    }

    @Override
    public User addFriend(long userId, long friendId) {
        String sqlQuery = "SELECT* FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (userRows.next()) {
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", friendId);
            if (friendRows.next()) {
                boolean status = false;
                SqlRowSet userFriendRows = jdbcTemplate.queryForRowSet("SELECT* FROM user_friends " +
                        "WHERE user_id = ? AND friend_id = ?", friendId, userId);
                if (userFriendRows.next()) {
                    status = true;
                    jdbcTemplate.update("UPDATE user_friends SET status = ? WHERE user_id = ? AND friend_id = ?",
                            status, friendId, userId);
                }
                jdbcTemplate.update("INSERT INTO user_friends (user_id, friend_id, status) VALUES (?, ?, ?)",
                        userId, friendId, status);
                log.info("Пользователь {} добавил друга {}", userId, friendId);
            } else {
                throw new NotFoundException("Пользователь с id: " + friendId + " не найден");
            }
        } else {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUsers, userId);
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        String sqlQuery = "SELECT* FROM user_friends WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (userRows.next()) {
            SqlRowSet userFriendRows = jdbcTemplate.queryForRowSet("SELECT* FROM user_friends " +
                    "WHERE user_id = ? AND friend_id = ?", userId, friendId);
            if (userFriendRows.next()) {
                jdbcTemplate.update("DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?",
                        userId, friendId);
                SqlRowSet friendRows = jdbcTemplate.queryForRowSet("SELECT* FROM user_friends WHERE user_id = ? A" +
                        "ND friend_id = ?", friendId, userId);
                if (friendRows.next()) {
                    jdbcTemplate.update("UPDATE user_friends SET status = ? WHERE user_id = ? AND friend_id = ?",
                            false, friendId, userId);
                }
                log.info("Пользователь {} удалил друга {}", userId, friendId);
            } else {
                throw new NotFoundException("У пользователя " + userId + " друг с id: " + friendId + " не найден");
            }
        } else {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        return jdbcTemplate.queryForObject("SELECT* FROM users WHERE user_id = ?", this::mapRowToUsers, userId);
    }

    private User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setName(resultSet.getString("user_name"));
        user.setLogin(resultSet.getString("login"));
        user.setEmail(resultSet.getString("email"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }
}
