package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class SedziaModel {
    SimpleStringProperty imie, nazwisko, pesel;

    public SedziaModel(String imie, String nazwisko, String pesel) {
        this.imie = new SimpleStringProperty(imie);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.pesel = new SimpleStringProperty(pesel);
    }

    public String getImie() {
        return imie.get();
    }


    public String getNazwisko() {
        return nazwisko.get();
    }


    public String getPesel() {
        return pesel.get();
    }

    public void setImie(String imie) {
        this.imie.set(imie);
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko.set(nazwisko);
    }

    public void setPesel(String pesel) {
        this.pesel.set(pesel);
    }
}
