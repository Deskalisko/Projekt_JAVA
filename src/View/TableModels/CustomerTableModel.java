package View.TableModels;

import DAO.DatabaseConnection;

import javax.swing.table.AbstractTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Imię", "Nazwisko", "Adres", "Telefon", "Email", "Suma zakupów", "NIP", "Nazwa firmy"};
    private List<Object[]> data = new ArrayList<>();
    private boolean isRetail = true;

    public void setRetail(boolean retail) {
        isRetail = retail;
        loadData();
    }

    public void loadData() {
        data.clear();
        String sql = isRetail ? "SELECT id, imie, nazwisko, adres, telefon, email, suma_zakupow FROM klienci_detaliczni" : "SELECT id, imie, nazwisko, adres, telefon, email, suma_zakupow, NIP, nazwa_firmy FROM klienci_hurtowi";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()){
                if(isRetail){
                    data.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("imie"),
                            rs.getString("nazwisko"),
                            rs.getString("adres"),
                            rs.getString("telefon"),
                            rs.getString("email"),
                            rs.getDouble("suma_zakupow"),
                            null,
                            null
                    });
                }else{
                    data.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("imie"),
                            rs.getString("nazwisko"),
                            rs.getString("adres"),
                            rs.getString("telefon"),
                            rs.getString("email"),
                            rs.getDouble("suma_zakupow"),
                            rs.getString("NIP"),
                            rs.getString("nazwa_firmy")
                    });
                }
            }
            fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getRowCount(){
        return data.size();
    }

    @Override
    public int getColumnCount(){
        return isRetail ? 7 : 9;
    }

    @Override
    public String getColumnName(int columnIndex){
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
}
