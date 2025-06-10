package DAO;

import Model.Products.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>(); // Lista produktów inicjalizowana jako pusta — przechowuje wyniki zapytania
        String sql = "SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {// Iteracja po każdym zwróconym rekordzie
                Product product = createProductFromResultSet(resultSet); // tworzenie obiektu produktu mapujac pola z resultset
                products.add(product); // Dodajemy produkt do listy
            }
        }
        return products; // Zwracamy wypełnioną listę produktów
    }

    public List<Product> getProductsSorted(int page, int pageSize, String sortColumn, String sortOrder) throws SQLException { // Metoda zwraca produkty z paginacją i sortowaniem
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * pageSize; // Offset do zapytania SQL – liczba rekordów pominięta, by ustalić stronę np. 2 -> pomiń pierwsze pageSize rekordów

        if (!sortColumn.matches("id|nazwa|ilosc|kategoria|typ")) { // Walidacja, czy nazwa kolumny do sortowania jest bezpieczna i dozwolona – zapobiega SQL Injection przez pola sortowania (white-listowanie)
            sortColumn = "p.id";
        }
        //LIMIT steruje ilością rekordów, OFFSET przesunięciem startu
        String sql = "SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "ORDER BY " + sortColumn + " " + sortOrder + " LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, pageSize);
            statement.setInt(2, offset);// ile rekordow pominac

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
            }
        }
        return products;
    }

    public int getTotalPages(int pageSize) throws SQLException {
        String sql = "SELECT COUNT(*) FROM produkty";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt(1); // liczba wszystkich produktow - pobieramy
                return (int) Math.ceil((double) total / pageSize);
            }
        }
        return 1; // Domyślnie zwracamy 1, gdyby nie udało się pobrać danych
    }

    public List<Product> szukanyProdukt(String searchTerm, String sortColumn, String sortOrder, String filterType) throws SQLException {
        List<Product> products = new ArrayList<>();
        //(użycie LIKE i % jako wildcardów)
        if (!sortColumn.matches("id|nazwa|ilosc|kategoria|typ")) {
            sortColumn = "p.id";
        }

        StringBuilder sql = new StringBuilder("SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p ");
        sql.append("JOIN ceny_produkty cp ON p.id = cp.produkt_id ");
        sql.append("WHERE (p.nazwa LIKE ? OR p.kategoria LIKE ?)");
        //dodatkowy filtr po typie produktu w zależności od wyboru użytkownika
        switch (filterType) {
            case "Ogólne":
                sql.append(" AND p.typ = 'ogolny'");
                break;
            case "Elektronika":
                sql.append(" AND p.typ = 'elektronika'");
                break;
            case "Odzież":
                sql.append(" AND p.typ = 'odziez'");
                break;
            case "Materialy":
                sql.append(" AND p.typ = 'materialy'");
                break;
            case "Sprzet":
                sql.append(" AND p.typ = 'sprzet'");
                break;
            case "Na wyczerpaniu":
                sql.append(" AND p.ilosc = 0");
                break;
            case "Niskie stany":
                sql.append(" AND p.ilosc < 10");
                break;
        }

        sql.append(" ORDER BY p.").append(sortColumn).append(" ").append(sortOrder); // // Dodanie sortowania

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
            }
        }
        return products;
    }

    public List<Product> getProductsByType(String type, String sortColumn, String sortOrder) throws SQLException {//pobranie produktów danego typu
        if (!sortColumn.matches("id|nazwa|cena|ilosc|kategoria|typ")) {
            sortColumn = "p.id";
        }

        String sql = "SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE p.typ = ? ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, type);

            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
                return products;
            }
        }
    }

    public List<Product> getOutOfStockProducts(String sortColumn, String sortOrder) throws SQLException { // Pobierz produkty ktorych nie ma na stanie
        if (!sortColumn.matches("id|nazwa|cena|ilosc|kategoria|typ")) {
            sortColumn = "id";
        }

        String sql = "SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE ilosc = 0 ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = createProductFromResultSet(rs);
                products.add(product);
            }
            return products;
        }
    }

    public List<Product> getLowStockProducts(int threshold, String sortColumn, String sortOrder) throws SQLException {//zwraca produkty ktore maja niska ilosc
        if (!sortColumn.matches("id|nazwa|cena|ilosc|kategoria|typ")) {
            sortColumn = "id";
        }

        String sql = "SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE ilosc < ? ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, threshold);

            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
                return products;
            }
        }
    }

    public void updateProductQuantity(int id, int newQuantity) throws SQLException { // Aktualizacja ilości produktu
        String sql = "UPDATE produkty SET ilosc = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, newQuantity);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    public Product getProductById(int id) throws SQLException { // Pobierz produkt o podanym ID
        String sql = "SELECT p.*, cp.cena_detaliczna AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createProductFromResultSet(rs);
                }
            }
        }
        return null;
    }

    private Product createProductFromResultSet(ResultSet rs) throws SQLException {//tworzenie obiektu produktu na podstawie danych z resultset
        String typ = rs.getString("typ");  // Typ produktu - pobieramy i definiujemy jaka podklase utworzyc
        int id = rs.getInt("id");
        String nazwa = rs.getString("nazwa");
        double cena = rs.getDouble("cena");
        int ilosc = rs.getInt("ilosc");
        String kategoria = rs.getString("kategoria");
        // w zaleznosci od typu produktu tworzymy odpowiedni obiekt - specyficzne wlasciwoscie produktu
        switch (typ) {
            case "odziez":
                ProductClothes clothes = loadClothesProduct(id, nazwa, cena, ilosc, kategoria, typ);
                return clothes != null ? clothes : new Product(id, nazwa, cena, ilosc, kategoria, typ);
            case "elektronika":
                ProductElectric electric = loadElectricProduct(id, nazwa, cena, ilosc, kategoria, typ);
                return electric != null ? electric : new Product(id, nazwa, cena, ilosc, kategoria, typ);
            case "materialy":
                ProductMaterials materials = loadMaterialsProduct(id, nazwa, cena, ilosc, kategoria, typ);
                return materials != null ? materials : new Product(id, nazwa, cena, ilosc, kategoria, typ);
            case "sprzet":
                ProductEquipment equipment = loadEquipmentProduct(id, nazwa, cena, ilosc, kategoria, typ);
                return equipment != null ? equipment : new Product(id, nazwa, cena, ilosc, kategoria, typ);
            default:
                return new Product(id, nazwa, cena, ilosc, kategoria, typ);
        }
    }

    private ProductEquipment loadEquipmentProduct(int id, String nazwa, double cena, int ilosc, String kategoria, String typ) throws SQLException { // Wczytaj dane dla sprzętu
        String sql = "SELECT * FROM produkty_sprzet WHERE produkt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductEquipment(id, nazwa, cena, ilosc, kategoria, typ,
                        rs.getString("material"),
                        rs.getInt("gwarancja"),
                        rs.getDouble("waga"));
            }
        }
        return null;
    }

    private ProductMaterials loadMaterialsProduct(int id, String nazwa, double cena, int ilosc, String kategoria, String typ) throws SQLException { // Wczytaj dane dla materiałów
        String sql = "SELECT * FROM produkty_materialy WHERE produkt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductMaterials(id, nazwa, cena, ilosc, kategoria, typ,
                        rs.getDouble("waga"),
                        rs.getString("jednostka"));
            }
        }
        return null;
    }

    private ProductElectric loadElectricProduct(int id, String nazwa, double cena, int ilosc, String kategoria, String typ) throws SQLException { // Wczytaj dane dla elektroniki
        String sql = "SELECT * FROM produkty_elektronika WHERE produkt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductElectric(id, nazwa, cena, ilosc, kategoria, typ,
                        rs.getString("moc"),
                        rs.getString("napiecie"),
                        rs.getInt("gwarancja"),
                        rs.getDouble("waga"));
            }
        }
        return null;
    }

    private ProductClothes loadClothesProduct(int id, String nazwa, double cena, int ilosc,
                                              String kategoria, String typ) throws SQLException { //Wczytaj dane dla odzieży
        String sql = "SELECT * FROM produkty_odziez WHERE produkt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductClothes(id, nazwa, cena, ilosc, kategoria, typ,
                        rs.getString("rozmiar"),
                        rs.getString("kolor"),
                        rs.getString("material"));
            }
        }
        return null;
    }

    public List<Product> getAllProductsForWholesale() throws SQLException { //Pobierz wszystkie produkty dla hurtowni
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, cp.cena_hurtowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = createProductFromResultSet(resultSet);
                products.add(product);
            }
        }
        return products;
    }

    // Metody do pracy z cenami magazynowymi
    public List<Product> getAllWarehouseProducts() throws SQLException { // Pobierz wszystkie produkty dla magazynu
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = createProductFromResultSet(resultSet);
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> getWarehouseProductsSorted(int page, int pageSize, String sortColumn, String sortOrder) throws SQLException { // Pobierz produkty dla magazynu z paginacją i sortowaniem
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        if (!sortColumn.matches("id|nazwa|ilosc|kategoria|typ")) {
            sortColumn = "p.id";
        }

        String sql = "SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "ORDER BY " + sortColumn + " " + sortOrder + " LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, offset);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
            }
        }
        return products;
    }

    public List<Product> searchWarehouseProducts(String searchTerm, String sortColumn, String sortOrder, String filterType) throws SQLException {//Pobierz produkty dla magazynu z wyszukiwaniem
        List<Product> products = new ArrayList<>();

        if (!sortColumn.matches("id|nazwa|ilosc|kategoria|typ")) {
            sortColumn = "p.id";
        }

        StringBuilder sql = new StringBuilder("SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p ");
        sql.append("JOIN ceny_produkty cp ON p.id = cp.produkt_id ");
        sql.append("WHERE (p.nazwa LIKE ? OR p.kategoria LIKE ?)");

        switch (filterType) {
            case "Ogólne": sql.append(" AND p.typ = 'ogolny'"); break;
            case "Elektronika": sql.append(" AND p.typ = 'elektronika'"); break;
            case "Odzież": sql.append(" AND p.typ = 'odziez'"); break;
            case "Materialy": sql.append(" AND p.typ = 'materialy'"); break;
            case "Sprzet": sql.append(" AND p.typ = 'sprzet'"); break;
            case "Na wyczerpaniu": sql.append(" AND p.ilosc = 0"); break;
            case "Niskie stany": sql.append(" AND p.ilosc < 10"); break;
        }

        sql.append(" ORDER BY p.").append(sortColumn).append(" ").append(sortOrder);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
            }
        }
        return products;
    }

    public List<Product> getWarehouseProductsByType(String type, String sortColumn, String sortOrder) throws SQLException {//Pobierz produkty dla magazynu danego typu
        if (!sortColumn.matches("id|nazwa|cena|ilosc|kategoria|typ")) {
            sortColumn = "p.id";
        }

        String sql = "SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE p.typ = ? ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, type);

            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
                return products;
            }
        }
    }

    public List<Product> getWarehouseOutOfStockProducts(String sortColumn, String sortOrder) throws SQLException {//Pobierz produkty dla magazynu ktorych nie ma na stanie
        if (!sortColumn.matches("id|nazwa|cena|ilosc|kategoria|typ")) {
            sortColumn = "id";
        }

        String sql = "SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE ilosc = 0 ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = createProductFromResultSet(rs);
                products.add(product);
            }
            return products;
        }
    }

    public List<Product> getWarehouseLowStockProducts(int threshold, String sortColumn, String sortOrder) throws SQLException {//Pobierz produkty dla magazynu ktorych ilosc jest niska
        if (!sortColumn.matches("id|nazwa|cena|ilosc|kategoria|typ")) {
            sortColumn = "id";
        }

        String sql = "SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE ilosc < ? ORDER BY " + sortColumn + " " + sortOrder;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, threshold); // ustawienie progu ilosci

            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product product = createProductFromResultSet(rs);
                    products.add(product);
                }
                return products;
            }
        }
    }

    public Product getWarehouseProductById(int id) throws SQLException {//Pobierz produkt dla magazynu o podanym ID
        String sql = "SELECT p.*, cp.cena_magazynowa AS cena FROM produkty p " +
                "JOIN ceny_produkty cp ON p.id = cp.produkt_id " +
                "WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createProductFromResultSet(rs);
                }
            }
        }
        return null;
    }

}