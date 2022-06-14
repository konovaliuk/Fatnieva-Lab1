package dao.impl;

import dao.inte.UserDao;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    protected static final String TABLE_NAME = "users";

    private final Connection conn;

    public UserDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User fromRow(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUsername(rs.getString("username"));
        user.setPwdHash(rs.getString("pwd_hash"));

        return user;
    }

    @Override
    public ArrayList<User> fromRows(ResultSet rs) throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        if (!rs.next()) {
            return users;
        }

        do {
            users.add(fromRow(rs));
        } while (rs.next());

        return users;
    }

    @Override
    public Optional<User> get(String id) {
        String query = String.format("SELECT * FROM %s WHERE username='%s'", TABLE_NAME, id);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Optional<User> maybeUser = Optional.empty();

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return maybeUser;
            }
            maybeUser = Optional.ofNullable(fromRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maybeUser;
    }

    @Override
    public List<User> getAll() {
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            users = fromRows(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public String save(User user) {
        String query = String.format(
                "INSERT INTO %s (username, pwd_hash) VALUES ('%s', '%s') RETURNING username",
                TABLE_NAME,
                user.getUsername(),
                user.getPwdHash()
        );
        PreparedStatement stmt;
        ResultSet rs;

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            rs.next();
            return rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(User user) {
        String query = String.format(
                "UPDATE %s SET pwd_hash = '%s' WHERE username = '%s'",
                TABLE_NAME,
                user.getPwdHash(),
                user.getUsername()
        );
        PreparedStatement stmt;

        try {
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String query = String.format("DELETE FROM %s WHERE username='%s'", TABLE_NAME, id);
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
