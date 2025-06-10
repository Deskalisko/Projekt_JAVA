package Model.Products;

public class ProductClothes extends Product{
    private String rozmiar;
    private String kolor;
    private String material;

    public ProductClothes(int id, String nazwa, double cena, int ilosc, String kategoria, String typ, String rozmiar, String kolor, String material) {
        super(id, nazwa, cena, ilosc, kategoria, typ);
        this.rozmiar = rozmiar;
        this.kolor = kolor;
        this.material = material;
    }

    public String getRozmiar() {
        return rozmiar;
    }

    public void setRozmiar(String rozmiar) {
        this.rozmiar = rozmiar;
    }

    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
