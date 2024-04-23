package Batiment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MoteurOuverture {

    private Map<ILecteur, IPorte> assosciation = new HashMap();
    private List<IPorte> portesOuvertes = new ArrayList<>();
    private List<Badge> badgesBloque= new ArrayList<>();
    private boolean accesAutoris=true;
    private int numBadgePasse;
    private Map<Batiment, LocalDate> assoBlocage_Bat_Date= new HashMap();
    private List<Batiment> batimentsBloque= new ArrayList<>();
   // private Map<Porteur, Badge> assoPort_badge= new HashMap<>();

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }

    public void accesNonAutoriseDurant(Batiment bat, LocalDate date){
        this.accesAutoris=true;
        if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY) && batimentsBloque.contains(bat)){
            this.accesAutoris=false;
        }
    }

    public void accesNonAutoriseDurant2(Batiment bat, LocalDate date){
            if(assoBlocage_Bat_Date.keySet().contains(bat)){
                assoBlocage_Bat_Date.replace(bat,date);
            }
            else{
                assoBlocage_Bat_Date.put(bat,date);
            }
            System.out.println(assoBlocage_Bat_Date);
    }

    public void interroger() {

        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {
            var interm = entry.getKey().badgeDétécté(); //badge
            if (interm!=null && interm.getPersonne() != null ) {//feature de gestion de blocage selon porteur associé ou pas
                if (interm!=null && !badgesBloque.contains(interm)) {

                    this.numBadgePasse = interm.getNumSerie(); // recup le num du badge qui est passé
                    var porte= entry.getValue();
                    var batiment= checkDoorInBatiment(porte);
                    var blokOK= blocBatimentAccess(batiment);
                    if (!portesOuvertes.contains(porte) && !blokOK) {

                        porte.ouvrir();
                        portesOuvertes.add(entry.getValue());
                    }
                }//else ça n'ouvre rien
            }
        }

    }

    public Batiment checkDoorInBatiment(IPorte porte){
        for (Batiment batiment : assoBlocage_Bat_Date.keySet()) {
            if (batiment.getPortes().contains(porte)) {
                return batiment;
            }
        }
        return null; // S
    }

    public boolean blocBatimentAccess(Batiment b){
        if(assoBlocage_Bat_Date.keySet().contains(b) && b!=null){
            LocalDate date = assoBlocage_Bat_Date.get(b);
            if(date.equals(LocalDate.now())){
                return true;
            }
        }
        return false;

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