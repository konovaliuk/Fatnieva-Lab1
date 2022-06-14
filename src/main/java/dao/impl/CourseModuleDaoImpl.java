package dao.impl;

import dao.inte.CourseModuleDao;
import entities.CourseModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseModuleDaoImpl implements CourseModuleDao {
    protected static final String TABLE_NAME = "course_modules";

    private final Connection conn;

    public CourseModuleDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public CourseModule fromRow(ResultSet rs) throws SQLException {
        CourseModule courseModule = new CourseModule();

        courseModule.setId(rs.getLong("id"));
        courseModule.setTitle(rs.getString("title"));
        courseModule.setEta(rs.getInt("eta"));
        courseModule.setMaterials(rs.getString("materials"));

        return courseModule;
    }

    @Override
    public ArrayList<CourseModule> fromRows(ResultSet rs) throws SQLException {
        ArrayList<CourseModule> courseModules = new ArrayList<>();

        if (!rs.next()) {
            return courseModules;
        }

        do {
            courseModules.add(fromRow(rs));
        } while (rs.next());

        return courseModules;
    }

    @Override
    public Optional<CourseModule> get(Long id) {
        String query = String.format("SELECT * FROM %s WHERE id='%s'", TABLE_NAME, id);
        PreparedStatement stmt;
        ResultSet rs;
        Optional<CourseModule> maybeCourseModule = Optional.empty();

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return maybeCourseModule;
            }
            maybeCourseModule = Optional.ofNullable(fromRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maybeCourseModule;
    }

    @Override
    public List<CourseModule> getAll() {
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement stmt;
        ResultSet rs;
        ArrayList<CourseModule> courseModules = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            courseModules = fromRows(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseModules;
    }

    @Override
    public Long save(CourseModule courseModule) {
        String query = String.format(
                "INSERT INTO %s (eta, title, materials) VALUES ('%s', '%s', '%s') RETURNING id",
                TABLE_NAME,
                courseModule.getEta(),
                courseModule.getTitle(),
                courseModule.getMaterials()
        );
        PreparedStatement stmt;
        ResultSet rs;

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            rs.next();
            return rs.getLong("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(CourseModule courseModule) {
        String query = String.format(
                "UPDATE %s SET eta = '%s', title = '%s', materials = '%s' WHERE id = '%s'",
                TABLE_NAME,
                courseModule.getEta(),
                courseModule.getTitle(),
                courseModule.getMaterials(),
                courseModule.getId()
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
    public void delete(Long id) {
        String query = String.format("DELETE FROM %s WHERE id='%s'", TABLE_NAME, id);
        PreparedStatement stmt;

        try {
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
