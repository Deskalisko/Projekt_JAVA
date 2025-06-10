package DAO;

import Model.Products.Product;
import Model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class OrderDAO {
    public static void saveOrder(Cart koszyk, String imie, String nazwisko, String adres, String telefon, String email, String paymentMethod) throws SQLException {
        Connection conn = null; // zmienna do przechowywania połączenia
        try {
            conn = DatabaseConnection.getConnection(); // laczenie z baza danych
            conn.setAutoCommit(false); // Rozpoczęcie transakcji

            int clientId = insertRetailClient(conn, imie, nazwisko, adres, telefon, email); //Dodanie klienta detalicznego

            double totalAmount = koszyk.obliczCalkowitaSume();// Dodanie transakcji
            int transactionId = insertTransaction(conn, clientId, totalAmount, paymentMethod); // Przekazanie metody płatności

            insertTransactionItems(conn, transactionId, koszyk);// Dodanie pozycji transakcji i aktualizacja stanów magazynowych
            updateClientPurchaseSum(conn, clientId, totalAmount);//Aktualizacja sumy zakupów klienta
            conn.commit(); // Zatwierdzenie transakcji
        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // Wycofanie zmian w przypadku błędu
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true); // przywrócenie auto-commit
        }
    }

    private static int insertRetailClient(Connection conn, String imie, String nazwisko,
                                          String adres, String telefon, String email) throws SQLException {
        String sql = "INSERT INTO klienci_detaliczni (imie, nazwisko, adres, telefon, email, suma_zakupow) VALUES (?, ?, ?, ?, ?, 0)"; // Początkowa suma zakupów = 0
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, imie);   // Użyj przekazanych wartości dla imienia, nazwiska itp.
            stmt.setString(2, nazwisko);
            stmt.setString(3, adres);
            stmt.setString(4, telefon);
            stmt.setString(5, email);
            stmt.executeUpdate(); //wykonanie zapytania

            try (var generatedKeys = stmt.getGeneratedKeys()) { //Pobranie generowanych kluczy
                if (generatedKeys.next()) { //sprawdzamy czy istnieją generowane klucze
                    return generatedKeys.getInt(1);  //zwracamy id nowego klienta
                } else {
                    throw new SQLException("Creating client failed, no ID obtained.");
                }
            }
        }
    }

    private static int insertTransaction(Connection conn, int clientId, double totalAmount, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO transakcje (klient_detaliczny_id, data, calkowita_kwota, typ, metoda_platnosci) " +
                "VALUES (?, NOW(), ?, 'detaliczny', ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) { // RETURN_GENERATED_KEYS - pobiera generowany klucz
            stmt.setInt(1, clientId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, paymentMethod); // Uzywa przekazanej metody płatności
            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }
    }

    private static void insertTransactionItems(Connection conn, int transactionId, Cart koszyk) throws SQLException {
        String insertSql = "INSERT INTO pozycje_transakcji (transakcja_id, produkt_id, ilosc, cena_jednostkowa) VALUES (?, ?, ?, ?)"; // dodanie pozycji transakcji
        String updateSql = "UPDATE produkty SET ilosc = ilosc - ? WHERE id = ?"; // aktualizacja stanu magazynowego

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            for (Map.Entry<Product, Integer> entry : koszyk.getProdukty()) { // iteracja po wszystkich produktach w koszyku
                Product product = entry.getKey(); // produkt
                int quantity = entry.getValue(); // ilość

                insertStmt.setInt(1, transactionId);
                insertStmt.setInt(2, product.getId());
                insertStmt.setInt(3, quantity);
                insertStmt.setDouble(4, product.getCena());
                insertStmt.addBatch(); // dodanie batcha - pozwala na zmniejszenie liczby połączeń z bazą danych - kilka na raz zapytan

                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, product.getId());
                updateStmt.addBatch();
            }

            insertStmt.executeBatch();
            updateStmt.executeBatch();
        }
    }

    private static void updateClientPurchaseSum(Connection conn, int clientId, double amount) throws SQLException {
        String sql = "UPDATE klienci_detaliczni SET suma_zakupow = suma_zakupow + ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, clientId);
            stmt.executeUpdate();
        }
    }
}