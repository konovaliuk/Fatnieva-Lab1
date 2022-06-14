import connection.ConnectionPool;
import dao.DaoFactory;
import dao.inte.CourseDao;
import dao.inte.CourseModuleDao;
import dao.inte.UserDao;
import entities.Course;
import entities.CourseLevel;
import entities.CourseModule;
import entities.User;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) {
//      // test course dao
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        Connection conn = ConnectionPool.getInstance().getConnection();
        CourseDao courseDao = DaoFactory.getCourseDao(conn);

        Course course = new Course();
        course.setLevel(CourseLevel.B2);
        course.setTitle("Course b2 english");
        course.setDescription("description");

        Long courseId = courseDao.save(course);
        System.out.printf("Course with id=%s created%n", courseId);
        courseDao.delete(courseId);
        System.out.printf("Course with id=%s deleted%n", courseId);

        // test user dao
        Connection userConn = ConnectionPool.getInstance().getConnection();
        UserDao userDao = DaoFactory.getUserDao(userConn);

        User user = new User();
        user.setUsername("login");
        user.setPwdHash("password");

        String userId = userDao.save(user);
        System.out.printf("User with id=%s created%n", userId);
        System.out.printf("Users: %s%n", userDao.getAll());

        user.setPwdHash("pwd");
        userDao.update(user);
        System.out.printf("Updated course: %s%n", userDao.get(user.getUsername()));

        userDao.delete(userId);
        System.out.printf("Course with id=%s deleted%n", userId);

        // test course modules
        Connection courseModulesConn = ConnectionPool.getInstance().getConnection();
        CourseModuleDao courseModuleDao = DaoFactory.getCourseModuleDao(courseModulesConn);

        CourseModule courseModule = new CourseModule();
        courseModule.setTitle("Module 1");
        courseModule.setEta(5);
        courseModule.setMaterials("description");

        Long courseModuleId = courseModuleDao.save(courseModule);
        System.out.printf("CourseModule with id=%s created%n", courseModuleId);

        System.out.printf("CourseModules: %s%n", courseModuleDao.getAll());

        courseModuleDao.delete(courseModuleId);
        System.out.printf("CourseModule with id=%s deleted%n", courseModuleId);
    }
}