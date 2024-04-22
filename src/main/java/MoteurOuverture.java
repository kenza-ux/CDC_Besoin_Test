import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoteurOuverture {

    private Map<ILecteur, IPorte> assosciation = new HashMap();
    private List<IPorte> portesOuvertes= new ArrayList<>();
    private IBadge badge;

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }

    public void interroger() {
        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {
            if (entry.getKey().badgeDétécté()) {
                if(!portesOuvertes.contains(entry.getValue())&& !badge.estBloque() )
                    //on rajoute la condition de badge s'il n'est pas bloqué
                    entry.getValue().ouvrir();
                    portesOuvertes.add(entry.getValue());

            }//else ça n'ouvre rien
        }
    }
    public IBadge getBadge() {
        return badge;
    }

    public void setBadge(IBadge badge) {
        this.badge = badge;
    }




}