public class LecteurFake implements ILecteur{

    private FakeBadge badgeDetect=null;

    public LecteurFake(){

    }
    public void simulerDetecBadge(FakeBadge b ){
        this.badgeDetect=b;
    }
    public void simulerDetecBadge( ){
        this.badgeDetect=new FakeBadge();
    }

    @Override
    public FakeBadge badgeDétécté() {
        var interm= this.badgeDetect;
        this.badgeDetect= new FakeBadge(0);
        return interm;
    }


}
