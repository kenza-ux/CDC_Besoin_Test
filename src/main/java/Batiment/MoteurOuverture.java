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
    private Map<Porteur, LocalDate> assoBlocage_Porteur_Porte= new HashMap();

    private LocalDate timeMaintenant= LocalDate.now();

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }




// un porteur qui a déjà une date de blocage se verra modifier par la nouvelle
    // s'il n'en a pas, alors il prend la nouvelle date
    public void accesNonAutoriseDurant(Porteur p, LocalDate date){
            if(assoBlocage_Porteur_Porte.keySet().contains(p)){
                assoBlocage_Porteur_Porte.replace(p,date);
            }
            else{
                assoBlocage_Porteur_Porte.put(p,date);
            }
            System.out.println(assoBlocage_Porteur_Porte);
    }

    public void interroger() {

        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {
            var interm = entry.getKey().badgeDétécté(); //badge
            if (interm!=null && interm.getPersonne() != null ) {//feature de gestion de blocage selon porteur associé ou pas
                if (interm!=null && !badgesBloque.contains(interm)) {

                    this.numBadgePasse = interm.getNumSerie(); // recup le num du badge qui est passé
                    var porte= entry.getValue();

                    if (!portesOuvertes.contains(porte)) {

                        porte.ouvrir();
                        portesOuvertes.add(entry.getValue());
                    }
                }//else ça n'ouvre rien
            }
        }

    }



    public void blocPorteAccessPorteur(Porteur p,LocalDate date){
        if(this.timeMaintenant.equals(date)){
            for (Badge b:p.getBadges()){
                bloquerBadge(b);
            }
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

    public void setDateAujourdhui(LocalDate timeMaintenant) {
        this.timeMaintenant = timeMaintenant;
    }

}