package View.Admin.Customer;

import DAO.DatabaseConnection;
import DAO.UserDAO;
import View.Admin.AdminForm;
import View.TableModels.CustomerTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerList extends JFrame {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel controlPanel;
    private JRadioButton Retail;
    private JRadioButton WholeSale;
    private JTable table1;
    private JButton dodajButton;
    private JButton edytujButton;
    private JButton usunButton;
    private JButton showDetails;
    private JButton menuButton;
    private CustomerTableModel tableModel;
    private ButtonGroup customerTypeGroup;
    private JLabel statusLabel;

    public CustomerList() {
        super("Lista klientów");
        initializeComponents();
        setupLayout();
        setupListeners();
        loadCustomers();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        // Ustawienie ikony aplikacji
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());
    }

    private void initializeComponents() {
        // Główny panel
        JPanel1 = new JPanel(new BorderLayout(10, 10));
        JPanel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel1.setBackground(new Color(245, 245, 245));

        // Nagłówek
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Tytuł
        JLabel titleLabel = new JLabel("LISTA KLIENTÓW", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel zawartości
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(245, 245, 245));

        // Panel kontrolny
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        controlPanel.setBackground(Color.WHITE);

        // Radio buttons
        Retail = new JRadioButton("Klienci detaliczni");
        WholeSale = new JRadioButton("Klienci hurtowi");
        customerTypeGroup = new ButtonGroup();
        customerTypeGroup.add(Retail);
        customerTypeGroup.add(WholeSale);
        Retail.setSelected(true);

        // Stylizacja radio buttons
        Retail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        WholeSale.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        controlPanel.add(new JLabel("Typ klienta:"));
        controlPanel.add(Retail);
        controlPanel.add(WholeSale);

        // Tabela
        tableModel = new CustomerTableModel();
        table1 = new JTable(tableModel);
        styleTable(table1);
        table1.setAutoCreateRowSorter(true);
        table1.setFillsViewportHeight(true);

        // Przyciski z ikonami
        dodajButton = createStyledButtonWithIcon("Dodaj", new Color(46, 204, 113), "/figurs/icons8-add-button-50.png");
        edytujButton = createStyledButtonWithIcon("Edytuj", new Color(52, 152, 219), "/figurs/icons8-edit-50.png");
        usunButton = createStyledButtonWithIcon("Usuń", new Color(231, 76, 60), "/figurs/icons8-delete-80.png");
        showDetails = createStyledButton("Szczegóły", new Color(241, 196, 15));
        menuButton = createStyledButton("Menu", new Color(155, 89, 182));

        // Panel przycisków
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(dodajButton);
        buttonsPanel.add(edytujButton);
        buttonsPanel.add(usunButton);
        buttonsPanel.add(showDetails);
        buttonsPanel.add(menuButton);

        // Panel statusu
        statusLabel = new JLabel("Użytkownik: ADMINISTRATOR | Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
    }

    private JButton createStyledButtonWithIcon(String text, Color color, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setHorizontalTextPosition(SwingConstants.LEFT);
            button.setIconTextGap(10);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }

        return button;
    }

    private void setupLayout() {
        // Panel tabeli
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(table1);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel dolny z przyciskami
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JPanel buttonsContainer = new JPanel(new BorderLayout());
        buttonsContainer.setOpaque(false);
        buttonsContainer.add(controlPanel, BorderLayout.NORTH);

        JPanel buttonsWrapper = new JPanel(new BorderLayout());
        buttonsWrapper.setOpaque(false);
        buttonsWrapper.add(new JPanel(), BorderLayout.CENTER); // Pusty panel do centrowania
        buttonsWrapper.add(createButtonsPanel(), BorderLayout.EAST);

        buttonsContainer.add(buttonsWrapper, BorderLayout.SOUTH);
        bottomPanel.add(buttonsContainer, BorderLayout.SOUTH);

        // Dodanie komponentów do panelu zawartości
        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Dodanie komponentów do głównego panelu
        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);

        this.setContentPane(JPanel1);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(dodajButton);
        buttonsPanel.add(edytujButton);
        buttonsPanel.add(usunButton);
        buttonsPanel.add(showDetails);
        buttonsPanel.add(menuButton);
        return buttonsPanel;
    }

    private void setupListeners() {
        // Timer aktualizujący status
        new Timer(1000, e -> {
            statusLabel.setText("Użytkownik: ADMINISTRATOR | Data: " + new java.util.Date().toString());
        }).start();

        Retail.addActionListener(e -> loadCustomers());
        WholeSale.addActionListener(e -> loadCustomers());

        dodajButton.addActionListener(e -> addCustomer());
        edytujButton.addActionListener(e -> editCustomer());
        usunButton.addActionListener(e -> deleteCustomer());
        showDetails.addActionListener(e -> showCustomerDetails());

        menuButton.addActionListener(e -> {
            dispose();
            new AdminForm();
        });
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

    private void showCustomerDetails() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Proszę wybrać klienta", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean isRetail = Retail.isSelected();

        new CustomerDetails(this, customerId, isRetail);
    }

    private void loadCustomers() {
        boolean isRetail = Retail.isSelected();
        tableModel.setRetail(isRetail);
        tableModel.loadData();
    }

    private void deleteCustomer() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Proszę wybrać klienta do usunięcia", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int customerID = (int) tableModel.getValueAt(selectedRow, 0);
        boolean isRetail = Retail.isSelected();

        if (hasRelatedTransactions(customerID, isRetail)) {
            JOptionPane.showMessageDialog(null, "Nie można usunąć klienta z transakcjami", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Czy na pewno chcesz usunąć tego klienta?",
                "Usuwanie",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UserDAO userDAO = new UserDAO();

                if (!isRetail) {
                    int userId = userDAO.getUserIdByWholesaleId(customerID);
                    userDAO.deleteUser(userId);
                }

                String tableName = isRetail ? "klienci_detaliczni" : "klienci_hurtowi";
                String sql = "DELETE FROM " + tableName + " WHERE id = ?";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, customerID);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Usunięto klienta", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        loadCustomers();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd podczas usuwania klienta: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean hasRelatedTransactions(int customerID, boolean isRetail) {
        String query = isRetail ? "SELECT COUNT(*) FROM transakcje WHERE klient_detaliczny_id = ?" : "SELECT COUNT(*) FROM transakcje WHERE klient_hurtowy_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private void editCustomer() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Proszę wybrać klienta do edycji", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int customerID = (int) tableModel.getValueAt(selectedRow, 0);
        boolean isRetail = Retail.isSelected();
        if(isRetail){
            new AddEditRetailCustomer(this, customerID).setVisible(true);
        }else{
            new AddEditWholeSaleCustomer(this, customerID).setVisible(true);
        }
        loadCustomers();
    }

    private void addCustomer() {
        boolean isRetail = Retail.isSelected();
        if(isRetail){
            JOptionPane.showMessageDialog(this, "Dodawanie nowych klientów detalicznych jest zablokowane", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }else{
            new AddEditWholeSaleCustomer(this, null).setVisible(true);
        }
        loadCustomers();
    }
}