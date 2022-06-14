package connection;

public record ConnectionProps(String url, String user, String password, int poolSize) {
}
