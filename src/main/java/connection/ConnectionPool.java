package connection;

import utils.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {
    private static ConnectionProps connectionConfig;
    private static ConnectionPool instance;
    private static BlockingQueue<Connection> connectionQueue;

    private ConnectionPool() {
        connectionConfig = loadConnectionConfig();
        connectionQueue = new ArrayBlockingQueue<>(connectionConfig.poolSize());
        for (int i = 0; i < connectionConfig.poolSize(); i++) {
            Connection connection;
            try {
                connection = createConnection();
                connectionQueue.offer(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private ConnectionProps loadConnectionConfig() {
        ResourceBundle resource = ResourceBundle.getBundle(Constants.DATABASE_PROPERTIES);
        String user = resource.getString(Constants.DB_USER);
        String password = resource.getString(Constants.DB_PASSWORD);
        String url = resource.getString(Constants.DB_URL);
        int polSize = Integer.parseInt(resource.getString(Constants.DB_POOL));

        return new ConnectionProps(url, user, password, polSize);
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(connectionConfig.url(), connectionConfig.user(), connectionConfig.password());
    }

    public Connection getConnection() {
        try {
            Connection conn = connectionQueue.poll(5, TimeUnit.SECONDS);
            if (conn == null) throw new Exception("Connection Pool is exhausted");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(Connection connection) {
        connectionQueue.offer(connection);
    }

    public void closeAllRealConnections() {

        for (Connection conn : connectionQueue) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
