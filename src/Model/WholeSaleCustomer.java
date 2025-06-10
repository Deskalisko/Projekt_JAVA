package Model;

public class WholeSaleCustomer extends Customer {
    private String NIP;
    private String nazwa_firmy;
    private double suma_zakupow;

    public WholeSaleCustomer(int customerID, String imie, String nazwisko, String adres, String telefon, String email, String NIP, String nazwa_firmy, double suma_zakupow) {
        super(customerID, imie, nazwisko, adres, telefon, email);
        this.NIP = NIP;
        this.nazwa_firmy = nazwa_firmy;
        this.suma_zakupow = suma_zakupow;
    }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public String getNazwa_firmy() {
        return nazwa_firmy;
    }

    public void setNazwa_firmy(String nazwa_firmy) {
        this.nazwa_firmy = nazwa_firmy;
    }

    public double getSuma_zakupow() {
        return suma_zakupow;
    }

    public void setSuma_zakupow(double suma_zakupow) {
        this.suma_zakupow = suma_zakupow;
    }
}
