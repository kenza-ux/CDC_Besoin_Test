public class PorteSpy implements IPorte{

    private boolean demandé= false;
    private int nbSignals=0;


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

}
