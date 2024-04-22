package Batiment;

public class Badge {

    private Porteur personne;
    private int numSerie=0;

    public Badge(int valueNum){
        this.numSerie=valueNum;
    }
    public Badge() {

    }

    public int getNumSerie(){
        return this.numSerie;
    }

    public Porteur getPersonne() {
        return personne;
    }
    public void setPersonne(Porteur personne) {
        this.personne = personne;
    }


}
