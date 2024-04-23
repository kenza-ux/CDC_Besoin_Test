package Batiment.Utils;

import Batiment.Batiment;
import Batiment.IPorte;

public class PorteSpy implements IPorte {

    private Batiment batiment;
    private boolean demandé= false;
    private int nbSignals=0;


    public PorteSpy(){

    }

    public PorteSpy(Batiment b){
        this.batiment=b;
    }

    @Override
    public boolean ouvrir() {
        this.nbSignals++;
        return this.demandé=true;
    }// faudra penser à comment refermer la porte une fois ouverte
    

    public boolean ouvertureDemande(){
        return this.demandé;
    }

    public int getNbSignals() {
        return nbSignals;
    }
/*
    public boolean porteDefaillante() {
        return this.defaillante=true;
    }
*/



}
