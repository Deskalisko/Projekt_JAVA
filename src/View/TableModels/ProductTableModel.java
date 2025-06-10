package View.TableModels;

import Model.Products.Product;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    private List<Product> products;
    private final String[] columnNames = {"ID", "NAZWA", "CENA", "ILOŚĆ", "KATEGORIA", "TYP"};

    // Domyślny konstruktor
    public ProductTableModel() {
        this.products = new ArrayList<>();
    }

    // Konstruktor z listą produktów
    public ProductTableModel(List<Product> products) {
        this.products = new ArrayList<>(products);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0: return product.getId();
            case 1: return product.getNazwa();
            case 2: return String.format("%.2f zł", product.getCena()); // Formatowanie ceny
            case 3: return product.getIlosc();
            case 4: return product.getKategoria();
            case 5: return product.getTyp();
            default: return null;
        }
    }

    public Product getProductAt(int rowIndex) {
        return products.get(rowIndex);
    }

    public void setProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        fireTableDataChanged();
    }
}
