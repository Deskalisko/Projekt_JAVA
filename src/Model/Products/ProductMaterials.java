package Model.Products;

public class ProductMaterials extends Product{
    private double waga;
    private String jednostka;

    public ProductMaterials(int id, String nazwa, double cena, int ilosc, String kategoria, String typ, double waga, String jednostka) {
        super(id, nazwa, cena, ilosc, kategoria, typ);
        this.waga = waga;
        this.jednostka = jednostka;
    }

    public double getWaga() {
        return waga;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }

    public String getJednostka() {
        return jednostka;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }
}
