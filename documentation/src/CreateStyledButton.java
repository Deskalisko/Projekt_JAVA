private JButton createStyledButton(String text, String iconPath) {
    ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
    Image scaledIcon = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    JButton button = new JButton(text, new ImageIcon(scaledIcon));

    button.setFont(new Font("Segoe UI", Font.BOLD, 16));
    button.setForeground(new Color(44, 62, 80));
    button.setBackground(new Color(236, 240, 241));
    button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
    button.setFocusPainted(false); 
    return button;
}