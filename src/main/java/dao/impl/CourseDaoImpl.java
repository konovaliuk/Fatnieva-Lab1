package dao.impl;

import dao.inte.CourseDao;
import entities.Course;
import entities.CourseLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDaoImpl implements CourseDao {
    protected static final String TABLE_NAME = "courses";

    private final Connection conn;

    public CourseDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Course fromRow(ResultSet rs) throws SQLException {
        Course course = new Course();

        course.setId(rs.getLong("id"));
        course.setTitle(rs.getString("title"));
        course.setDescription(rs.getString("description"));
        course.setLevel(CourseLevel.valueOf(rs.getString("level")));

        return course;
    }

    @Override
    public ArrayList<Course> fromRows(ResultSet rs) throws SQLException {
        ArrayList<Course> courses = new ArrayList<>();

        if (!rs.next()) {
            return courses;
        }

        do {
            courses.add(fromRow(rs));
        } while (rs.next());

        return courses;
    }

    @Override
    public Optional<Course> get(Long id) {
        String query = String.format("SELECT * FROM %s WHERE id='%s'", TABLE_NAME, id);
        PreparedStatement stmt;
        ResultSet rs;
        Optional<Course> maybeCourse = Optional.empty();

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return maybeCourse;
            }
            maybeCourse = Optional.ofNullable(fromRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maybeCourse;
    }

    @Override
    public List<Course> getAll() {
        String query = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement stmt;
        ResultSet rs;
        ArrayList<Course> courses = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            courses = fromRows(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Long save(Course course) {
        String query = String.format(
                "INSERT INTO %s(title, level, description) VALUES ('%s', '%s', '%s') RETURNING id",
                TABLE_NAME,
                course.getTitle(),
                course.getLevel(),
                course.getDescription()
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
    public void update(Course course) {
        String query = String.format(
                "UPDATE %s SET title = '%s', level = '%s', description = '%s' WHERE id = '%s'",
                TABLE_NAME,
                course.getTitle(),
                course.getLevel(),
                course.getDescription(),
                course.getId()
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
        } catch (SQLException ignored) {
        }
    }
}
