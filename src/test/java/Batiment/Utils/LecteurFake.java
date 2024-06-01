package Batiment.Utils;

import Batiment.Badge;
import Batiment.ILecteur;

public class LecteurFake implements ILecteur {

    private Badge badgeDetect=null;

    public LecteurFake(){

    }
    public void simulerDetectionBadge(Badge b ){
        this.badgeDetect=b;
    }
    public void simulerDetectionBadge( ){
        this.badgeDetect=new Badge();
    }

    @Override
    public Badge badgeDétécté() {
        var interm= this.badgeDetect;
        //this.badgeDetect= new Badge(0);
        return interm;
    }


}
