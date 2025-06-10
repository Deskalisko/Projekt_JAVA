package View.Admin;

import View.Admin.Customer.CustomerList;
import Model.Transaction;
import DAO.TransactionDAO;
import View.MainForm;
import View.Admin.Warehouse.WarehouseForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdminForm extends JFrame {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JLabel logoLabel;
    private JLabel titleLabel;
    private JLabel statusLabel;
    private JButton clientsButton;
    private JButton warehouseButton;
    private JButton transactionsButton;
    private JButton logoutButton;
    private JButton closeButton;
    private JTable transactionsTable;
    private JLabel recentTransactionsLabel;
    private JScrollPane tableScrollPane;

    public AdminForm() {
        super("Panel Administratora");
        initializeComponents();
        setupLayout();
        setupListeners();
        loadTransactions();

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
        titleLabel = new JLabel("PANEL ADMINISTRATORA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel zawartości
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(245, 245, 245));

        // Etykieta ostatnich transakcji
        recentTransactionsLabel = new JLabel("Ostatnie transakcje:");
        recentTransactionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recentTransactionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Tabela transakcji
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[]{"ID", "Data", "Typ", "Klient", "Email", "Kwota", "Płatność"});

        transactionsTable = new JTable(model);
        styleTable(transactionsTable);
        tableScrollPane = new JScrollPane(transactionsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);


        // Panel przycisków
        buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Przyciski z ikonami
        clientsButton = createStyledButtonWithIcon("Klienci", new Color(52, 152, 219), "/figurs/icons8-people-100.png");
        warehouseButton = createStyledButtonWithIcon("Magazyn", new Color(155, 89, 182), "/figurs/icons8-warehouse-64.png");
        transactionsButton = createStyledButtonWithIcon("Transakcje", new Color(26, 188, 156), "/figurs/icons8-transaction-100.png");
        logoutButton = createStyledButton("Wyloguj", new Color(241, 196, 15));
        closeButton = createStyledButton("Zamknij", new Color(231, 76, 60));

        // Panel statusu
        statusLabel = new JLabel("Użytkownik: ADMINISTRATOR | Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
    }

    private void setupLayout() {
        // Dodanie komponentów do panelu zawartości
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(recentTransactionsLabel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        tablePanel.setBackground(new Color(245, 245, 245));

        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Dodanie przycisków do panelu przycisków
        buttonPanel.add(clientsButton);
        buttonPanel.add(warehouseButton);
        buttonPanel.add(transactionsButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(closeButton);

        // Dodanie komponentów do głównego panelu
        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);
        JPanel1.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(this.JPanel1);
    }

    private void setupListeners() {
        // Timer aktualizujący status
        new Timer(1000, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statusLabel.setText("Użytkownik: ADMINISTRATOR | Data: " + dateFormat.format(new Date()));
        }).start();

        closeButton.addActionListener(e -> dispose());

        logoutButton.addActionListener(e -> {
            dispose();
            new MainForm();
        });

        transactionsButton.addActionListener(e -> {
            dispose();
            new ManagementTransactionForm();
        });

        clientsButton.addActionListener(e -> {
            dispose();
            new CustomerList();
        });

        warehouseButton.addActionListener(e -> {
            dispose();
            new WarehouseForm();
        });
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efekt hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JButton createStyledButtonWithIcon(String text, Color color, String iconPath) {
        JButton button = createStyledButton(text, color);

        // Dodanie ikony
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        if (icon.getImage() != null) {
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setIconTextGap(5);
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

        // Pozwól na przewijanie myszą
        table.addMouseWheelListener(e -> {
            JScrollBar verticalScrollBar = tableScrollPane.getVerticalScrollBar();
            if (verticalScrollBar != null) {
                int direction = e.getWheelRotation() > 0 ? 1 : -1;
                verticalScrollBar.setValue(verticalScrollBar.getValue() + (direction * table.getRowHeight() * 3));
            }
        });

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
    }

    private void loadTransactions() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    TransactionDAO dao = new TransactionDAO();
                    List<Transaction> transactions = dao.getRecentTransactions();
                    DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();

                    SwingUtilities.invokeLater(() -> model.setRowCount(0));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    for (Transaction transaction : transactions) {
                        String klient = transaction.getImieKlienta() + " " + transaction.getNazwiskoKlienta();
                        Object[] row = new Object[]{
                                transaction.getTransakcja_id(),
                                dateFormat.format(transaction.getData()),
                                transaction.getTyp(),
                                klient,
                                transaction.getEmailKlienta(),
                                String.format("%.2f PLN", transaction.getCalkowita_kwota()),
                                transaction.getMetoda_platnosci()
                        };

                        SwingUtilities.invokeLater(() -> model.addRow(row));
                    }
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Błąd podczas pobierania danych z bazy danych " + e.getMessage(),
                                "Błąd", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    });
                }
                return null;
            }
        }.execute();
    }
}