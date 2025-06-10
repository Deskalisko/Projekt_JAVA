package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDetailsDAO {

    public static class TransactionDetail { // Klasa wewnętrzna do przechowywania produktów w transakcji
        public int id;
        public Timestamp data;
        public String typ;
        public String metodaPlatnosci;
        public double calkowitaKwota;

        public TransactionDetail(int id, Timestamp data, String typ, String metodaPlatnosci, double calkowitaKwota) {
            this.id = id;
            this.data = data;
            this.typ = typ;
            this.metodaPlatnosci = metodaPlatnosci;
            this.calkowitaKwota = calkowitaKwota;
        }
    }

    public static class ProductInTransaction {
        public String nazwa;
        public int ilosc;
        public double cenaJednostkowa;

        public ProductInTransaction(String nazwa, int ilosc, double cenaJednostkowa) {
            this.nazwa = nazwa;
            this.ilosc = ilosc;
            this.cenaJednostkowa = cenaJednostkowa;
        }
    }

    public TransactionDetail getTransactionDetails(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transakcje WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TransactionDetail(
                            rs.getInt("id"),
                            rs.getTimestamp("data"),
                            rs.getString("typ"),
                            rs.getString("metoda_platnosci"),
                            rs.getDouble("calkowita_kwota")
                    );
                }
            }
        }
        return null;
    }

    public List<ProductInTransaction> getProductsInTransaction(int transactionId) throws SQLException {
        List<ProductInTransaction> products = new ArrayList<>();
        String sql = "SELECT p.nazwa, pt.ilosc, pt.cena_jednostkowa " +
                "FROM pozycje_transakcji pt JOIN produkty p ON pt.produkt_id = p.id " +
                "WHERE pt.transakcja_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new ProductInTransaction(
                            rs.getString("nazwa"),
                            rs.getInt("ilosc"),
                            rs.getDouble("cena_jednostkowa")
                    ));
                }
            }
        }
        return products;
    }
}