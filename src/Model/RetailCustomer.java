package Model;

public class RetailCustomer extends Customer {
    private double suma_zakupow;

    public RetailCustomer(int customerID, String imie, String nazwisko, String adres, String telefon, String email, double suma_zakupow) {
        super(customerID, imie, nazwisko, adres, telefon, email);
        this.suma_zakupow = suma_zakupow;
    }

    public double getSuma_zakupow() {
        return suma_zakupow;
    }

    public void setSuma_zakupow(double suma_zakupow) {
        this.suma_zakupow = suma_zakupow;
    }
}
