package View.Admin;

import DAO.DatabaseConnection;
import DAO.StatisticsDAO;
import DAO.TransactionDetailsDAO;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ManagementTransactionForm extends JFrame {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel filterPanel;
    private JPanel tablePanel;
    private JPanel statsPanel;
    private JPanel buttonPanel;
    private JLabel logoLabel;
    private JLabel titleLabel;
    private JLabel statusLabel;
    private JRadioButton wszystkieRadioButton;
    private JRadioButton detaliczneRadioButton;
    private JRadioButton hurtoweRadioButton;
    private JLabel datefromLabel;
    private JButton filterButton;
    private JTable transactionsTable;
    private JButton generateReportButton;
    private JButton refreshButton;
    private JLabel totalTransactionsLabel;
    private JLabel totalValueLabel;
    private JLabel averageValueLabel;
    private JButton backButton;
    private JList<String> productsList;
    private JButton showDetailsButton;
    private JTextPane transactionDetailsPane;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ManagementTransactionForm() {
        super("Zarządzanie transakcjami");
        initializeComponents();
        setupLayout();
        setupListeners();
        setDefaultDates();
        loadTransactions();
        updateStatistics();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 1000);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        // Ustawienie ikony aplikacji
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());
    }

    private void setDefaultDates() {
        // Data końcowa - dzisiaj
        toDatePicker.setDate(LocalDate.now());

        // Data początkowa - miesiąc wstecz
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        fromDatePicker.setDate(monthAgo);
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
        titleLabel = new JLabel("ZARZĄDZANIE TRANSAKCJAMI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel zawartości
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(245, 245, 245));

        // Panel filtrów
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        filterPanel.setBackground(Color.WHITE);

        // Radio buttony
        wszystkieRadioButton = new JRadioButton("Wszystkie");
        detaliczneRadioButton = new JRadioButton("Detaliczne");
        hurtoweRadioButton = new JRadioButton("Hurtowe");
        ButtonGroup transactionTypeGroup = new ButtonGroup();
        transactionTypeGroup.add(wszystkieRadioButton);
        transactionTypeGroup.add(detaliczneRadioButton);
        transactionTypeGroup.add(hurtoweRadioButton);
        wszystkieRadioButton.setSelected(true);

        // Data od
        datefromLabel = new JLabel("Od:");
        fromDatePicker = createDatePicker();

        // Data do
        JLabel dateToLabel = new JLabel("Do:");
        toDatePicker = createDatePicker();

        // Przycisk filtrowania
        filterButton = createStyledButton("Filtruj", new Color(52, 152, 219));

        // Dodanie komponentów do panelu filtrów
        filterPanel.add(new JLabel("Typ transakcji:"));
        filterPanel.add(wszystkieRadioButton);
        filterPanel.add(detaliczneRadioButton);
        filterPanel.add(hurtoweRadioButton);
        filterPanel.add(datefromLabel);
        filterPanel.add(fromDatePicker);
        filterPanel.add(dateToLabel);
        filterPanel.add(toDatePicker);
        filterPanel.add(filterButton);

        // Tabela transakcji
        String[] columnNames = {"ID", "Data", "Typ", "Klient", "Kwota", "Metoda płatności", "Liczba produktów"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionsTable = new JTable(model);
        styleTable(transactionsTable);
        transactionsTable.setAutoCreateRowSorter(true);

        transactionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showSelectedTransactionDetails();
                }
            }
        });

        // Panel tabeli
        tablePanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(transactionsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel szczegółów transakcji
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Szczegóły transakcji"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        detailsPanel.setBackground(Color.WHITE);

        transactionDetailsPane = new JTextPane();
        transactionDetailsPane.setContentType("text/html");
        transactionDetailsPane.setEditable(false);
        transactionDetailsPane.setText("<html><body style='padding:15px; font-family:Arial; font-size:14px;'><h3 style='color:#2c3e50;'>Wybierz transakcję aby zobaczyć szczegóły</h3></body></html>");
        JScrollPane detailsScrollPane = new JScrollPane(transactionDetailsPane);

        productsList = new JList<>();
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productsScrollPane = new JScrollPane(productsList);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, detailsScrollPane, productsScrollPane);
        splitPane.setResizeWeight(0.6);
        detailsPanel.add(splitPane, BorderLayout.CENTER);

        showDetailsButton = createStyledButton("Pokaż szczegóły", new Color(241, 196, 15));

        JPanel detailsButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        detailsButtonPanel.setOpaque(false);
        detailsButtonPanel.add(showDetailsButton);
        detailsPanel.add(detailsButtonPanel, BorderLayout.SOUTH);

        statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Statystyki"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        statsPanel.setBackground(Color.WHITE);

        totalTransactionsLabel = new JLabel("Łączna liczba: 0", SwingConstants.CENTER);
        totalValueLabel = new JLabel("Łączna wartość: 0.00 zł", SwingConstants.CENTER);
        averageValueLabel = new JLabel("Średnia wartość: 0.00 zł", SwingConstants.CENTER);

        totalTransactionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        averageValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        statsPanel.add(totalTransactionsLabel);
        statsPanel.add(totalValueLabel);
        statsPanel.add(averageValueLabel);

        buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        generateReportButton = createStyledButton("Raport", new Color(155, 89, 182));
        refreshButton = createStyledButton("Odśwież", new Color(26, 188, 156));
        backButton = createStyledButton("Menu", new Color(241, 196, 15));


        buttonPanel.add(generateReportButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        statusLabel = new JLabel("Użytkownik: ADMINISTRATOR | Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
    }

    private DatePicker createDatePicker() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setAllowEmptyDates(false);

        DatePicker datePicker = new DatePicker(settings);
        datePicker.setPreferredSize(new Dimension(150, 30));
        datePicker.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return datePicker;
    }

    private void setupLayout() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel detailsStatsPanel = new JPanel(new BorderLayout());
        detailsStatsPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel detailsWrapper = new JPanel(new BorderLayout());
        detailsWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        detailsWrapper.add(new JLabel("Szczegóły wybranej transakcji:"), BorderLayout.NORTH);

        JPanel transactionDetailsPanel = new JPanel(new BorderLayout());
        transactionDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        transactionDetailsPanel.setBackground(Color.WHITE);

        transactionDetailsPane.setPreferredSize(new Dimension(400, 150));
        transactionDetailsPanel.add(new JScrollPane(transactionDetailsPane), BorderLayout.CENTER);

        JPanel productsPanel = new JPanel(new BorderLayout());
        productsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Produkty w transakcji"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        productsPanel.add(new JScrollPane(productsList), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, transactionDetailsPanel, productsPanel);
        splitPane.setResizeWeight(0.5);
        detailsWrapper.add(splitPane, BorderLayout.CENTER);

        detailsStatsPanel.add(detailsWrapper, BorderLayout.CENTER);
        southPanel.add(detailsStatsPanel, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);

        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);
        JPanel1.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(JPanel1);
    }

    private void setupListeners() {
        new Timer(1000, e -> statusLabel.setText("Użytkownik: ADMINISTRATOR | Data: " + new Date().toString())).start();

        filterButton.addActionListener(e -> {
            loadTransactions();
            updateStatistics();
        });
        refreshButton.addActionListener(e -> {
            loadTransactions();
            updateStatistics();
        });
        showDetailsButton.addActionListener(e -> showSelectedTransactionDetails());
        backButton.addActionListener(e -> {
            dispose();
            new AdminForm();
        });
        generateReportButton.addActionListener(e -> generateCSVRaport());
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            String iconPath = null;
            if (text.equals("Filtruj")) {
                iconPath = "/figurs/icons8-sort-amount-up-100.png";
            } else if (text.equals("Raport")) {
                iconPath = "/figurs/icons8-export-csv-100.png";
            } else if (text.equals("Odśwież")) {
                iconPath = "/figurs/icons8-refresh-button-64.png";
            }

            if (iconPath != null) {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                // skaluje ikonę do odpowiedniej wielkości
                Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledIcon));
                button.setHorizontalTextPosition(SwingConstants.LEFT);
                button.setIconTextGap(10);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }

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

    private void loadTransactions() {
        DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
        model.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder query = new StringBuilder(
                    "SELECT t.id, t.data, t.typ, t.calkowita_kwota, t.metoda_platnosci, COUNT(pt.id) as liczba_produktow, " +
                            "CASE WHEN t.typ = 'hurtowy' THEN kh.nazwa_firmy WHEN t.typ = 'detaliczny' THEN CONCAT(kd.imie, ' ', kd.nazwisko) " +
                            "END as klient FROM transakcje t LEFT JOIN pozycje_transakcji pt ON t.id = pt.transakcja_id " +
                            "LEFT JOIN klienci_hurtowi kh ON t.klient_hurtowy_id = kh.id LEFT JOIN klienci_detaliczni kd ON t.klient_detaliczny_id = kd.id WHERE 1=1 "
            );

            if (detaliczneRadioButton.isSelected()) {
                query.append(" AND t.typ = 'detaliczny'");
            } else if (hurtoweRadioButton.isSelected()) {
                query.append(" AND t.typ = 'hurtowy'");
            }

            // Pobranie dat z DatePickerów
            LocalDate fromLocalDate = fromDatePicker.getDate();
            LocalDate toLocalDate = toDatePicker.getDate();

            if (fromLocalDate != null) {
                Date fromDate = Date.from(fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                query.append(" AND t.data >= '").append(dateFormat.format(fromDate)).append("'");
            }
            if (toLocalDate != null) {
                Date toDate = Date.from(toLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                query.append(" AND t.data <= '").append(dateFormat.format(toDate)).append(" 23:59:59'");
            }
            query.append(" GROUP BY t.id ORDER BY t.data DESC");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query.toString());

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getTimestamp("data"),
                        rs.getString("typ"),
                        rs.getString("klient"),
                        rs.getBigDecimal("calkowita_kwota"),
                        rs.getString("metoda_platnosci"),
                        rs.getInt("liczba_produktow")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            showError("Błąd podczas ładowania transakcji: " + e.getMessage());
        }
    }

    private void updateStatistics() {
        try {
            StatisticsDAO dao = new StatisticsDAO();
            String transactionType = null;
            java.sql.Date fromSqlDate = null;
            java.sql.Date toSqlDate = null;

            // Pobierz typ transakcji z radio buttonów
            if (detaliczneRadioButton.isSelected()) {
                transactionType = "detaliczny";
            } else if (hurtoweRadioButton.isSelected()) {
                transactionType = "hurtowy";
            }

            // Pobierz daty z DatePickerów
            LocalDate fromLocalDate = fromDatePicker.getDate();
            LocalDate toLocalDate = toDatePicker.getDate();

            if (fromLocalDate != null) {
                Date fromDate = Date.from(fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                fromSqlDate = new java.sql.Date(fromDate.getTime());
            }

            if (toLocalDate != null) {
                Date toDate = Date.from(toLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                toSqlDate = new java.sql.Date(toDate.getTime());
            }

            // Pobierz statystyki dla przefiltrowanych transakcji
            StatisticsDAO.TransactionStats stats = dao.getFilteredTransactionStatistics(transactionType, fromSqlDate, toSqlDate);

            totalTransactionsLabel.setText("Łączna liczba: " + stats.totalCount);
            totalValueLabel.setText(String.format("Łączna wartość: %.2f zł", stats.totalSum));
            averageValueLabel.setText(String.format("Średnia wartość: %.2f zł", stats.average));
        } catch (SQLException e) {
            showError("Błąd podczas ładowania statystyk: " + e.getMessage());
        }
    }

    private void showSelectedTransactionDetails() {
        int selectedRow = transactionsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String transactionId = transactionsTable.getValueAt(selectedRow, 0).toString();
            updateTransactionDetails(transactionId);
        }
    }

    private void updateTransactionDetails(String transactionId) {
        try {
            TransactionDetailsDAO dao = new TransactionDetailsDAO();
            int id = Integer.parseInt(transactionId);

            // Pobierz szczegóły transakcji
            TransactionDetailsDAO.TransactionDetail detail = dao.getTransactionDetails(id);
            if (detail != null) {
                // Formatowanie szczegółów transakcji
                StringBuilder details = new StringBuilder();
                details.append("<html><body style='padding:10px'>");
                details.append("<h3>Szczegóły transakcji #").append(transactionId).append("</h3>");
                details.append("<p><b>Data:</b> ").append(detail.data).append("<br>");
                details.append("<b>Typ:</b> ").append(detail.typ).append("<br>");
                details.append("<b>Metoda płatności:</b> ").append(detail.metodaPlatnosci).append("<br>");
                details.append("<b>Łączna kwota:</b> ").append(detail.calkowitaKwota).append(" zł</p>");
                details.append("</body></html>");

                transactionDetailsPane.setText(details.toString());

                // Pobierz produkty dla transakcji
                List<TransactionDetailsDAO.ProductInTransaction> products = dao.getProductsInTransaction(id);
                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (TransactionDetailsDAO.ProductInTransaction p : products) {
                    String productInfo = String.format("%s (Ilość: %d, Cena: %.2f zł)",
                            p.nazwa, p.ilosc, p.cenaJednostkowa);
                    listModel.addElement(productInfo);
                }
                // Dodaj koszt dostawy na końcu listy produktów
                double deliveryCost = 0.0;
                String paymentMethod = detail.metodaPlatnosci.toLowerCase();
                if (paymentMethod.contains("karta")) {
                    deliveryCost = 15.0;
                } else if (paymentMethod.contains("gotówka") || paymentMethod.contains("gotowka")) {
                    deliveryCost = 25.0;
                }
                String deliveryInfo = String.format("Koszt dostawy: %.2f zł", deliveryCost);
                listModel.addElement(deliveryInfo);

                productsList.setModel(listModel);
            }
        } catch (SQLException e) {
            showError("Błąd podczas ładowania szczegółów transakcji: " + e.getMessage());
        }
    }

    private void generateCSVRaport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz raport CSV");
        fileChooser.setSelectedFile(new File("raport_transakcji.csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                // Nagłówki kolumn
                writer.append("ID;Data;Typ;Klient;Kwota;Metoda platnosci;Liczba produktow\n");

                // Dane transakcji
                DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    writer.append(String.valueOf(model.getValueAt(i, 0))).append(";");
                    writer.append(String.valueOf(model.getValueAt(i, 1))).append(";");
                    writer.append(String.valueOf(model.getValueAt(i, 2))).append(";");
                    writer.append(String.valueOf(model.getValueAt(i, 3))).append(";");
                    writer.append(String.valueOf(model.getValueAt(i, 4))).append(";");
                    writer.append(String.valueOf(model.getValueAt(i, 5))).append(";");
                    writer.append(String.valueOf(model.getValueAt(i, 6))).append("\n");
                }

                JOptionPane.showMessageDialog(this,
                        "Raport został pomyślnie wygenerowany!",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                showError("Błąd podczas zapisywania pliku: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Błąd",
                JOptionPane.ERROR_MESSAGE);
    }
}

