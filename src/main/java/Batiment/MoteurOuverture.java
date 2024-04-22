package Batiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoteurOuverture {

    private Map<ILecteur, IPorte> assosciation = new HashMap();
    private List<IPorte> portesOuvertes = new ArrayList<>();


    private List<Badge> badgesBloque= new ArrayList<>();
    //private boolean bloqué = false;
    private int numBadgePasse;

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }


    public void interroger() {

        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {
            var interm=entry.getKey().badgeDétécté();
            if ( interm!= null && !badgesBloque.contains(interm)) {
                this.numBadgePasse=interm.getNumSerie();
                if (!portesOuvertes.contains(entry.getValue()))

                    entry.getValue().ouvrir();
                portesOuvertes.add(entry.getValue());

            }//else ça n'ouvre rien
        }
    }




    public void bloquerBadge(Badge b){
       badgesBloque.add(b);
    }

    public void débloquerBadge(Badge b){
        badgesBloque.remove(b);
    }

    public int getNumBadgePasse() {
        return numBadgePasse;
    }

    public List<Badge> getBadgesBloque() {
        return badgesBloque;
    }
}