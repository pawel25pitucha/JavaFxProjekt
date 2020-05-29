package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class PlayerModel {
   SimpleStringProperty imie,nazwisko,pesel,poziom;

    public PlayerModel(String imie, String nazwisko, String pesel, String poziom) {
        this.imie=new SimpleStringProperty(imie);
        this.nazwisko=new SimpleStringProperty(nazwisko);
        this.pesel=new SimpleStringProperty(pesel);
        this.poziom=new SimpleStringProperty(poziom);
    }

    public String getImie() {
        return imie.get();
    }

    public SimpleStringProperty imieProperty() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public SimpleStringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public String getPesel() {
        return pesel.get();
    }

    public SimpleStringProperty peselProperty() {
        return pesel;
    }

    public String getPoziom() {
        return poziom.get();
    }

    public SimpleStringProperty poziomProperty() {
        return poziom;
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

    public void setPoziom(String poziom) {
        this.poziom.set(poziom);
    }
}
