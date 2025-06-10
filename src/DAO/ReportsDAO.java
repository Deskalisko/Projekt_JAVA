package DAO;

import Model.Products.Product;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportsDAO {
    private ProductsDAO productsDAO;

    public ReportsDAO() {
        this.productsDAO = new ProductsDAO();
    }

    public void generateLowStockReport(int threshold, String filePath) throws SQLException, IOException { // generowanie raportu o produkcie z niskim stanem magazynowym
        List<Product> lowStockProducts = productsDAO.getLowStockProducts(threshold, "ilosc", "ASC"); // pobranie ponizej progu threshold

        try (FileWriter writer = new FileWriter(filePath)) { //filewriter do zapisu pliku
            writer.write("RAPORT NISKICH STANOW MAGAZYNOWYCH\n");
            writer.write("Wygenerowano: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("Liczba produktow z niskim stanem: " + lowStockProducts.size() + "\n\n");

            // Naglowki kolumn
            writer.write(String.format("%-8s | %-30s | %-10s | %-8s | %-15s | %-15s\n",
                    "ID", "Nazwa produktu", "Cena", "Ilosc", "Kategoria", "Typ"));
            writer.write("--------|--------------------------------|------------|----------|-----------------|----------------\n");

            // Dane produktow
            for (Product p : lowStockProducts) {
                String nazwa = p.getNazwa()
                        .replace("ą", "a").replace("ć", "c").replace("ę", "e")
                        .replace("ł", "l").replace("ń", "n").replace("ó", "o")
                        .replace("ś", "s").replace("ź", "z").replace("ż", "z");

                String kategoria = p.getKategoria()
                        .replace("ą", "a").replace("ć", "c").replace("ę", "e")
                        .replace("ł", "l").replace("ń", "n").replace("ó", "o")
                        .replace("ś", "s").replace("ź", "z").replace("ż", "z");

                String typ = p.getTyp()
                        .replace("ą", "a").replace("ć", "c").replace("ę", "e")
                        .replace("ł", "l").replace("ń", "n").replace("ó", "o")
                        .replace("ś", "s").replace("ź", "z").replace("ż", "z");

                writer.write(String.format("%-8d | %-30s | %-10.2f | %-8d | %-15s | %-15s\n",
                        p.getId(),
                        nazwa,
                        p.getCena(),
                        p.getIlosc(),
                        kategoria,
                        typ));
            }

            // Podsumowanie
            writer.write("\n");
            writer.write("UWAGA: Produkty z iloscia mniejsza niz " + threshold + " sa uwazane za niskie stany magazynowe.\n");
            writer.write("Zalecane jest uzupelnienie zapasow w najblizszym mozliwym terminie.\n");
        }
    }
}