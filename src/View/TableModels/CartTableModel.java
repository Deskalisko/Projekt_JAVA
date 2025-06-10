package View.TableModels;

import Model.Products.Product;
import Model.Cart;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartTableModel extends AbstractTableModel {
    private Cart koszyk;
    private String[] columnNames = {"Produkt", "Ilość", "Cena jednostkowa", "Łączna cena" };

    public CartTableModel(Cart koszyk) {
        this.koszyk = koszyk;
    }

    @Override
    public int getRowCount() {
        return koszyk.getProdukty().size() + 1;
    } // Dodajemy jeden wiersz dla sumy, więc dodajemy 1 do liczby produktów. - zwraca liczbe produktow w koszyku

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }// Zwraca liczbę kolumn tabeli.

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }//Zwraca nazwę kolumny o podanym indeksie.

    @Override
    public Object getValueAt(int row, int col){ // pobranie produktuy na daj pozycji
        // Jeśli to ostatni wiersz (suma)
        if (row == koszyk.getProdukty().size()) {
            switch (col) {
                case 0:
                    return "Łączna suma:";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return String.format("%.2f zł", koszyk.obliczCalkowitaSume());
                default:
                    return null;
            }
        }

        Map.Entry<Product, Integer> entry = koszyk.getProdukty().get(row);
        switch(col){
            case 0:
                return entry.getKey().getNazwa();
            case 1:
                return entry.getValue();
            case 2:
                return String.format("%.2f zł", entry.getKey().getCena());
            case 3:
                return String.format("%.2f zł", entry.getKey().getCena() * entry.getValue());
            default:
                return null;
        }
    }

    public Product getProductAt(int rowIndex) {
        if (koszyk == null || koszyk.getProdukty().isEmpty()) {
            return null;
        }

        // Konwertujemy entrySet na listę, aby móc pobrać element po indeksie
        List<Map.Entry<Product, Integer>> entries = new ArrayList<>(koszyk.getProdukty());

        if (rowIndex >= 0 && rowIndex < entries.size()) {
            return entries.get(rowIndex).getKey();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Sprawdź, czy wiersz jest w zakresie listy produktów i czy kolumna to "Ilość"
        if (row >= koszyk.getProdukty().size() || col != 1) {
            return false;
        }
        Product product = koszyk.getProdukty().get(row).getKey();
        return product.getIlosc() > 0;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row < koszyk.getProdukty().size() && col == 1) {
            try {
                int newValue = Integer.parseInt(value.toString());
                if (newValue > 0) {
                    Product product = koszyk.getProdukty().get(row).getKey();
                    if (newValue <= product.getIlosc()) {
                        koszyk.getProdukty().get(row).setValue(newValue);
                        fireTableCellUpdated(row, col); // Aktualizuj komórkę tabeli. - odswiezanie komorki tabeli po zmianie ilosci produktu
                        fireTableCellUpdated(row, 3);
                        // Aktualizuj również wiersz z sumą
                        fireTableRowsUpdated(koszyk.getProdukty().size(), koszyk.getProdukty().size());// Odświeżenie wiersza z sumą.
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie można dodać więcej niż " + product.getIlosc(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (RuntimeException e) {
                // Ignoruj błędne dane
            }
        }
    }

}
