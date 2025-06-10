package DAO;

import javax.swing.*;
import java.sql.*;

public class UserDAO {

    public boolean authenticateUser(String username, String password, String role) throws SQLException {
        String sql = "SELECT u.* FROM uzytkownicy u " +
                "LEFT JOIN klienci_hurtowi kh ON u.id = kh.uzytkownik_id " +
                "WHERE u.login=? AND u.haslo=? AND u.typ=? " +
                "AND (u.typ != 'hurtowy' OR kh.id IS NOT NULL)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public int addUser(String login, String password, String type) {
        String sql = "INSERT INTO uzytkownicy (login, haslo, typ) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, type);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean deleteUser(int userID) throws SQLException {
        String sql = "DELETE FROM uzytkownicy WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, userID);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }


    public int getUserIdByWholesaleLogin(String login) throws SQLException {
        String sql = "SELECT kh.id FROM uzytkownicy u " +
                "JOIN klienci_hurtowi kh ON u.id = kh.uzytkownik_id " +
                "WHERE u.login = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("id"); // Zwracamy id z klienci_hurtowi, a nie z uzytkownicy
                } else {
                    throw new SQLException("Nie znaleziono klienta hurtowego dla podanego loginu");
                }
            }
        }
    }

    public int getUserIdByWholesaleId(int customerId) throws SQLException {
        String sql = "SELECT uzytkownik_id FROM klienci_hurtowi WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("uzytkownik_id");
                } else {
                    throw new SQLException("Klient hurtowy o podanym ID nie istnieje");
                }
            }
        }
    }

    public String getUserEmail(String username) throws SQLException {
        String sql = "SELECT kh.email FROM uzytkownicy u " +
                "JOIN klienci_hurtowi kh ON u.id = kh.uzytkownik_id " +
                "WHERE u.login = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("email");
            }
            return null;
        }
    }

    public boolean resetUserPassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE uzytkownicy SET haslo = ? WHERE login = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
