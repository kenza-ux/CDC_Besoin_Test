package Batiment;

import java.util.ArrayList;
import java.util.List;

public class Batiment {

    private int idBat;
    public List<IPorte> portes= new ArrayList<>();

    public Batiment(){

    }
    public List<IPorte> getPortes(){
        return this.portes;
    }
    public Batiment(int idBat, List<IPorte> portes) {
        this.idBat = idBat;
        this.portes=portes;
    }


    public int getIdBat() {
        return idBat;
    }

    public void setIdBat(int idBat) {
        this.idBat = idBat;
    }

    public void ajouterPorte(IPorte p){
        this.portes.add(p);
    }


    @Override
    public String toString() {
        return "Batiment{" +
                "idBat=" + idBat +
                ", portes=" + portes +
                '}';
    }
}
