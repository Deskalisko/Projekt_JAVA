package Model.Products;

public class ProductElectric extends Product{
    private String moc;
    private String napiecie;
    private int gwarancja;
    private double waga;

    public ProductElectric(int id, String nazwa, double cena, int ilosc, String kategoria, String typ, String moc, String napiecie, int gwarancja, double waga) {
        super(id, nazwa, cena, ilosc, kategoria, typ);
        this.moc = moc;
        this.napiecie = napiecie;
        this.gwarancja = gwarancja;
        this.waga = waga;
    }

    public String getMoc() {
        return moc;
    }

    public void setMoc(String moc) {
        this.moc = moc;
    }

    public String getNapiecie() {
        return napiecie;
    }

    public void setNapiecie(String napiecie) {
        this.napiecie = napiecie;
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
