package Model;

import java.sql.Timestamp;

public class Transaction {
    private int transakcja_id;
    private int klient_hurtowy_id;
    private int klient_detaliczny_id;
    private Timestamp data;
    private String typ;
    private double calkowita_kwota;
    private String metoda_platnosci;
    private Timestamp data_utworzenia;
    private String imieKlienta;
    private String nazwiskoKlienta;
    private String emailKlienta;

    public Transaction(int transakcja_id, int klient_hurtowy_id, int klient_detaliczny_id, Timestamp data, String typ, double calkowita_kwota, String metoda_platnosci, Timestamp data_utworzenia) {
        this.transakcja_id = transakcja_id;
        this.klient_hurtowy_id = klient_hurtowy_id;
        this.klient_detaliczny_id = klient_detaliczny_id;
        this.data = data;
        this.typ = typ;
        this.calkowita_kwota = calkowita_kwota;
        this.metoda_platnosci = metoda_platnosci;
        this.data_utworzenia = data_utworzenia;
    }

    public Transaction(int transakcja_id, int klient_hurtowy_id, int klient_detaliczny_id, Timestamp data, String typ, double calkowita_kwota, String metoda_platnosci, Timestamp data_utworzenia, String imieKlienta, String nazwiskoKlienta, String emailKlienta) {
        this.transakcja_id = transakcja_id;
        this.klient_hurtowy_id = klient_hurtowy_id;
        this.klient_detaliczny_id = klient_detaliczny_id;
        this.data = data;
        this.typ = typ;
        this.calkowita_kwota = calkowita_kwota;
        this.metoda_platnosci = metoda_platnosci;
        this.data_utworzenia = data_utworzenia;
        this.imieKlienta = imieKlienta;
        this.nazwiskoKlienta = nazwiskoKlienta;
        this.emailKlienta = emailKlienta;
    }

    public int getTransakcja_id() {
        return transakcja_id;
    }

    public void setTransakcja_id(int transakcja_id) {
        this.transakcja_id = transakcja_id;
    }

    public int getKlient_hurtowy_id() {
        return klient_hurtowy_id;
    }

    public void setKlient_hurtowy_id(int klient_hurtowy_id) {
        this.klient_hurtowy_id = klient_hurtowy_id;
    }

    public int getKlient_detaliczny_id() {
        return klient_detaliczny_id;
    }

    public void setKlient_detaliczny_id(int klient_detaliczny_id) {
        this.klient_detaliczny_id = klient_detaliczny_id;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public double getCalkowita_kwota() {
        return calkowita_kwota;
    }

    public void setCalkowita_kwota(double calkowita_kwota) {
        this.calkowita_kwota = calkowita_kwota;
    }

    public String getMetoda_platnosci() {
        return metoda_platnosci;
    }

    public void setMetoda_platnosci(String metoda_platnosci) {
        this.metoda_platnosci = metoda_platnosci;
    }

    public Timestamp getData_utworzenia() {
        return data_utworzenia;
    }

    public void setData_utworzenia(Timestamp data_utworzenia) {
        this.data_utworzenia = data_utworzenia;
    }

    public String getImieKlienta() {
        return imieKlienta;
    }

    public void setImieKlienta(String imieKlienta) {
        this.imieKlienta = imieKlienta;
    }

    public String getNazwiskoKlienta() {
        return nazwiskoKlienta;
    }

    public void setNazwiskoKlienta(String nazwiskoKlienta) {
        this.nazwiskoKlienta = nazwiskoKlienta;
    }

    public String getEmailKlienta() {
        return emailKlienta;
    }

    public void setEmailKlienta(String emailKlienta) {
        this.emailKlienta = emailKlienta;
    }
}
