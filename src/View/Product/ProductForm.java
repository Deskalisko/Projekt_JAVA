package View.Product;

import Model.Products.*;
import DAO.ProductsDAO;
import View.Shopping.ShopRetailForm;
import View.MainForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.List;

public class ProductForm extends JFrame {
    private JPanel JPanel1;
    private JTable productTable;
    private JComboBox<String> sort;
    private JComboBox<String> filter;
    private JTextField search;
    private JButton searchButton;
    private JButton prevButton;
    private JButton nextButton;
    private JButton refreshButton;
    private JLabel pageLabel;
    private JButton shoppingButton;
    private JButton closeButton;
    private int currentPage = 1;
    private int totalPages = 1;
    private int dynamicPageSize;
    private final int MIN_PAGE_SIZE = 5;
    private final int ROW_HEIGHT = 25;
    private final int HEADER_HEIGHT = 32;
    private final int OTHER_COMPONENTS_HEIGHT = 300;

    public ProductForm() {
        super("Lista produktów");
        initializeComponents();
        setupLayout();
        setupListeners();
        calculateDynamicPageSize();
        loadInitialData();
        // Ustawienie ikony aplikacji
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());

    }

    private void initializeComponents() {
        // Główny panel
        JPanel1 = new JPanel(new BorderLayout(10, 10));
        JPanel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel1.setBackground(new Color(245, 245, 245));

        // Tabela produktów
        productTable = new JTable();
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nazwa", "Cena", "Ilość", "Kategoria", "Typ", "Waga", "Gwarancja", "Materiał"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable.setModel(model);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.setFillsViewportHeight(true);
        productTable.setRowHeight(ROW_HEIGHT);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Nagłówek tabeli
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(new Color(44, 62, 80));
        productTable.getTableHeader().setForeground(Color.WHITE);

        // Inne komponenty
        sort = new JComboBox<>(new String[]{"ID (rosnąco)", "ID (malejąco)", "Nazwa (A-Z)", "Nazwa (Z-A)",
                "Cena (rosnąco)", "Cena (malejąco)", "Ilość (rosnąco)", "Ilość (malejąco)", "Kategoria", "Typ"});
        filter = new JComboBox<>(new String[]{"Wszystkie", "Elektronika", "Odzież", "Sprzęt", "Materiały", "Niski stan (<10)", "Brak w magazynie"});
        search = new JTextField(20);
        searchButton = new JButton("Szukaj");
        prevButton = new JButton("Poprzednia");
        nextButton = new JButton("Następna");
        pageLabel = new JLabel("Strona: 1/1", SwingConstants.CENTER);
        shoppingButton = new JButton("Zakupy");
        closeButton = new JButton("MENU");
        refreshButton = new JButton("Odśwież");

        // Stylizacja przycisków
        styleButton(searchButton, new Color(52, 152, 219));
        styleButton(prevButton, new Color(149, 165, 166));
        styleButton(nextButton, new Color(149, 165, 166));
        styleButton(shoppingButton, new Color(46, 204, 113));
        styleButton(closeButton, new Color(231, 76, 60));
        styleButton(refreshButton, new Color(241, 196, 15));
    }

    private void setupLayout() {
        // Panel nagłówka
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("LISTA PRODUKTÓW", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel1.add(headerPanel, BorderLayout.NORTH);

        // Panel kontrolny
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        controlPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sortowanie
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Sortuj według:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        controlPanel.add(sort, gbc);

        // Filtrowanie
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Filtruj według:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        controlPanel.add(filter, gbc);

        // Wyszukiwanie
        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Szukaj:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        controlPanel.add(search, gbc);
        gbc.gridx = 2; gbc.gridy = 2;
        controlPanel.add(searchButton, gbc);


        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel dolny z paginacją i przyciskami
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.setBackground(new Color(245, 245, 245));

        // Paginacja
        JPanel paginationPanel = new JPanel();
        paginationPanel.setBackground(new Color(245, 245, 245));
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);
        bottomPanel.add(paginationPanel, BorderLayout.CENTER);

        // Przyciski akcji
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(new Color(245, 245, 245));
        actionPanel.add(shoppingButton);
        actionPanel.add(closeButton);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        // Główny układ - teraz z odpowiednimi wagami
        JPanel1.add(controlPanel, BorderLayout.NORTH); // Panel kontrolny na górze
        JPanel1.add(tablePanel, BorderLayout.CENTER);  // Tabela w centrum (zajmie dostępne miejsce)
        JPanel1.add(bottomPanel, BorderLayout.SOUTH);  // Panel dolny na dole

        // Ustawienia głównego okna
        this.setContentPane(JPanel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
    }
    private void setupListeners() {
        sort.addActionListener(e -> sortAndFilter());
        filter.addActionListener(e -> sortAndFilter());
        searchButton.addActionListener(e -> searchProducts());
        prevButton.addActionListener(e -> navigatePage(-1));
        nextButton.addActionListener(e -> navigatePage(1));
        refreshButton.addActionListener(e -> refreshWindow());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                refreshWindow();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainForm();
            }
        });

        shoppingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null,
                        "Czy jesteś klientem hurtowym?",
                        "Potwierdzenie",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null,
                            "Proszę się zalogować, aby kontynuować.",
                            "Logowanie",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainForm();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Wybrano opcję klienta detalicznego",
                            "Informacja",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new ShopRetailForm();
                }
            }
        });
    }

    private void loadInitialData() {
        loadProducts(1, "id", "asc", "Wszystkie");
        this.setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void navigatePage(int direction) {
        currentPage += direction;
        sortAndFilter();
    }

    private void searchProducts() {
        String searchTerm = search.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                ProductsDAO dao = new ProductsDAO();
                String[] sortParams = getColumnAndOrderForSorting();
                String column = sortParams[0];
                String order = sortParams[1];
                String filterType = filter.getSelectedItem().toString();

                List<Product> products = dao.szukanyProdukt(searchTerm, column, order, filterType);
                displayProducts(products);

                currentPage = 1;
                pageLabel.setText("Wyniki wyszukiwania");
                prevButton.setEnabled(false);
                nextButton.setEnabled(false);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas wyszukiwania produktu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Zawartość pola 'Szukaj:' jest pusta", "PUSTE POLE", JOptionPane.WARNING_MESSAGE);
            String[] sortParams = getColumnAndOrderForSorting();
            loadProducts(1, sortParams[0], sortParams[1], "Wszystkie");
        }
    }

    private String[] getColumnAndOrderForSorting() {
        String selected = sort.getSelectedItem().toString();
        if (selected.startsWith("ID")) {
            return new String[]{"id", selected.contains("rosnąco") ? "asc" : "desc"};
        } else if (selected.startsWith("Nazwa")) {
            return new String[]{"nazwa", selected.contains("A-Z") ? "asc" : "desc"};
        } else if (selected.startsWith("Cena")) {
            return new String[]{"cena", selected.contains("rosnąco") ? "asc" : "desc"};
        } else if (selected.startsWith("Ilość")) {
            return new String[]{"ilosc", selected.contains("rosnąco") ? "asc" : "desc"};
        } else if (selected.startsWith("Kategoria")) {
            return new String[]{"kategoria", "asc"};
        } else if (selected.startsWith("Typ")) {
            return new String[]{"typ", "asc"};
        }
        return new String[]{"id", "asc"};
    }

    private void displayProducts(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        for(Product product : products) {
            if (product == null) continue;  // Skip null products

            Object[] rowData = new Object[]{
                    product.getId(),
                    product.getNazwa(),
                    String.format("%.2f zł", product.getCena()),
                    product.getIlosc(),
                    product.getKategoria(),
                    product.getTyp(),
                    null, // Waga - zostanie uzupełnione poniżej
                    null, // Gwarancja - zostanie uzupełnione poniżej
                    null  // Materiał - zostanie uzupełnione poniżej
            };

            if(product instanceof ProductClothes) {
                ProductClothes clothes = (ProductClothes) product;
                rowData[6] = "-";
                rowData[7] = "-";
                rowData[8] = clothes.getMaterial();
            } else if (product instanceof ProductElectric) {
                ProductElectric electric = (ProductElectric) product;
                rowData[6] = electric.getWaga() + " kg";
                rowData[7] = electric.getGwarancja() > 0 ? electric.getGwarancja() + " mies." : "Brak";
                rowData[8] = "-";
            } else if (product instanceof ProductMaterials) {
                ProductMaterials materials = (ProductMaterials) product;
                rowData[6] = materials.getWaga() + " " + materials.getJednostka();
                rowData[7] = "-";
                rowData[8] = "-";
            } else if (product instanceof ProductEquipment) {
                ProductEquipment equipment = (ProductEquipment) product;
                rowData[6] = equipment.getWaga() + " kg";
                rowData[7] = equipment.getGwarancja() > 0 ? equipment.getGwarancja() + " mies." : "Brak";
                rowData[8] = equipment.getMaterial();
            } else {
                // For generic Product objects
                rowData[6] = "-";
                rowData[7] = "-";
                rowData[8] = "-";
            }

            model.addRow(rowData);
        }
    }

    private void sortAndFilter() {
        String[] sortParams = getColumnAndOrderForSorting();
        String column = sortParams[0];
        String order = sortParams[1];
        String filterType = filter.getSelectedItem().toString();

        String filterValue;
        switch (filterType) {
            case "Niski stan (<10)": filterValue = "Niskie stany"; break;
            case "Brak w magazynie": filterValue = "Na wyczerpaniu"; break;
            case "Elektronika": filterValue = "elektronika"; break;
            case "Odzież": filterValue = "odziez"; break;
            case "Sprzęt": filterValue = "sprzet"; break;
            case "Materiały": filterValue = "materialy"; break;
            default: filterValue = "Wszystkie";
        }

        loadProducts(currentPage, column, order, filterValue);
    }

    private void loadProducts(int page, String sortColumn, String sortOrder, String filterType) {
        try {
            ProductsDAO dao = new ProductsDAO();
            List<Product> products;
            boolean usePagination = true;

            switch (filterType) {
                case "elektronika":
                    products = dao.getProductsByType("elektronika", sortColumn, sortOrder);
                    usePagination = false;
                    break;
                case "odziez":
                    products = dao.getProductsByType("odziez", sortColumn, sortOrder);
                    usePagination = false;
                    break;
                case "materialy":
                    products = dao.getProductsByType("materialy", sortColumn, sortOrder);
                    usePagination = false;
                    break;
                case "sprzet":
                    products = dao.getProductsByType("sprzet", sortColumn, sortOrder);
                    usePagination = false;
                    break;
                case "Na wyczerpaniu":
                    products = dao.getOutOfStockProducts(sortColumn, sortOrder);
                    usePagination = false;
                    break;
                case "Niskie stany":
                    products = dao.getLowStockProducts(10, sortColumn, sortOrder);
                    usePagination = false;
                    break;
                default:
                    totalPages = dao.getTotalPages(dynamicPageSize);
                    products = dao.getProductsSorted(page, dynamicPageSize, sortColumn, sortOrder);
                    break;
            }

            displayProducts(products);
            updatePagination(usePagination);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas pobierania produktów.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePagination(boolean enablePagination) {
        if (enablePagination) {
            pageLabel.setText("Strona: " + currentPage + "/" + totalPages);
            prevButton.setEnabled(currentPage > 1);
            nextButton.setEnabled(currentPage < totalPages);
        } else {
            pageLabel.setText("Widok filtrowany");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }

    private void calculateDynamicPageSize() {
        int availableHeight = this.getHeight() - OTHER_COMPONENTS_HEIGHT - HEADER_HEIGHT;
        int rowsThatFit = Math.max(MIN_PAGE_SIZE, (availableHeight - 5) / ROW_HEIGHT);
        dynamicPageSize = rowsThatFit-1;
    }

    private void refreshWindow(){
        calculateDynamicPageSize();
        sortAndFilter();
    }

}