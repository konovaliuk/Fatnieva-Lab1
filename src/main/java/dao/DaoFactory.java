package dao;

import dao.impl.CourseDaoImpl;
import dao.impl.CourseModuleDaoImpl;
import dao.impl.UserDaoImpl;
import dao.inte.CourseDao;
import dao.inte.CourseModuleDao;
import dao.inte.UserDao;

import java.sql.Connection;

public class DaoFactory {
    public static CourseDao getCourseDao(Connection conn) {
        return new CourseDaoImpl(conn);
    }

    public static CourseModuleDao getCourseModuleDao(Connection conn) {
        return new CourseModuleDaoImpl(conn);
    }

    public static UserDao getUserDao(Connection conn) {
        return new UserDaoImpl(conn);
    }
}
