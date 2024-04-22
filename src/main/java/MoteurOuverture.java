public class MoteurOuverture {
    private IPorte porte;
    private ILecteur lecteur;

    public MoteurOuverture(){

    }

    public void associer(IPorte p, ILecteur l){
        this.porte= p;
        this.lecteur=l;
    }

    public void interroger(){
        if (this.lecteur.badgeDétécté()){
            this.porte.ouvrir();
        }
    }
}
