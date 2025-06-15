package View.Shopping;

import DAO.TransactionDAO;
import DAO.WholesaleCustomerDAO;
import Model.Cart;
import View.TableModels.CartTableModel;
import View.TableModels.ProductTableModel;
import View.Admin.Customer.AddEditWholeSaleCustomer;
import DAO.DatabaseConnection;
import DAO.ProductsDAO;
import Model.Products.Product;
import View.MainForm;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShopWholeSaleForm extends JFrame {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel customerDataPanel;
    private JButton editDataButton;
    private JButton placeOrderButton;
    private JButton addToCartButton;
    private JPanel cartPanel;
    private JSpinner quantitySpinner;
    private JPanel productsPanel;
    private JTable productsTable;
    private JTable cartTable;
    private JLabel statusLabel;
    private JPanel cartButtonsPanel;
    private Cart koszyk;
    private CartTableModel cartTableModel;
    private ProductTableModel productsTableModel;
    private JButton removeFromCartButton;
    private JButton logoutButton;
    private int customerId;
    private JLabel titleLabel;
    private JComboBox<String> paymentMethodComboBox;
    private JButton changePasswordButton;
    private JCheckBox showDataCheckBox;

    public ShopWholeSaleForm(int customerId) {
        this.customerId = customerId;
        this.koszyk = new Cart();

        initializeComponents();
        setupLayout();
        loadCustomerData();
        loadProducts();
        setupTimer();

        this.setTitle("Panel Klienta Hurtowego");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Otwiera okno w trybie pełnoekranowym
        this.setSize(1200,1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Ustawienie ikony aplikacji
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());

    }

    private void setupTimer() {
        new Timer(1000, e -> {
            statusLabel.setText("Użytkownik: Klient hurtowy | Data: " + new Date().toString());
        }).start();
    }

    private void loadProducts() {
        try {
            ProductsDAO dao = new ProductsDAO();
            List<Product> products = dao.getAllProductsForWholesale();
            productsTableModel.setProducts(products);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd ładowania produktów: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        JPanel1 = new JPanel(new BorderLayout());
        JPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel1.setBackground(new Color(245, 245, 245));

        setupHeaderPanel();
        setupContentPanel();
        setupStatusPanel();
    }

    private void setupHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/icons8-crane-100.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        titleLabel = new JLabel("PANEL KLIENTA HURTOWEGO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
    }

    private void setupContentPanel() {
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // wysokość panelu zawierającego dane klienta oraz przyciski (15%)
        gbc.weighty = 0.15;
        gbc.gridy = 0;
        setupCustomerDataPanel();
        contentPanel.add(customerDataPanel, gbc);

        // Produkty panel (60% )
        gbc.weighty = 0.6;
        gbc.gridy = 1;
        setupProductsPanel();
        contentPanel.add(productsPanel, gbc);

        // Cart panel (25% )
        gbc.weighty = 0.25;
        gbc.gridy = 2;
        setupCartPanel();
        contentPanel.add(cartPanel, gbc);
    }

    private void setupCustomerDataPanel() {
        customerDataPanel = new JPanel(new GridBagLayout());
        customerDataPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        customerDataPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();// gridbagconstraints by lepiej skonfigurować rozkład panelu
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        setupCustomerButtons();

        loadCustomerData();
    }

    private void setupCustomerButtons() {
        JPanel customerButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        customerButtonsPanel.setOpaque(false);

         // checkbox czy wyświetlać dane klienta
        showDataCheckBox = new JCheckBox("Pokaż dane", true);
        showDataCheckBox.setFont(new Font("Segoe UI", Font.BOLD, 14));
        showDataCheckBox.setBackground(Color.WHITE);
        showDataCheckBox.setForeground(new Color(44, 62, 80));
        showDataCheckBox.setFocusPainted(false);
        showDataCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showDataCheckBox.addActionListener(e -> toggleCustomerDataVisibility());

        editDataButton = createStyledButton("Edytuj dane", new Color(52, 152, 219));
        editDataButton.addActionListener(e -> editCustomerData());

        changePasswordButton = createStyledButton("Zmień hasło", new Color(155, 89, 182));
        changePasswordButton.addActionListener(e -> changePassword());

        logoutButton = createStyledButton("Wyloguj", new Color(231, 76, 60));
        logoutButton.addActionListener(e -> {
            dispose();
            new MainForm();
        });

        customerButtonsPanel.add(showDataCheckBox);
        customerButtonsPanel.add(editDataButton);
        customerButtonsPanel.add(changePasswordButton);
        customerButtonsPanel.add(logoutButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 15, 0);
        customerDataPanel.add(customerButtonsPanel, gbc);
    }
    private void toggleCustomerDataVisibility() {
        if (showDataCheckBox.isSelected()) {
            loadCustomerData();
        } else {
            clearCustomerData();
        }
        customerDataPanel.revalidate();
        customerDataPanel.repaint();
    }

    private void clearCustomerData() {
        Component[] components = customerDataPanel.getComponents();
        for (Component comp : components) {
            if (!(comp instanceof JPanel)) {
                customerDataPanel.remove(comp);
            }
        }
    }

    private void changePassword() {
        // Panel do wprowadzenia danych
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBackground(new Color(245, 245, 245));

        JLabel oldPassLabel = new JLabel("Aktualne hasło:");
        oldPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPasswordField oldPassField = new JPasswordField(20);

        JLabel newPassLabel = new JLabel("Nowe hasło:");
        newPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPasswordField newPassField = new JPasswordField(20);

        JLabel confirmPassLabel = new JLabel("Potwierdź nowe hasło:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPasswordField confirmPassField = new JPasswordField(20);

        panel.add(oldPassLabel);
        panel.add(oldPassField);
        panel.add(newPassLabel);
        panel.add(newPassField);
        panel.add(confirmPassLabel);
        panel.add(confirmPassField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Zmiana hasła",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPassField.getPassword());
            String newPassword = new String(newPassField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());

            if (newPassword.equals(confirmPassword)) {
                try {
                    if (updatePasswordInDatabase(oldPassword, newPassword)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Hasło zostało zmienione pomyślnie!",
                                "Sukces",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Nieprawidłowe aktualne hasło!",
                                "Błąd",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Błąd podczas zmiany hasła: " + ex.getMessage(),
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Nowe hasła nie są identyczne!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private boolean updatePasswordInDatabase(String oldPassword, String newPassword) throws SQLException {
        return WholesaleCustomerDAO.updatePassword(customerId, oldPassword, newPassword);
    }

    private void setupProductsPanel() {
        productsPanel = new JPanel(new BorderLayout(10, 10));
        productsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        productsPanel.setBackground(Color.WHITE);
        productsPanel.setPreferredSize(new Dimension(0, 400));

        productsTableModel = new ProductTableModel();
        productsTable = new JTable(productsTableModel);
        styleTable(productsTable);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productsScrollPane = new JScrollPane(productsTable);
        productsScrollPane.setPreferredSize(new Dimension(0, 250));
        productsPanel.add(productsScrollPane, BorderLayout.CENTER);

        JPanel productsControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        productsControlPanel.setOpaque(false);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setColumns(3);

        addToCartButton = createStyledButton("Dodaj do koszyka", new Color(46, 204, 113));
        addToCartButton.addActionListener(e -> addToCart());

        productsControlPanel.add(new JLabel("Ilość: "));
        productsControlPanel.add(quantitySpinner);
        productsControlPanel.add(Box.createHorizontalStrut(20));
        productsControlPanel.add(addToCartButton);
        productsPanel.add(productsControlPanel, BorderLayout.SOUTH);
    }

    private void setupCartPanel() {
        cartPanel = new JPanel(new BorderLayout(10, 10));
        cartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setPreferredSize(new Dimension(0, 300));

        cartTableModel = new CartTableModel(koszyk);
        cartTable = new JTable(cartTableModel);
        styleTable(cartTable);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setPreferredSize(new Dimension(0, 200));
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        cartButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cartButtonsPanel.setOpaque(false);

        removeFromCartButton = createStyledButton("Usuń z koszyka", new Color(231, 76, 60));
        removeFromCartButton.addActionListener(e -> removeFromCart());

        // metoda platnosci
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        paymentPanel.setOpaque(false);
        paymentPanel.add(new JLabel("Metoda płatności:"));
        paymentMethodComboBox = new JComboBox<>(new String[]{"gotówka", "karta"});
        paymentPanel.add(paymentMethodComboBox);
        cartButtonsPanel.add(paymentPanel);

        cartButtonsPanel.add(Box.createHorizontalStrut(20));

        placeOrderButton = createStyledButton("Złóż zamówienie", new Color(241, 196, 15));
        placeOrderButton.addActionListener(e -> placeOrder());

        cartButtonsPanel.add(removeFromCartButton);
        cartButtonsPanel.add(placeOrderButton);

        cartPanel.add(cartButtonsPanel, BorderLayout.SOUTH);
    }

    private void setupStatusPanel() {
        statusLabel = new JLabel("Użytkownik: Klient hurtowy | Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
        JPanel1.add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupLayout() {
        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);
        this.setContentPane(JPanel1);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleTable(JTable table) { // stylowanie tabeli
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

    private void addToCart() { //dodaj do koszyka
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = productsTableModel.getProductAt(selectedRow);
            int quantity = (int) quantitySpinner.getValue();

            // Oblicz całkowitą ilość w koszyku (jeśli produkt już istnieje)
            int totalInCart = koszyk.getProdukty().stream()
                    .filter(entry -> entry.getKey().getId() == product.getId())
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            if (quantity + totalInCart > product.getIlosc()) {
                JOptionPane.showMessageDialog(this,
                        "Łączna ilość w koszyku (" + (quantity + totalInCart) +
                                ") przekracza dostępną ilość w magazynie (" + product.getIlosc() + ")!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            koszyk.dodajProdukt(product, quantity);
            cartTableModel.fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(this, "Nie wybrano produktu!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = cartTableModel.getProductAt(selectedRow);
            koszyk.usunProdukt(product);
            cartTableModel.fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(this, "Nie wybrano produktu do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void placeOrder() {
        if (koszyk.getProdukty().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Koszyk jest pusty!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // sprawdź minimalną ilość produktów i minimalną wartość zamówienia
        int totalQuantity = koszyk.getProdukty().stream()
                .mapToInt(Map.Entry::getValue)
                .sum();

        if (totalQuantity < 3) {
            JOptionPane.showMessageDialog(this, "Minimalna ilość produktów w koszyku to 3 sztuki!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalAmount = koszyk.obliczCalkowitaSume();
        if (totalAmount < 50.0) {
            JOptionPane.showMessageDialog(this, "Minimalna wartość zamówienia dla klienta hurtowego to 50 zł!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            saveWholeSaleOrder();
            updateStoreBalance();
            showOrderConfirmation();
            koszyk.getProdukty().clear();
            cartTableModel.fireTableDataChanged();
            loadProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas składania zamówienia: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveWholeSaleOrder() throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            double totalAmount = koszyk.obliczCalkowitaSume();
            int transactionId = TransactionDAO.insertWholesaleTransaction(customerId, totalAmount,
                    (String) paymentMethodComboBox.getSelectedItem());
            TransactionDAO.insertTransactionItems(transactionId, koszyk);
            WholesaleCustomerDAO.updatePurchaseSum(customerId, totalAmount);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    private int insertWholesaleTransaction(Connection conn, double totalAmount) throws SQLException {
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
        double deliveryCost = paymentMethod.equals("karta") ? 15.0 : 25.0;
        totalAmount += deliveryCost;

        String sql = "INSERT INTO transakcje (klient_hurtowy_id, data, calkowita_kwota, typ, metoda_platnosci) " +
                "VALUES (?, NOW(), ?, 'hurtowy', ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, paymentMethod);
            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Tworzenie transakcji nie powiodło się!");
                }
            }
        }
    }

    private void insertTransactionItems(Connection conn, int transactionId) throws SQLException {
        String insertSql = "INSERT INTO pozycje_transakcji (transakcja_id, produkt_id, ilosc, cena_jednostkowa) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE produkty SET ilosc = ilosc - ? WHERE id = ?";

        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
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

            insertStmt.executeBatch();
            updateStmt.executeBatch();
        }
    }

    private void updateClientPurchaseSum(Connection conn, double totalAmount) throws SQLException {
        String sql = "UPDATE klienci_hurtowi SET suma_zakupow = suma_zakupow + ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, totalAmount);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        }
    }

    private void updateStoreBalance() throws SQLException {
        TransactionDAO.updateStoreBalance(koszyk.obliczCalkowitaSume());
    }

    private void showOrderConfirmation() {
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
        double deliveryCost = paymentMethod.equals("karta") ? 15.0 : 25.0;
        double totalAmount = koszyk.obliczCalkowitaSume();
        double totalWithDelivery = totalAmount + deliveryCost;

        StringBuilder message = new StringBuilder();
        message.append("<html><body style='width: 300px;'><h3 style='color: #4682B4;'>Zamówienie zostało złożone!</h3>");
        message.append("<p><b>Produkty:</b></p><ul>");

        for (Map.Entry<Product, Integer> entry : koszyk.getProdukty()) {
            message.append("<li>")
                    .append(entry.getKey().getNazwa())
                    .append(" X ")
                    .append(entry.getValue())
                    .append(" - ")
                    .append(String.format("%.2f zł", entry.getKey().getCena() * entry.getValue()))
                    .append("</li>");
        }

        message.append("</ul>");
        message.append("<p>Metoda płatności: ").append(paymentMethod).append("</p>");
        message.append("<p>Koszt dostawy: ").append(String.format("%.2f zł", deliveryCost)).append("</p>");
        message.append("<p style='font-weight: bold;'>Łączna wartość: ")
                .append(String.format("%.2f zł", totalWithDelivery))
                .append("</p></body></html>");

        JOptionPane.showMessageDialog(this, message.toString(), "Potwierdzenie zamówienia", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editCustomerData() {
        AddEditWholeSaleCustomer editDialog = new AddEditWholeSaleCustomer(this, customerId);
        editDialog.setVisible(true);
        refreshCustomerData();
    }

    private void refreshCustomerData() {
        Component[] components = customerDataPanel.getComponents();
        JPanel customerButtonsPanel = null;

        for (Component comp : components) {
            if (comp instanceof JPanel) {
                customerButtonsPanel = (JPanel) comp;
                break;
            }
        }

        for (Component comp : components) {
            if (comp instanceof JLabel) {
                customerDataPanel.remove(comp);
            }
        }

        // Show or hide customer data based on the checkbox state
        if (showDataCheckBox.isSelected()) {
            loadCustomerData();
        } else {
            clearCustomerData();
        }

        if (customerButtonsPanel != null) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(0, 0, 15, 0);
            customerDataPanel.add(customerButtonsPanel, gbc);
        }

        customerDataPanel.revalidate();
        customerDataPanel.repaint();
    }

    private void loadCustomerData() {
        try (ResultSet rs = WholesaleCustomerDAO.getCustomerData(customerId)) {
            if (rs.next()) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 20);

                addCustomerDataRow("Imię:", rs.getString("imie"), gbc);
                gbc.gridy++;
                addCustomerDataRow("Nazwisko:", rs.getString("nazwisko"), gbc);
                gbc.gridy++;
                addCustomerDataRow("Firma:", rs.getString("nazwa_firmy"), gbc);
                gbc.gridy++;
                addCustomerDataRow("NIP:", rs.getString("NIP"), gbc);
                gbc.gridy++;
                addCustomerDataRow("Adres:", rs.getString("adres"), gbc);
                gbc.gridy++;
                addCustomerDataRow("Telefon:", rs.getString("telefon"), gbc);
                gbc.gridy++;
                addCustomerDataRow("Email:", rs.getString("email"), gbc);
                gbc.gridy++;
                addCustomerDataRow("Suma zakupów:", String.format("%.2f zł", rs.getDouble("suma_zakupow")), gbc);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd ładowania danych klienta: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCustomerDataRow(String label, String value, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        customerDataPanel.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customerDataPanel.add(val, gbc);
    }
}

