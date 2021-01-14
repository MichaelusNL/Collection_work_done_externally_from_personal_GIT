package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class Statistieken {
    private SimpleStringProperty statistiek;
    private SimpleStringProperty waarde;

    public Statistieken(String statistiek, String waarde) {
        this.statistiek = new SimpleStringProperty(statistiek);
        this.waarde = new SimpleStringProperty(waarde);

    }

    public String getStatistiek() {
        return statistiek.get();
    }

    public void setStatistiek(String statistiek) {
        this.statistiek = new SimpleStringProperty(statistiek);
    }

    public String getWaarde() {
        return waarde.get();
    }

    public void setWaarde(String waarde) {
        this.waarde = new SimpleStringProperty(waarde);
    }
}
