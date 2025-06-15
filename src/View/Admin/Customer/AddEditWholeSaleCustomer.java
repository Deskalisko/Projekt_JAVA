package View.Admin.Customer;

import DAO.DatabaseConnection;
import DAO.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class AddEditWholeSaleCustomer extends JDialog {
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
    private JTextField nipField;
    private JTextField nazwaFirmyField;
    private JTextField sumaField;

    private JButton saveButton;
    private JButton cancelButton;

    private Integer customerId;
    private UserDAO userDAO;
    private boolean saved = false;
    private Integer newCustomerId = null;

    public AddEditWholeSaleCustomer(JFrame parent, Integer customerId) {
        super(parent, customerId == null ? "Dodaj nowego klienta hurtowego" : "Edytuj klienta hurtowego", true);
        this.customerId = customerId;
        userDAO = new UserDAO();

        initializeComponents();
        setupLayout();

        this.setSize(700, 600);
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
        JPanel1 = new JPanel(new BorderLayout(10, 10));
        JPanel1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel1.setBackground(new Color(245, 245, 245));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        titleLabel = new JLabel(customerId == null ? "DODAJ KLIENTA HURTOWEGO" : "EDYTUJ KLIENTA HURTOWEGO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

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

        imieField = createFormField("Imię:", gbc, 0);
        nazwiskoField = createFormField("Nazwisko:", gbc, 1);
        adresField = createFormField("Adres:", gbc, 2);
        telefonField = createFormField("Telefon:", gbc, 3);
        emailField = createFormField("Email:", gbc, 4);
        nipField = createFormField("NIP:", gbc, 5);
        nazwaFirmyField = createFormField("Nazwa firmy:", gbc, 6);
        sumaField = createFormField("Suma zakupów:", gbc, 7);
        sumaField.setEditable(false);

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

    private JTextField createFormField(String label, GridBagConstraints gbc, int row) {
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

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupLayout() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel1.add(headerPanel, BorderLayout.NORTH);
        JPanel1.add(contentPanel, BorderLayout.CENTER);

        this.setContentPane(JPanel1);
    }
    private void loadCustomerData() {
        String sql = "SELECT * FROM klienci_hurtowi WHERE id = ?";
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
                nipField.setText(rs.getString("nip"));
                nazwaFirmyField.setText(rs.getString("nazwa_firmy"));
                // Zmiana: użycie kropki zamiast przecinka
                sumaField.setText(String.format(Locale.US, "%.2f", rs.getDouble("suma_zakupow")));
                sumaField.setEditable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania danych klienta", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCustomer() {
        if (!validateFields()) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (customerId == null) {
                // Dodawanie nowego klienta - najpierw tworzymy użytkownika
                String login = emailField.getText();
                String password = generateDefaultPassword();
                int userId = userDAO.addUser(login, password, "hurtowy");

                if (userId == -1) {
                    JOptionPane.showMessageDialog(this, "Błąd podczas tworzenia użytkownika", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO klienci_hurtowi (imie, nazwisko, adres, telefon, email, NIP, nazwa_firmy, suma_zakupow, uzytkownik_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    setStatementValues(stmt);
                    stmt.setInt(9, userId);
                    stmt.executeUpdate();

                    // tworzenie wiadomości z danymi logowania
                    String successMessage = "<html><b>Klient został dodany pomyślnie!</b><br><br>" +
                            "Dane logowania:<br>" +
                            "Login: " + login + "<br>" +
                            "Hasło: " + password + "<br><br>" +
                            "Prosimy o zmianę hasła przy pierwszym logowaniu.</html>";

                    JOptionPane.showMessageDialog(this, successMessage, "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    this.saved = true;
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            this.newCustomerId = generatedKeys.getInt(1);
                        }
                    }
                    dispose();
                }
            } else {
                // Edycja istniejącego klienta
                String sql = "UPDATE klienci_hurtowi SET imie = ?, nazwisko = ?, adres = ?, telefon = ?, email = ?, NIP = ?, nazwa_firmy = ?, suma_zakupow = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    setStatementValues(stmt);
                    stmt.setInt(9, customerId);
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

    private String generateDefaultPassword() {
        return "pass" + (int)(Math.random() * 10000);
    } // generowanie domyślnego hasła dla nowych klientów

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        String imie = imieField.getText().trim();
        String nazwisko = nazwiskoField.getText().trim();
        String adres = adresField.getText().trim();
        String telefon = telefonField.getText().trim();
        String email = emailField.getText().trim();
        String nip = nipField.getText().trim();
        String nazwaFirmy = nazwaFirmyField.getText().trim();

        if (imie.isEmpty() || nazwisko.isEmpty() || adres.isEmpty() || telefon.isEmpty() ||
                email.isEmpty() || nip.isEmpty() || nazwaFirmy.isEmpty()) {
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

        if (!nip.matches("\\d{10}")) {
            errors.append("NIP musi zawierać dokładnie 10 cyfr!\n");
        }

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void setStatementValues(PreparedStatement stmt) throws SQLException { // metoda pomocnicza do ustawienia wartości parametrów w instrukcji SQL
        stmt.setString(1, imieField.getText());
        stmt.setString(2, nazwiskoField.getText());
        stmt.setString(3, adresField.getText());
        stmt.setString(4, telefonField.getText());
        stmt.setString(5, emailField.getText());
        stmt.setString(6, nipField.getText());
        stmt.setString(7, nazwaFirmyField.getText());

        String sumaText = sumaField.getText().replace(',', '.');//replace - przecinek na kropkę
        if (sumaText.isEmpty()) {//zabezpieczenie przed wprowadzeniem pustej sumy zakupów
            // pustych danych nie można zapisać do bazy danych, więc ustalamy wartość domyślną
            stmt.setDouble(8, 0.0);
        } else {
            stmt.setDouble(8, Double.parseDouble(sumaText)); //parsujemy jesli wartość jest pobrana jako tekst
        }
    }

}