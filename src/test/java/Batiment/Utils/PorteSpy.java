package Batiment.Utils;

import Batiment.IPorte;

public class PorteSpy implements IPorte {

    //private boolean bloque=false;
    private boolean demandé= false;
    private int nbSignals=0;
    private boolean defaillante=false;
    
    @Override
    public boolean ouvrir() {
        if (defaillante){
            return this.demandé=false;
        }
        this.nbSignals++;
        return this.demandé=true;
    }// faudra penser à comment refermer la porte une fois ouverte
    

    public boolean ouvertureDemande(){
        return this.demandé;
    }

    public int getNbSignals() {
        return nbSignals;
    }

    public boolean porteDefaillante() {
        return this.defaillante=true;
    }




}
