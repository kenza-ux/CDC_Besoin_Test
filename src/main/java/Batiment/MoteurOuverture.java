package Batiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoteurOuverture {

    private Map<ILecteur, IPorte> assosciation = new HashMap();
    private List<IPorte> portesOuvertes = new ArrayList<>();
    //private Map<FakeBadge, Boolean> etats_badge;
    private boolean bloqué = false;
    private Badge badge;

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }

    public void interroger() {

        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {
            badge = entry.getKey().badgeDétécté();
            if (badge != null && !bloqué) {

                if (!portesOuvertes.contains(entry.getValue()))

                    entry.getValue().ouvrir();
                portesOuvertes.add(entry.getValue());

            }//else ça n'ouvre rien
        }
    }

    public Badge getBadge() {
        return badge;
    }


    public boolean bloquerBadge(Badge b){
       return this.bloqué=true;
    }

    public boolean débloquerBadge(Badge b){
        return this.bloqué=false;
    }



}