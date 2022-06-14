package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CrudDao<T, PK> {
    T fromRow(ResultSet rs) throws SQLException;

    ArrayList<T> fromRows(ResultSet rs) throws SQLException;

    Optional<T> get(PK id);

    List<T> getAll();

    PK save(T t);

    void update(T t);

    void delete(PK id);
}
