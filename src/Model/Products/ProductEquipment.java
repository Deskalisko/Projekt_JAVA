package Model.Products;

public class ProductEquipment extends Product{
    private String material;
    private int gwarancja;
    private double waga;

    public ProductEquipment(int id, String nazwa, double cena, int ilosc, String kategoria, String typ, String material, int gwarancja, double waga) {
        super(id, nazwa, cena, ilosc, kategoria, typ);
        this.material = material;
        this.gwarancja = gwarancja;
        this.waga = waga;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getGwarancja() {
        return gwarancja;
    }

    public void setGwarancja(int gwarancja) {
        this.gwarancja = gwarancja;
    }

    public double getWaga() {
        return waga;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }
}
