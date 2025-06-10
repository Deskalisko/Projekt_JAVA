package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetailCustomerDAO {
    public static ResultSet getCustomerData(int customerId) throws SQLException { // pobiera dane klienta z tabeli klienci_detaliczni na podstawie jego ID
        String sql = "SELECT * FROM klienci_detaliczni WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerId);
        return stmt.executeQuery();
    }

    public static void updatePurchaseSum(int customerId, double amount) throws SQLException { //aktualizacja suma zakupów dla danego klienta
        String sql = "UPDATE klienci_detaliczni SET suma_zakupow = suma_zakupow + ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        }
    }
}