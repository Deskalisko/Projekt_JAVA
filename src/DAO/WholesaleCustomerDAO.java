package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WholesaleCustomerDAO {
    public static ResultSet getCustomerData(int customerId) throws SQLException {
        String sql = "SELECT * FROM klienci_hurtowi WHERE id = ?";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerId);
        return stmt.executeQuery();
    }

    public static void updatePurchaseSum(int customerId, double amount) throws SQLException {
        String sql = "UPDATE klienci_hurtowi SET suma_zakupow = suma_zakupow + ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        }
    }

    public static boolean updatePassword(int customerId, String oldPassword, String newPassword) throws SQLException {
        String checkSql = "SELECT u.id FROM uzytkownicy u " +
                "JOIN klienci_hurtowi kh ON u.id = kh.uzytkownik_id " +
                "WHERE kh.id = ? AND u.haslo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, customerId);
            checkStmt.setString(2, oldPassword);

            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return false; // false jesli haslo nie jest poprawne (stare - nie zgadza sie z tym w bazie)
            }

            int userId = rs.getInt("u.id");
            String updateSql = "UPDATE uzytkownicy SET haslo = ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, newPassword);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
                return true; // true jesli haslo zostalo zmienione pomyslnie
            }
        }
    }
}