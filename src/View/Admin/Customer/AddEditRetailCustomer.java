package View.Admin.Customer;

import DAO.DatabaseConnection;
import DAO.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddEditRetailCustomer extends JDialog {
    private JPanel JPanel1;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JLabel titleLabel;
    private JLabel logoLabel;

    private JTextField imieField;
    private JTextField nazwiskoField;
    private JTextField adresField;
    private JTextField telefonField;
    private JTextField emailField;
    private JTextField sumaField;

    private JButton saveButton;
    private JButton cancelButton;

    private Integer customerId;
    private UserDAO userDAO;
    private boolean saved = false;
    private Integer newCustomerId = null;

    public AddEditRetailCustomer(JFrame parent, Integer customerId) {
        super(parent, customerId == null ? "Dodawanie nowego klienta detalicznego" : "Edycja danych klienta detalicznego", true);
        this.customerId = customerId;
        userDAO = new UserDAO();

        initializeComponents();
        setupLayout();

        this.setSize(600, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        // Ustawienie ikony aplikacji
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());

        if (customerId != null) {
            loadCustomerData();
        }
    }

    private void initializeComponents() {
        //panel główny
        JPanel1 = new JPanel(new BorderLayout(10, 10));
        JPanel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel1.setBackground(new Color(245, 245, 245));

        //panel nagłówka
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // tytul
        titleLabel = new JLabel(customerId == null ? "DODAJ KLIENTA DETALICZNEGO" : "EDYTUJ KLIENTA DETALICZNEGO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        //panel formularza
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // pola formularza
        imieField = createFormField("Imię:", gbc, 0);
        nazwiskoField = createFormField("Nazwisko:", gbc, 1);
        adresField = createFormField("Adres:", gbc, 2);
        telefonField = createFormField("Telefon:", gbc, 3);
        emailField = createFormField("Email:", gbc, 4);
        sumaField = createFormField("Suma zakupów:", gbc, 5);
        sumaField.setEditable(false);

        //pabel przycisków
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        buttonPanel.setOpaque(false);

        saveButton = createStyledButton("Zapisz", new Color(46, 204, 113));
        saveButton.addActionListener(e -> saveCustomer());

        cancelButton = createStyledButton("Anuluj", new Color(231, 76, 60));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
    }

    private JTextField createFormField(String label, GridBagConstraints gbc, int row) { //tworzenie pól formularza
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 30));
        formPanel.add(field, gbc);

        gbc.weightx = 0;
        return field;
    }

    private JButton createStyledButton(String text, Color color) {//stylowanie przycisków
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupLayout() { // ustawianie układu okna
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);

        this.setContentPane(JPanel1);
    }

    private void loadCustomerData() {
        String sql = "SELECT * FROM klienci_detaliczni WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imieField.setText(rs.getString("imie"));
                nazwiskoField.setText(rs.getString("nazwisko"));
                adresField.setText(rs.getString("adres"));
                telefonField.setText(rs.getString("telefon"));
                emailField.setText(rs.getString("email"));
                sumaField.setText(String.format("%.2f", rs.getDouble("suma_zakupow")));
                sumaField.setEditable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveCustomer() { //zapisywanie danych klienta
        if (!validateFields()) {
            return;
        }

        String email = emailField.getText().trim();// trim - usuwa białe znaki na początku i końcu tekstu, getText - pobranie wartości z pola tekstowego

        // Najpierw sprawdź, czy email już istnieje w bazie (tylko dla nowego klienta)
        if (customerId == null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkSql = "SELECT * FROM klienci_detaliczni WHERE email = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, email);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        // Email już istnieje - uzupełnij dane i wyświetl komunikat
                        imieField.setText(rs.getString("imie"));
                        nazwiskoField.setText(rs.getString("nazwisko"));
                        adresField.setText(rs.getString("adres"));
                        telefonField.setText(rs.getString("telefon"));
                        sumaField.setText(String.format("%.2f", rs.getDouble("suma_zakupow")));

                        this.customerId = rs.getInt("id");
                        this.newCustomerId = rs.getInt("id");

                        JOptionPane.showMessageDialog(this,
                                "Klient o podanym adresie email już istnieje w bazie.\nPobrano dane klienta.\nJeśli chcesz zmienić dane klienta, kliknij \"Edytuj dane\".",
                                "Informacja",
                                JOptionPane.INFORMATION_MESSAGE);
                        this.saved = true;
                        dispose();
                        return;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Błąd podczas sprawdzania danych klienta: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Jeśli email nie istnieje lub edytujemy istniejącego klienta, kontynuuj normalne zapisywanie
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (customerId == null) {
                // Dodawanie nowego klienta - bez tworzenia użytkownika
                String sql = "INSERT INTO klienci_detaliczni (imie, nazwisko, adres, telefon, email) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, imieField.getText());
                    stmt.setString(2, nazwiskoField.getText());
                    stmt.setString(3, adresField.getText());
                    stmt.setString(4, telefonField.getText());
                    stmt.setString(5, email);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Klient został dodany", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    this.saved = true;
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            this.newCustomerId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Creating customer failed, no ID obtained.");
                        }
                    }
                    dispose();
                }
            } else {
                // Edycja istniejącego klienta
                String sql = "UPDATE klienci_detaliczni SET imie = ?, nazwisko = ?, adres = ?, telefon = ?, email = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, imieField.getText());
                    stmt.setString(2, nazwiskoField.getText());
                    stmt.setString(3, adresField.getText());
                    stmt.setString(4, telefonField.getText());
                    stmt.setString(5, email);
                    stmt.setInt(6, customerId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Dane klienta zostały zaktualizowane", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    this.saved = true;
                    this.newCustomerId = customerId;
                    dispose();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania klienta: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        String imie = imieField.getText().trim();
        String nazwisko = nazwiskoField.getText().trim();
        String adres = adresField.getText().trim();
        String telefon = telefonField.getText().trim();
        String email = emailField.getText().trim();

        if (imie.isEmpty() || nazwisko.isEmpty() || adres.isEmpty() || telefon.isEmpty() || email.isEmpty()) {
            errors.append("Wszystkie pola muszą być wypełnione!\n");
        }

        if (!telefon.matches("\\d{9}")) {
            errors.append("Numer telefonu musi zawierać dokładnie 9 cyfr!\n");
        }

        if (!email.contains("@")) {
            errors.append("Adres e-mail musi zawierać znak '@'!\n");
        }

        if (imie.length() < 1 || nazwisko.length() < 1) {
            errors.append("Imię i nazwisko muszą zawierać przynajmniej 1 znak!\n");
        }

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean wasSaved() {
        return saved;
    }

    public Integer getCustomerId() {
        return newCustomerId;
    }
}