package View.Admin.Warehouse;

import DAO.WarehouseOrderDAO;
import Model.WarehouseOrder;
import Model.WarehouseOrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class WarehouseOrderHistoryForm extends JFrame {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JLabel logoLabel;
    private JLabel titleLabel;
    private JLabel statusLabel;
    private JTable ordersTable;
    private JScrollPane tableScrollPane;
    private JButton closeButton;

    public WarehouseOrderHistoryForm() {
        super("Historia zamówień magazynu");
        initializeComponents();
        setupLayout();
        setupListeners();
        loadOrderHistory();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());
    }

    private void initializeComponents() {
        // Main panel
        JPanel1 = new JPanel(new BorderLayout(10, 10));
        JPanel1.setBackground(new Color(245, 245, 245));
        JPanel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        titleLabel = new JLabel("HISTORIA ZAMÓWIEŃ MAGAZYNU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        statusLabel = new JLabel("Data: ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);
        headerPanel.add(statusPanel, BorderLayout.SOUTH);


        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));


        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[] {
                "ID Zamówienia", "Data zamówienia", "ID Produktu", "Ilość", "Dostawca", "Koszt całkowity (zł)"
        });
        ordersTable = new JTable(model);
        styleTable(ordersTable);
        tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);


        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));

        closeButton = createStyledButton("Zamknij", new Color(231, 76, 60));
        buttonPanel.add(closeButton);
    }

    private void setupLayout() {
        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);
        JPanel1.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(JPanel1);
    }

    private void setupListeners() {
        closeButton.addActionListener(e -> dispose());

        new Timer(1000, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statusLabel.setText("Data: " + dateFormat.format(new java.util.Date()));
        }).start();
    }

    private void loadOrderHistory() {
        try {
            WarehouseOrderDAO dao = new WarehouseOrderDAO();
            List<WarehouseOrder> orders = dao.getWarehouseOrdersWithDetails();

            DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
            model.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (orders.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Brak historii zamówień.",
                        "Informacja",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (WarehouseOrder order : orders) {
                // weź tylko pierwsze zamówienie dla każdego produktu
                for (WarehouseOrderItem item : order.getItems()) {
                    Object[] row = new Object[] {
                            order.getId(),
                            order.getOrderDate() != null ? dateFormat.format(order.getOrderDate()) : "",
                            item.getProductId(),
                            item.getQuantity(),
                            order.getSupplier(),
                            String.format("%.2f", item.getUnitPrice() * item.getQuantity())
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas pobierania historii zamówień: " + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
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
        table.getTableHeader().setReorderingAllowed(false);
    }
}

