package DAO;

import Model.Cart;
import Model.Products.Product;
import Model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionDAO {

    public List<Transaction> getRecentTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.id AS transakcja_id, t.data, t.typ, t.calkowita_kwota, t.metoda_platnosci, " +
                "COALESCE(kd.imie, kh.imie) AS imie_klienta, COALESCE(kd.nazwisko, kh.nazwisko) AS nazwisko_klienta, " +
                "COALESCE(kd.email, kh.email) AS email_klienta " +
                "FROM transakcje t " +
                "LEFT JOIN klienci_detaliczni kd ON t.klient_detaliczny_id = kd.id " +
                "LEFT JOIN klienci_hurtowi kh ON t.klient_hurtowy_id = kh.id " +
                "ORDER BY t.data DESC LIMIT 50";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transakcja_id"),
                        0,
                        0,
                        rs.getTimestamp("data"),
                        rs.getString("typ"),
                        rs.getDouble("calkowita_kwota"),
                        rs.getString("metoda_platnosci"),
                        null
                );
                transaction.setImieKlienta(rs.getString("imie_klienta"));
                transaction.setNazwiskoKlienta(rs.getString("nazwisko_klienta"));
                transaction.setEmailKlienta(rs.getString("email_klienta"));
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public static int insertRetailTransaction(int customerId, double totalAmount, String paymentMethod) throws SQLException {//
        String sql = "INSERT INTO transakcje (klient_detaliczny_id, data, calkowita_kwota, typ, metoda_platnosci) " +
                "VALUES (?, NOW(), ?, 'detaliczny', ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, paymentMethod);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) { // pobranie generowanych kluczy
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Błąd podczas tworzenia transakcji. Nie udało się pobrać generowanego klucza."); // blad, jesli nie uzyskano ID
                }
            }
        }
    }

    public static int insertWholesaleTransaction(int customerId, double totalAmount, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO transakcje (klient_hurtowy_id, data, calkowita_kwota, typ, metoda_platnosci) " +
                "VALUES (?, NOW(), ?, 'hurtowy', ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, paymentMethod);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Błąd, nie uzyskano ID");
                }
            }
        }
    }

    public static void insertTransactionItems(int transactionId, Cart koszyk) throws SQLException {
        String insertSql = "INSERT INTO pozycje_transakcji (transakcja_id, produkt_id, ilosc, cena_jednostkowa) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE produkty SET ilosc = ilosc - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            for (Map.Entry<Product, Integer> entry : koszyk.getProdukty()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                insertStmt.setInt(1, transactionId);
                insertStmt.setInt(2, product.getId());
                insertStmt.setInt(3, quantity);
                insertStmt.setDouble(4, product.getCena());
                insertStmt.addBatch();

                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, product.getId());
                updateStmt.addBatch();
            }

            insertStmt.executeBatch(); // Wykonanie wszystkich zapytań w batchu
            updateStmt.executeBatch();
        }
    }

    public static void updateStoreBalance(double amount) throws SQLException {
        String query = "UPDATE finanse_sklepu SET saldo = saldo + ? WHERE id = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.executeUpdate();
        }
    }
}