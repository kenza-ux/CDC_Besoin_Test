public class PorteSpy implements IPorte{

    private boolean demandé= false;

    @Override
    public boolean ouvrir() {
        return this.demandé=true;
    }// faudra penser à comment refermer la porte une fois ouverte

    public boolean ouvertureDemande(){
        return this.demandé;
    }


}
