import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

private JDateChooser createDateChooser() {
    JDateChooser dateChooser = new JDateChooser();
    dateChooser.setDateFormatString("yyyy-MM-dd"); 
    dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    dateChooser.setPreferredSize(new Dimension(150, 30));
    dateChooser.setDate(new Date());
    return dateChooser; 
}