package View;

import DAO.UserDAO;
import View.Admin.AdminForm;
import View.Shopping.ShopRetailForm;
import View.Shopping.ShopWholeSaleForm;
import View.Product.ProductForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Date;

public class MainForm extends JFrame {
    private JPanel JPanel1;
    private JLabel titleLabel;
    private JButton productsButton;
    private JButton buyButton;
    private JButton loginButton;
    private JButton exitButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel forgotPasswordLabel;

    UserDAO dao = new UserDAO();

    public MainForm() {
        super("Centrum Budowlane"); //tytul okna
        this.setContentPane(createJPanel1()); // ustawia zawartość glownego okna
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //akcja przy zamknieciu okna
        this.pack();// dopasowanie rozmiaru okna do zawartosci
        this.setVisible(true);//widocznosc okna
        this.setLocationRelativeTo(null); // centrowanie okna na ekranie

        // Ustawienie ikony aplikacji
        ImageIcon icon = new ImageIcon(getClass().getResource("/figurs/logo.png"));
        setIconImage(icon.getImage());

        // Timer aktualizujący status logowania i czas
        new Timer(1000, e -> {
            Date date = new Date();
            setTitle("Centrum Budowlane - " + date.toString()); // tytul z aktualnym czasem
        }).start();

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//listener dla pola hasla po wciśnięciu enter - logowanie
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick(); // Wywołanie akcji logowania
                }
            }
        });

        // Listener dla przycisku logowania
        loginButton.addActionListener(e -> {
            String username = usernameField.getText(); // pobranie wartości loginu z pola tekstowego
            String password = new String(passwordField.getPassword());
            try {
                if (dao.authenticateUser(username, password, "admin")) {//sprawdza czy jest administrator
                    showStyledMessage("Zalogowano jako Administrator", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    AdminForm adminForm = new AdminForm();  // Najpierw otwórz nowe okno
                    dispose();  // Potem zamknij obecne
                } else if (dao.authenticateUser(username, password, "hurtowy")) { // sprawdzamy czy jest klientem hurtowym
                    int customerId = dao.getUserIdByWholesaleLogin(username);
                    showStyledMessage("Zalogowano jako klient hurtowy", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    ShopWholeSaleForm shopWholeSaleForm = new ShopWholeSaleForm(customerId);  // Najpierw otwórz nowe okno
                    dispose();  // Potem zamknij obecne
                } else {
                    showStyledMessage("Niepoprawne dane logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.out.println("Wystąpił błąd: " + ex.getMessage());
                showStyledMessage("Wystąpił błąd podczas logowania", "Błąd", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Listener dla przycisku zakupów
        buyButton.addActionListener(e -> {
            int option = showStyledConfirmDialog(
                    "Czy jesteś klientem hurtowym?",
                    "Potwierdź",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                showStyledMessage("Proszę się zalogować, aby kontynuować.", "Logowanie", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showStyledMessage("Wybrano opcję klienta detalicznego", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                ShopRetailForm zakupy = new ShopRetailForm();
            }
        });

        // Listener dla przycisku produktów
        productsButton.addActionListener(e -> {
            ProductForm product = new ProductForm();
            dispose();
        });

        // Listener dla przycisku wyjścia
        exitButton.addActionListener(e -> dispose());

        // Listener dla resetowania hasła
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                initiatePasswordReset();
            }
        });
    }

    private void initiatePasswordReset() {//resetowanie hasla
        String username = JOptionPane.showInputDialog(this,
                "<html><b>Wprowadź login:</b></html>",
                "Resetowanie hasła",
                JOptionPane.PLAIN_MESSAGE);

        if (username == null || username.trim().isEmpty()) { // trim usuwa biale znaki przed i po tekscie oraz sprawdzenie czy pole jest puste
            return;
        }

        try {
            String email = dao.getUserEmail(username);// pobranie emailu na podstawie loginu - bo nie zawsze w mojej bazie sie to zgadza(email jest domyslnym loginem przy tworzeniu nowych klientow, ale uzytkownikow dodalem przez phpmyadmin )
            if (email == null) {
                showStyledMessage("Nie znaleziono użytkownika o podanym loginie", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String resetCode = generateRandomCode(); //losowanie kodu resetującego

            //W ulepszonej aplikacji tutaj byłaby wysyłka maila
            System.out.println("Kod resetujący dla " + email + ": " + resetCode);

            String inputCode = JOptionPane.showInputDialog(this,
                    "<html><b>Kod resetujący został wysłany na email:</b><br>" +
                            email + "<br><br>Wprowadź kod:</html>",
                    "Weryfikacja kodu",
                    JOptionPane.PLAIN_MESSAGE);

            if (inputCode == null || !inputCode.equals(resetCode)) {
                showStyledMessage("Nieprawidłowy kod resetujący", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Panel do wprowadzenia nowego hasła
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel newPassLabel = new JLabel("Nowe hasło:");
            newPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            JPasswordField newPassField = new JPasswordField(20);

            JLabel confirmPassLabel = new JLabel("Potwierdź nowe hasło:");
            confirmPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            JPasswordField confirmPassField = new JPasswordField(20);

            panel.add(newPassLabel);
            panel.add(newPassField);
            panel.add(confirmPassLabel);
            panel.add(confirmPassField);

            int result = JOptionPane.showConfirmDialog(
                    this, panel, "Ustaw nowe hasło", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newPassword = new String(newPassField.getPassword());
                String confirmPassword = new String(confirmPassField.getPassword());

                if (!newPassword.equals(confirmPassword)) {
                    showStyledMessage("Hasła nie są identyczne", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (dao.resetUserPassword(username, newPassword)) {
                    showStyledMessage("Hasło zostało zmienione pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showStyledMessage("Błąd podczas zmiany hasła", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            showStyledMessage("Błąd podczas resetowania hasła: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String generateRandomCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }// generowanie losowego kodu resetującego 6 cyfrowego

    private void showStyledMessage(String message, String title, int messageType) {//stylowanie duzej ilosci komunikatow metodą
        JOptionPane.showMessageDialog(
                this,
                "<html><div style='width: 200px;'>" + message + "</div></html>",
                title,
                messageType);
    }

    private int showStyledConfirmDialog(String message, String title, int optionType, int messageType) {//okno wyswietlania potwierdzajacego
        return JOptionPane.showConfirmDialog(
                this,
                "<html><div style='width: 200px;'>" + message + "</div></html>", // html do stylizacji komunikatu
                title,
                optionType, //yes/no option
                messageType);//typ komunikatu
    }

    private JPanel createJPanel1() {//tworzenie glownego panelu
        JPanel1 = new JPanel(new BorderLayout(10, 10)); // ukladanie elementow w panelu przy uzyciu borderlayout
        JPanel1.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); //ustawienia marginesow. Pozwala na lepsze rozmieszczenie elementow wewnatrz panelu.
        JPanel1.setBackground(new Color(245, 245, 245)); // ustawienie koloru tla

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80)); // ustawienie koloru tla naglowka
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); //marginesy

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/figurs/icons8-crane-100.png")); // pobranie ikony logo
        Image scaledLogo = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);//skalowanie ikony do odpowiedniej wielkosci
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST); //dodanie ikony do na glowka po lewnej stronie

        titleLabel = new JLabel("CENTRUM BUDOWLANE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); //ustawienie czcionki: pogrubionej i rozmiar 28
        titleLabel.setForeground(Color.WHITE); // ustawienie koloru tekstu
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel1.add(headerPanel, BorderLayout.NORTH); //dodanie naglowka do panelu głównego

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // ustawienie układu gridlayout z dwoma przyciskami
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); //marginesy
        buttonsPanel.setBackground(new Color(245, 245, 245)); // ustawienie koloru tla przyciskow

        productsButton = createStyledButton("Produkty", "/figurs/icons8-products-50.png");
        buyButton = createStyledButton("Zakupy", "/figurs/icons8-shopping-bag-50.png");

        buttonsPanel.add(productsButton);
        buttonsPanel.add(buyButton);
        JPanel1.add(buttonsPanel, BorderLayout.CENTER);

        JPanel loginPanel = new JPanel(new GridBagLayout()); // ustawienie gridbaglayout(bardziej elastyczny - rozne rozmiary komorek) do umieszczania elementow w panelu
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        loginPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();// Ustawienie ograniczeń dla GridBagLayout
        gbc.insets = new Insets(5, 5, 5, 5); // marginesy między elementami
        gbc.anchor = GridBagConstraints.WEST; // wyrownanie
        gbc.fill = GridBagConstraints.HORIZONTAL; // wypełnienie

        JLabel loginTitle = new JLabel("Logowanie");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;//ustawienie pozycji x  na siatce
        gbc.gridy = 0;// pozycja y
        gbc.gridwidth = 2; // szerokość
        loginPanel.add(loginTitle, gbc);

        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(loginLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Hasło:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        // Dodanie linku do resetowania hasła
        forgotPasswordLabel = new JLabel("<html><u>Zapomniałeś hasła?</u></html>");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(new Color(41, 128, 185));
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2, 5, 5, 5);
        loginPanel.add(forgotPasswordLabel, gbc);

        // Przycisk logowania dodany do panelu logowania
        loginButton = new JButton("Zaloguj");
        loginButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false); // wylazcenie efektu podswietlania
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        loginPanel.add(loginButton, gbc);

        // Panel dolny
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(loginPanel, BorderLayout.NORTH);

        exitButton = new JButton("Wyjdź");
        exitButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(new Color(245, 245, 245));
        exitPanel.add(exitButton);
        bottomPanel.add(exitPanel, BorderLayout.SOUTH);

        JPanel1.add(bottomPanel, BorderLayout.SOUTH);

        return JPanel1;
    }

    private JButton createStyledButton(String text, String iconPath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image scaledIcon = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton button = new JButton(text, new ImageIcon(scaledIcon));//stworzenie przycisku z ikona

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(new Color(44, 62, 80));
        button.setBackground(new Color(236, 240, 241));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        button.setFocusPainted(false); // wyłączenie efektu podświetlania
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // ustalenie pozycji tekstu
        button.setVerticalTextPosition(SwingConstants.CENTER); // ustalenie pozycji ikony
        button.setIconTextGap(15); // odstęp między tekstem a ikoną

        return button;
    }
}