package View.Admin.Warehouse;

import DAO.ProductsDAO;
import DAO.ReportsDAO;
import DAO.WarehouseOrderDAO;
import Model.Products.Product;
import View.TableModels.ProductTableModel;
import Model.WarehouseOrder;
import View.Admin.AdminForm;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WarehouseForm extends JFrame {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel controlPanel;
    private JPanel buttonPanel;
    private JPanel saldoPanel;
    private JLabel logoLabel;
    private JLabel titleLabel;
    private JLabel statusLabel;
    private JComboBox<String> sortComboBox;
    private JComboBox<String> filterComboBox;
    private JButton generateReportButton;
    private JTable productsTable;
    private JButton orderProductButton;
    private JButton editStockButton;
    private JButton historyButton;
    private JLabel saldoLabel;
    private JButton menuButton;
    private ProductTableModel tableModel;
    private ProductsDAO productsDAO;

    public WarehouseForm() {
        super("Magazyn");
        initializeComponents();
        setupLayout();
        setupListeners();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Tytuł
        titleLabel = new JLabel("PANEL MAGAZYNU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel zawartości
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(245, 245, 245));

        // Panel kontrolny
        controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        controlPanel.setBackground(Color.WHITE);

        // Inicjalizacja komponentów
        productsDAO = new ProductsDAO();
        tableModel = new ProductTableModel();
        productsTable = new JTable(tableModel);
        styleTable(productsTable);

        // ComboBoxy
        sortComboBox = new JComboBox<>(new String[]{
                "ID (rosnąco)", "ID (malejąco)",
                "Nazwa (A-Z)", "Nazwa (Z-A)",
                "Cena (rosnąco)", "Cena (malejąco)",
                "Ilość (rosnąco)", "Ilość (malejąco)"
        });

        filterComboBox = new JComboBox<>(new String[]{
                "Wszystkie produkty", "Niskie stany (<20)",
                "Brak na stanie", "Elektronika",
                "Odzież", "Sprzęt", "Materiały"
        });

        // Przyciski
        generateReportButton = createStyledButtonWithIcon("Generuj raport", new Color(52, 152, 219),"/figurs/icons8-export-csv-100.png");
        orderProductButton = createStyledButtonWithIcon("Zamów produkt", new Color(46, 204, 113),"/figurs/icons8-create-order-50.png");
        editStockButton = createStyledButtonWithIcon("Edytuj stan", new Color(241, 196, 15),"/figurs/icons8-edit-50.png");
        historyButton = createStyledButtonWithIcon("Historia zamówień", new Color(155, 89, 182),"/figurs/icons8-order-history-50.png");
        menuButton = createStyledButton("Menu", new Color(231, 76, 60));



        // Saldo
        saldoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saldoPanel.setOpaque(false);

        saldoLabel = new JLabel("Saldo magazynu: ");
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel saldoValueLabel = new JLabel("ładowanie...");
        saldoValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        saldoValueLabel.setForeground(new Color(0, 100, 0)); // Dark green color

        saldoPanel.add(saldoLabel);
        saldoPanel.add(saldoValueLabel);


        // Panel statusu
        statusLabel = new JLabel("Użytkownik: ADMINISTRATOR | Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sortowanie
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Sortuj według:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        controlPanel.add(sortComboBox, gbc);

        // Filtrowanie
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Filtruj według:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        controlPanel.add(filterComboBox, gbc);

        // Saldo
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        controlPanel.add(saldoPanel, gbc);
        gbc.anchor = GridBagConstraints.WEST;

        // Panel tabeli
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel przycisków
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(orderProductButton);
        buttonPanel.add(editStockButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(menuButton);

        // Dodanie komponentów do panelu zawartości
        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Dodanie komponentów do głównego panelu
        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);
        JPanel1.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(JPanel1);
    }

    private void setupListeners() {
        // Timer aktualizujący status
        new Timer(1000, e -> {
            statusLabel.setText("Użytkownik: ADMINISTRATOR | Data: " + new Date().toString());
        }).start();

        sortComboBox.addActionListener(e -> loadProducts());
        filterComboBox.addActionListener(e -> loadProducts());

        generateReportButton.addActionListener(e -> generateReport());
        orderProductButton.addActionListener(e -> orderProducts());
        editStockButton.addActionListener(e -> editStock());
        historyButton.addActionListener(e -> showOrderHistory());

        menuButton.addActionListener(e -> {
            dispose();
            new AdminForm();
        });

        // Inicjalne ładowanie danych
        updateSaldoLabel();
        loadProducts();
    };



    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(200, 200, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void updateSaldoLabel() {
        WarehouseOrderDAO dao = new WarehouseOrderDAO();
        double balance = dao.getCurrentBalance();

        // Find the saldoValueLabel in the panel
        Component[] components = controlPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component panelComp : panel.getComponents()) {
                    if (panelComp instanceof JLabel && !((JLabel) panelComp).getText().contains("Saldo")) {
                        JLabel valueLabel = (JLabel) panelComp;
                        valueLabel.setText(String.format("%.2f zł", balance));

                        // Change color based on balance
                        if (balance < 0) {
                            valueLabel.setForeground(Color.RED);
                        } else {
                            valueLabel.setForeground(new Color(0, 100, 0)); // Dark green
                        }
                    }
                }
            }
        }
    }

    private void showOrderHistory() {
        try {
            WarehouseOrderDAO orderDAO = new WarehouseOrderDAO();
            List<WarehouseOrder> orders = orderDAO.getWarehouseOrdersWithDetails();

            if (orders.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Brak historii zamówień.",
                        "Informacja",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            WarehouseOrderHistoryForm orderHistoryFrame = new WarehouseOrderHistoryForm();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas pobierania historii zamówień: " + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void editStock() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Proszę wybrać produkt do edycji.", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product selectedProduct = tableModel.getProductAt(productsTable.convertRowIndexToModel(selectedRow));

        // Create dialog components
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JTextField quantityField = new JTextField(10);
        quantityField.setText(String.valueOf(selectedProduct.getIlosc()));
        
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        inputPanel.add(new JLabel("Nowa ilość:"));
        inputPanel.add(quantityField);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Edytuj stan magazynowy - " + selectedProduct.getNazwa(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
        if (result == JOptionPane.OK_OPTION) {
            try {
                int newQuantity = Integer.parseInt(quantityField.getText().trim());
                if (newQuantity < 0) {
                    JOptionPane.showMessageDialog(null, "Ilość nie może być ujemna", "Błąd", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ProductsDAO productsDAO = new ProductsDAO();
                productsDAO.updateProductQuantity(selectedProduct.getId(), newQuantity);
                JOptionPane.showMessageDialog(null, "Stan magazynowy został zaaktualizowany.",
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);

                loadProducts();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Proszę podać poprawną liczbę.", "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Błąd podczas aktualizacji stanu magazynowego: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void orderProducts() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Proszę wybrać produkt do zamówienia.", "Brak wyboru", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product selectedProduct = tableModel.getProductAt(productsTable.convertRowIndexToModel(selectedRow));
        
        // Create dialog components
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField iloscField = new JTextField(10);
        JTextField dostawcaField = new JTextField(20);
        JTextArea uwagiArea = new JTextArea(3, 20);
        uwagiArea.setLineWrap(true);
        
        panel.add(new JLabel("Ilość:"));
        panel.add(iloscField);
        panel.add(new JLabel("Dostawca:"));
        panel.add(dostawcaField);
        panel.add(new JLabel("Uwagi:"));
        panel.add(new JScrollPane(uwagiArea));
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Zamów produkt - " + selectedProduct.getNazwa(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        WarehouseOrderDAO dao = new WarehouseOrderDAO();
        if (result == JOptionPane.OK_OPTION) {
            try {
                int ilosc = Integer.parseInt(iloscField.getText().trim());
                String dostawca = dostawcaField.getText().trim();
                String uwagi = uwagiArea.getText().trim();
                
                double totalCost = selectedProduct.getCena() * ilosc;
                
                dao.createWarehouseOrder(selectedProduct.getId(), ilosc, dostawca, uwagi);
                updateSaldoLabel();

                JOptionPane.showMessageDialog(this,
                        String.format("Zamówienie zostało złożone pomyślnie. Koszt: %.2f zł", totalCost),
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Proszę podać poprawną liczbę.", "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Błąd podczas złożenia zamówienia: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz raport CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki CSV (*.csv)", "csv"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        String defaultFileName = "raport_stanow_" + dateFormat.format(new Date()) + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
            }

            try {
                ReportsDAO reportsDAO = new ReportsDAO();
                reportsDAO.generateLowStockReport(20, selectedFile.getAbsolutePath());

                JOptionPane.showMessageDialog(null, "Raport zostal wygenerowany pomyslnie!\n" +
                                "Lokalizacja: " + selectedFile.getAbsolutePath(),
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(null, "Blad podczas generowania raportu: " + e.getMessage(),
                        "Blad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadProducts() {
        try {
            String sortColumn = "id";
            String sortOrder = "ASC";

            switch (sortComboBox.getSelectedIndex()) {
                case 0: sortColumn = "id"; sortOrder = "ASC"; break;
                case 1: sortColumn = "id"; sortOrder = "DESC"; break;
                case 2: sortColumn = "nazwa"; sortOrder = "ASC"; break;
                case 3: sortColumn = "nazwa"; sortOrder = "DESC"; break;
                case 4: sortColumn = "cena"; sortOrder = "ASC"; break;
                case 5: sortColumn = "cena"; sortOrder = "DESC"; break;
                case 6: sortColumn = "ilosc"; sortOrder = "ASC"; break;
                case 7: sortColumn = "ilosc"; sortOrder = "DESC"; break;
                default: sortColumn = "id"; sortOrder = "ASC";
            }

            List<Product> products;

            switch (filterComboBox.getSelectedIndex()) {
                case 0: // wszystkie produkty
                    products = productsDAO.getWarehouseProductsSorted(1, 100, sortColumn, sortOrder);
                    break;
                case 1: // niskie stany (<20)
                    products = productsDAO.getWarehouseLowStockProducts(20, sortColumn, sortOrder);
                    break;
                case 2: // brak na stanie
                    products = productsDAO.getWarehouseOutOfStockProducts(sortColumn, sortOrder);
                    break;
                case 3: // elektronika
                    products = productsDAO.getWarehouseProductsByType("elektronika", sortColumn, sortOrder);
                    break;
                case 4: // odzież
                    products = productsDAO.getWarehouseProductsByType("odziez", sortColumn, sortOrder);
                    break;
                case 5: // sprzęt
                    products = productsDAO.getWarehouseProductsByType("sprzet", sortColumn, sortOrder);
                    break;
                case 6: // materiały
                    products = productsDAO.getWarehouseProductsByType("materialy", sortColumn, sortOrder);
                    break;
                default:
                    products = productsDAO.getWarehouseProductsSorted(1, 100, sortColumn, sortOrder);
            }

            tableModel.setProducts(products);
            tableModel.fireTableDataChanged();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Błąd podczas ładowania produktów: " + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButtonWithIcon(String text, Color color, String iconPath) {
        JButton button = createStyledButton(text, color);

        // Dodanie ikony
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        if (icon.getImage() != null) {
            Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setHorizontalTextPosition(SwingConstants.LEFT);
            button.setIconTextGap(10);
        }

        return button;
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

}