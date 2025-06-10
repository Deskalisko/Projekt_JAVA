package Model;

import Model.Products.Product;

import java.util.*;

public class Cart {
    //lista przechowujaca produkty i ich ilosc w koszyku
    //uzywany arraylist , elementy sa parami klucz-wartosc, klucz to produkt, wartosc to ilosc w koszyku
    private ArrayList<Map.Entry<Product, Integer>> produkty;
    public Cart(){
        produkty=new ArrayList<>();
    }

    public ArrayList<Map.Entry<Product,Integer>> getProdukty() {// zwrocenie listy z produktami aktualnego klienta
        return produkty;

    }

    public void dodajProdukt(Product product, int ilosc) { // dodaje produkt do koszyka lub aktualizuje jesli jest juz w koszyku
        for(Map.Entry<Product, Integer>entry : produkty){ // sprawdzam czy dany produkt jest juz w koszyku
            if(entry.getKey().getId()==product.getId()){//jesli tak to aktualizuje jego ilosc
                entry.setValue(entry.getValue() + ilosc);
                return;
            }
        }
        produkty.add(new AbstractMap.SimpleEntry<>(product, ilosc)); // jesli nie to go dodaje, abstractmap.simpleentry tworzy pary klucz-wartosc
    }

    public void usunProdukt(Product product) {// usuwanie produktu z koszyka
        produkty.removeIf(entry ->entry.getKey().getId() == product.getId()); // usuwa wszystkie wystapienia danego produktu
    }

    public void aktualizujIlosc(Product product, int nowaIlosc) { // aktualizacja ilosci produktu w koszyku
        for (Map.Entry<Product, Integer> entry : produkty) {
            if (entry.getKey().getId() == product.getId()) {
                entry.setValue(nowaIlosc); //nowa ilosc produktu
                return;
            }
        }
    }

    public double obliczCalkowitaSume(){//stream api - mapowanie kazdego wpisu na jego wartosc * ilosc oraz sumowanie wynikow
        return produkty.stream().mapToDouble(entry->entry.getKey().getCena()*entry.getValue()).sum();
    }


}
