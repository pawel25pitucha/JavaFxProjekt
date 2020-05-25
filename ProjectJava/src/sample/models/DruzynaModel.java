package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class DruzynaModel {
    SimpleStringProperty nazwa,liga,dyscyplina;

    public DruzynaModel(String nazwa, String liga, String dyscyplina) {
        this.nazwa=new SimpleStringProperty(nazwa);
        this.liga=new SimpleStringProperty(liga);
        this.dyscyplina=new SimpleStringProperty(dyscyplina);
    }

    public void setNazwa(String nazwa) {
        this.nazwa.set(nazwa);
    }

    public void setLiga(String liga) {
        this.liga.set(liga);
    }

    public void setDyscyplina(String dyscyplina) {
        this.dyscyplina.set(dyscyplina);
    }

    public String getNazwa() {
        return nazwa.get();
    }

    public SimpleStringProperty nazwaProperty() {
        return nazwa;
    }

    public String getLiga() {
        return liga.get();
    }

    public SimpleStringProperty ligaProperty() {
        return liga;
    }

    public String getDyscyplina() {
        return dyscyplina.get();
    }

    public SimpleStringProperty dyscyplinaProperty() {
        return dyscyplina;
    }
}
