import java.util.HashMap;
import java.util.Map;

public class MoteurOuverture {

    private Map<ILecteur, IPorte> assosciation = new HashMap();
    //private ;

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }

    public void interroger() {
        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {
            if (entry.getKey().badgeDétécté()) {
                entry.getValue().ouvrir();
            }
        }
    }
}