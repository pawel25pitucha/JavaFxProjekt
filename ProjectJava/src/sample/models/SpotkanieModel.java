package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class SpotkanieModel {
    SimpleStringProperty gospodarzId,goscId,sedziaId,gospodarzPunkty,goscPunkty,cena,data,nazwaGospodarz,nazwaGosc;

    public SpotkanieModel(String gospodarzId, String goscId, String sedziaId, String gospodarzPunkty, String goscPunkty, String cena, String data,String nazwaGospodarz,String nazwaGosc){
        this.gospodarzId = new SimpleStringProperty(gospodarzId);
        this.goscId = new SimpleStringProperty(goscId);
        this.sedziaId = new SimpleStringProperty(sedziaId);
        this.gospodarzPunkty = new SimpleStringProperty(gospodarzPunkty);
        this.goscPunkty = new SimpleStringProperty(goscPunkty);
        this.cena = new SimpleStringProperty(cena);
        this.data = new SimpleStringProperty(data);
        this.nazwaGospodarz=new SimpleStringProperty(nazwaGospodarz);
        this.nazwaGosc=new SimpleStringProperty(nazwaGosc);
    }

    public String getNazwaGospodarz() {
        return nazwaGospodarz.get();
    }

    public SimpleStringProperty nazwaGospodarzProperty() {
        return nazwaGospodarz;
    }

    public String getNazwaGosc() {
        return nazwaGosc.get();
    }

    public SimpleStringProperty nazwaGoscProperty() {
        return nazwaGosc;
    }

    public String getGospodarzId() {
        return gospodarzId.get();
    }

    public SimpleStringProperty gospodarzIdProperty() {
        return gospodarzId;
    }

    public String getGoscId() {
        return goscId.get();
    }

    public SimpleStringProperty goscIdProperty() {
        return goscId;
    }

    public String getSedziaId() {
        return sedziaId.get();
    }

    public SimpleStringProperty sedziaIdProperty() {
        return sedziaId;
    }

    public String getGospodarzPunkty() {
        return gospodarzPunkty.get();
    }

    public SimpleStringProperty gospodarzPunktyProperty() {
        return gospodarzPunkty;
    }

    public String getGoscPunkty() {
        return goscPunkty.get();
    }

    public SimpleStringProperty goscPunktyProperty() {
        return goscPunkty;
    }

    public String getCena() {
        return cena.get();
    }

    public SimpleStringProperty cenaProperty() {
        return cena;
    }

    public String getData() {
        return data.get();
    }

    public SimpleStringProperty dataProperty() {
        return data;
    }

}
