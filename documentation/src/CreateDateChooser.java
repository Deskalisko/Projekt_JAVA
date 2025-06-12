import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

private JDateChooser createDateChooser() {
    JDateChooser dateChooser = new JDateChooser(); [cite: 1683]
    dateChooser.setDateFormatString("yyyy-MM-dd"); [cite: 1684]
    dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14)); [cite: 1684]
    dateChooser.setPreferredSize(new Dimension(150, 30)); [cite: 1685]
    dateChooser.setDate(new Date()); // Ustawienie aktualnej daty jako domyślnej 
    return dateChooser; [cite: 1685]
}