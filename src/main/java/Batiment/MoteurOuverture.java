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
    private Map<Porteur, Map<IPorte, LocalDate>> blocage_Porteur_Porte_Date= new HashMap<>();

    private LocalDate timeMaintenant= LocalDate.now();

    public MoteurOuverture() {

    }

    public void associer(IPorte p, ILecteur l) {
        assosciation.put(l, p);

    }


    public void bloquerPorteAccesPorteurJourPrecis(Porteur p, IPorte porte, LocalDate jourBlocage) {


        if (blocage_Porteur_Porte_Date.containsKey(p)) {
            blocage_Porteur_Porte_Date.replace(p, Map.of(porte, jourBlocage)); // Mettre à jour l'entrée existante
        } else {
            blocage_Porteur_Porte_Date.put(p, Map.of(porte, jourBlocage)); // Créer une nouvelle entrée
        }
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
                if (!badgesBloque.contains(interm) ) {

                    this.numBadgePasse = interm.getNumSerie(); // recup le num du badge qui est passé
                    var porte= entry.getValue();

                    if (!portesOuvertes.contains(porte) && !isBloque(interm.getPersonne(), porte, timeMaintenant)) {

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

    private boolean isBloque(Porteur p, IPorte porte, LocalDate dateJour) {
        if (blocage_Porteur_Porte_Date.containsKey(p)) {
            Map<IPorte, LocalDate> blocagesPorteDate = blocage_Porteur_Porte_Date.get(p);
            return blocagesPorteDate.containsKey(porte) && blocagesPorteDate.get(porte).equals(dateJour);
        } else {
            return false;
        }
    }


    public void setDateAujourdhui(LocalDate timeMaintenant) {
        this.timeMaintenant = timeMaintenant;
    }

}