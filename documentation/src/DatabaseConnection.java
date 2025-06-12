public class DatabaseConnection {
    private static final String URL= "jdbc:mysql://localhost:3306/sklep_db"; [cite: 1676]
    private static final String USER = "root"; [cite: 1677]
    private static final String PASSWORD = ""; [cite: 1677]
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); [cite: 1678]
    
    }
}