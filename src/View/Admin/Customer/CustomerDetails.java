package View.Admin.Customer;

import DAO.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDetails extends JFrame {
    private JPanel JPanel1;
    private JTabbedPane tabbedPane;
    private JPanel customerInfoPanel;
    private JPanel transactionsPanel;
    private JPanel productsPanel;
    private JTable transactionsTable;
    private JTable productsTable;
    private JLabel nameLabel;
    private JLabel typeLabel;
    private JLabel purchasesLabel;
    private JLabel addressLabel;
    private JLabel contactLabel;
    private JLabel statusLabel;

    public CustomerDetails(JFrame parent, int customerId, boolean isRetail) {
        super("Szczegóły klienta");

        // Inicjalizacja komponentów
        initializeComponents();

        // Ustawienie layoutu
        setupLayout();

        // Załaduj dane
        loadCustomerData(customerId, isRetail);
        loadTransactionsData(customerId, isRetail);
        loadProductsStatistics(customerId, isRetail);

        // Konfiguracja okna
        configureWindow(parent);
    }

    private void configureWindow(JFrame parent) {
        this.setContentPane(JPanel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(parent);

        // Ustawienie ikony
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Nie można załadować ikony: " + e.getMessage());
        }

        this.setVisible(true);
    }

    private void initializeComponents() {
        // Główny panel
        JPanel1 = new JPanel(new BorderLayout());
        JPanel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel1.setBackground(new Color(245, 245, 245));

        // Nagłówek
        JPanel headerPanel = createHeaderPanel();
        JPanel1.add(headerPanel, BorderLayout.NORTH);

        // Panel zakładek
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Panel informacji o kliencie
        customerInfoPanel = createCustomerInfoPanel();

        // Panel transakcji
        transactionsPanel = createTransactionsPanel();

        // Panel statystyk produktów
        productsPanel = createProductsPanel();

        // Dodanie zakładek
        tabbedPane.addTab("Informacje", customerInfoPanel);
        tabbedPane.addTab("Transakcje", transactionsPanel);
        tabbedPane.addTab("Statystyki produktów", productsPanel);

        // Panel statusu
        statusLabel = new JLabel("Użytkownik: ADMINISTRATOR | Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);

        JPanel1.add(tabbedPane, BorderLayout.CENTER);
        JPanel1.add(statusPanel, BorderLayout.SOUTH);

        // Timer aktualizujący status
        new Timer(1000, e -> {
            statusLabel.setText("Użytkownik: ADMINISTRATOR | Data: " + new java.util.Date().toString());
        }).start();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
            Image scaledLogo = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
            headerPanel.add(logoLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Błąd ładowania logo: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("SZCZEGÓŁY KLIENTA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createCustomerInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // Inicjalizacja etykiet
        nameLabel = createStyledLabel("");
        typeLabel = createStyledLabel("");
        purchasesLabel = createStyledLabel("");
        addressLabel = createStyledLabel("");
        contactLabel = createStyledLabel("");

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        transactionsTable = new JTable();
        styleTable(transactionsTable);
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        productsTable = new JTable();
        styleTable(productsTable);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 20);

        customerInfoPanel.add(createTitleLabel("Dane klienta:"), gbc);
        gbc.gridy++;
        customerInfoPanel.add(nameLabel, gbc);
        gbc.gridy++;
        customerInfoPanel.add(typeLabel, gbc);
        gbc.gridy++;
        customerInfoPanel.add(purchasesLabel, gbc);
        gbc.gridy++;
        customerInfoPanel.add(addressLabel, gbc);
        gbc.gridy++;
        customerInfoPanel.add(contactLabel, gbc);
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return label;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(200, 200, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void loadCustomerData(int customerId, boolean isRetail) {
        String tableName = isRetail ? "klienci_detaliczni" : "klienci_hurtowi";
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("imie") + " " + rs.getString("nazwisko");
                nameLabel.setText("Klient: " + name);
                typeLabel.setText("Typ: " + (isRetail ? "Detaliczny" : "Hurtowy"));
                purchasesLabel.setText("Suma zakupów: " + String.format("%.2f zł", rs.getDouble("suma_zakupow")));
                addressLabel.setText("Adres: " + rs.getString("adres"));

                String contact = "Telefon: " + rs.getString("telefon") + ", Email: " + rs.getString("email");
                if (!isRetail) {
                    contact += ", NIP: " + rs.getString("NIP") + ", Firma: " + rs.getString("nazwa_firmy");
                }
                contactLabel.setText(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych klienta", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTransactionsData(int customerId, boolean isRetail) {
        String columnName = isRetail ? "klient_detaliczny_id" : "klient_hurtowy_id";
        String sql = "SELECT t.id, t.data, t.calkowita_kwota, t.typ, t.metoda_platnosci, " +
                "GROUP_CONCAT(p.nazwa SEPARATOR ', ') AS produkty FROM transakcje t JOIN pozycje_transakcji pt ON t.id = pt.transakcja_id " +
                "JOIN produkty p ON pt.produkt_id = p.id WHERE t." + columnName + " = ? GROUP BY t.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.addColumn("ID");
            model.addColumn("Data");
            model.addColumn("Kwota");
            model.addColumn("Typ");
            model.addColumn("Płatność");
            model.addColumn("Produkty");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getTimestamp("data"),
                        String.format("%.2f zł", rs.getDouble("calkowita_kwota")),
                        rs.getString("typ"),
                        rs.getString("metoda_platnosci"),
                        rs.getString("produkty")
                });
            }
            transactionsTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania transakcji", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProductsStatistics(int customerId, boolean isRetail) {
        String columnName = isRetail ? "klient_detaliczny_id" : "klient_hurtowy_id";
        String sql = "SELECT p.id, p.nazwa, SUM(pt.ilosc) AS ilosc, SUM(pt.ilosc * pt.cena_jednostkowa) AS wartosc FROM produkty p " +
                "JOIN pozycje_transakcji pt ON p.id = pt.produkt_id JOIN transakcje t ON pt.transakcja_id = t.id WHERE t." + columnName + " = ? " +
                "GROUP BY p.id ORDER BY ilosc DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.addColumn("ID Produktu");
            model.addColumn("Nazwa");
            model.addColumn("Ilość");
            model.addColumn("Wartość");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nazwa"),
                        rs.getInt("ilosc"),
                        String.format("%.2f zł", rs.getDouble("wartosc"))
                });
            }
            productsTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania statystyk produktów", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}