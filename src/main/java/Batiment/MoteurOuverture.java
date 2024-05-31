package Batiment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoteurOuverture {

    private Map<ILecteur, IPorte> assosciation = new HashMap();
    private List<IPorte> portesOuvertes = new ArrayList<>();
    private List<Badge> badgesBloque= new ArrayList<>();
    private boolean accesAutoris=true;
    private int numBadgePasse;
    private Map<Porteur, LocalDate> assoBlocage_Porteur_Porte= new HashMap();
    private Map<Porteur,List<LocalDate>> assoPorteurDateBloc = new HashMap(); 
    private boolean blocPermanent = false;
    private LocalDate timeMaintenant= LocalDate.now(
    		);

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
    }

    public void interroger() {
        for (Map.Entry<ILecteur, IPorte> entry : assosciation.entrySet()) {	
            Badge interm = entry.getKey().badgeDétécté(); //badge
            //on debloque la personne si il figure dans la list des personne bloque pour une date et on est pas dans cette derniere 
          if(interm!=null)  debloquerPersonneSiDateBlocageDifferente(interm);
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
    
    public void debloquerPersonneSiDateBlocageDifferente(Badge badge) {
    	Porteur porteur = badge.getPersonne();
    	if(porteur!=null) {
    		  if(this.assoPorteurDateBloc.keySet().contains(porteur) 
    	        		&& !this.assoPorteurDateBloc.get(porteur).contains(this.timeMaintenant)
    	        		&& !this.assoPorteurDateBloc.get(porteur).contains(LocalDate.of(3000, 01, 01)) 
    	        		&& this.badgesBloque.contains(badge)) this.débloquerBadge(badge);
    	    }
    	}
      



    public void blocPorteAccessPorteur(Porteur p,LocalDate ...dates){
    	List<LocalDate> datesList=new ArrayList<>();
    	for(LocalDate d : dates) {
    		datesList.add(d);
    	}
    	  if(this.assoPorteurDateBloc.keySet().contains(p)) {
    		  this.assoPorteurDateBloc.get(p).addAll(List.of(dates));
    	  }else {
    		  this.assoPorteurDateBloc.put(p,List.of(dates) );
    	  }
    	for(LocalDate d:dates) {
    	
    	  if(this.timeMaintenant.equals(d)){
    	          for (Badge b:p.getBadges()){
    	              bloquerBadge(b,datesList);
    	              
    	          }
    	      }
    	}
       
    }
    
    public void blocAccessDurant(Porteur p,LocalDate dateDebut,LocalDate dateFin) {
    	List<LocalDate> datesList=getDatesBetween(dateDebut,dateFin);
    	if(this.assoPorteurDateBloc.keySet().contains(p)) 
    	this.assoPorteurDateBloc.get(p).addAll(datesList);
  	    else
  	    this.assoPorteurDateBloc.put(p,datesList);
  	    for (Badge b:p.getBadges()){
  	         bloquerBadge(b,datesList);
  	        }
    }

    public void bloquerBadge(Badge b){
    	Porteur porteur = b.getPersonne();
    	if(this.assoPorteurDateBloc.containsKey(b.getPersonne())) this.assoPorteurDateBloc.replace(porteur, List.of(LocalDate.of(3000,01,01)));
    	else this.assoPorteurDateBloc.put(porteur,List.of(LocalDate.of(3000,01,01)));
       badgesBloque.add(b);
    }
    
    public void bloquerBadge(Badge b,List<LocalDate>dates){
    	Porteur porteur = b.getPersonne();
    	if(this.assoPorteurDateBloc.containsKey(b.getPersonne())) {
    		List<LocalDate> ds = this.assoPorteurDateBloc.get(porteur);
    		ds = new ArrayList<>();
    		ds.addAll(ds);
    		
    		}
    	else this.assoPorteurDateBloc.put(porteur,dates);
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
    
    public List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                     .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                     .collect(Collectors.toList());
    }

}