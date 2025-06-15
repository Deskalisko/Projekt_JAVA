package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.WarehouseOrder;
import Model.WarehouseOrderItem;

import javax.swing.*;

public class WarehouseOrderDAO {

    public void createWarehouseOrder(int productId, int quantity, String supplier, String notes) throws SQLException { // Utwórz zamówienie magazynowe
        String sqlOrder = "INSERT INTO zamowienia_magazynowe (data_zamowienia, dostawca, uwagi, calkowita_kwota) VALUES (NOW(), ?, ?, ?)";

        // Pobierz cenę magazynową produktu
        double unitPrice = getProductWarehousePrice(productId); // Pobranie ceny magazynowej dla danego produktu
        double totalCost = unitPrice * quantity; // Obliczenie całkowitej kwoty na podstawie ceny magazynowej

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, supplier);//dostawca
            stmt.setString(2, notes);//uwagi
            stmt.setDouble(3, totalCost);//calkowita_kwota
            stmt.executeUpdate();

            int orderId;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Nie udało się utworzyć zamówienia, brak ID.");
                }
            }

            String sqlItems = "INSERT INTO pozycje_zamowienia_magazynowego (zamowienie_id, produkt_id, ilosc, cena_jednostkowa) VALUES (?, ?, ?, ?)";

            try (PreparedStatement itemsStmt = conn.prepareStatement(sqlItems)) {
                itemsStmt.setInt(1, orderId);
                itemsStmt.setInt(2, productId);
                itemsStmt.setInt(3, quantity);
                itemsStmt.setDouble(4, unitPrice);
                itemsStmt.executeUpdate();
            }

            // Dodane: Aktualizacja stanu magazynowego produktu
            String sqlUpdateStock = "UPDATE produkty SET ilosc = ilosc + ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateStock)) {
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, productId);
                updateStmt.executeUpdate();
            }

            // Odejmij koszt od salda
            updateBalance(-totalCost);
        }
    }

    public double getProductWarehousePrice(int productId) throws SQLException {
        String sql = "SELECT cena_magazynowa FROM ceny_produkty WHERE produkt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cena_magazynowa");
                }
                throw new SQLException("Produkt o ID " + productId + " nie ma ceny magazynowej.");
            }
        }
    }

    public List<WarehouseOrder> getAllWarehouseOrders() throws SQLException {
        List<WarehouseOrder> orders = new ArrayList<>();
        String sql = "SELECT zm.id, zm.data_zamowienia, zm.dostawca, zm.calkowita_kwota " +
                "FROM zamowienia_magazynowe zm " +
                "ORDER BY zm.data_zamowienia DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                WarehouseOrder order = new WarehouseOrder(
                        rs.getInt("id"),
                        0, // productId  - będzie ustawione z pozycji zamówienia
                        0, // quantity  - ustawione po pobraniu pozycji zamówienia
                        rs.getTimestamp("data_zamowienia"),
                        rs.getString("dostawca"),
                        rs.getDouble("calkowita_kwota")
                );
                orders.add(order);
            }
        }
        return orders;
    }

    public double getCurrentBalance() {
        String sql = "SELECT saldo FROM finanse_sklepu LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
            return 0.0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Błąd podczas pobierania salda: " + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return 0.0;
        }
    }

    public void updateBalance(double amount) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE finanse_sklepu SET saldo = saldo + ?")) {

            stmt.setDouble(1, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Błąd podczas aktualizacji salda: " + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<WarehouseOrder> getWarehouseOrdersWithDetails() throws SQLException {
        List<WarehouseOrder> orders = getAllWarehouseOrders();
        for (WarehouseOrder order : orders) {
            order.setItems(getOrderItems(order.getId()));
        }
        return orders;
    }

    public List<WarehouseOrderItem> getOrderItems(int orderId) throws SQLException {
        List<WarehouseOrderItem> items = new ArrayList<>();
        String sql = "SELECT pzm.id, pzm.produkt_id, pzm.ilosc, pzm.cena_jednostkowa, p.nazwa " +
                "FROM pozycje_zamowienia_magazynowego pzm " +
                "JOIN produkty p ON pzm.produkt_id = p.id " +
                "WHERE pzm.zamowienie_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WarehouseOrderItem item = new WarehouseOrderItem();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(orderId);
                    item.setProductId(rs.getInt("produkt_id"));
                    item.setProductName(rs.getString("nazwa"));
                    item.setQuantity(rs.getInt("ilosc"));
                    item.setUnitPrice(rs.getDouble("cena_jednostkowa"));
                    items.add(item);
                }
            }
        }
        return items;
    }
}