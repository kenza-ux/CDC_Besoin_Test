public class LecteurFake implements ILecteur{
    private IPorte porte;
    private boolean badgeDetect=false;

    public LecteurFake(){

    }

    public void simulerDetecBadge(){
        this.badgeDetect=true;
    }

    @Override
    public boolean badgeDétécté() {
        boolean interm= this.badgeDetect;
        this.badgeDetect=false;
        return interm;
    }

    public void setPorte(IPorte nouvelle){
        this.porte=nouvelle;
    }
}
