package Batiment;

import java.util.ArrayList;
import java.util.List;

public class Porteur {

    private List<Badge> badges=new ArrayList<Badge>();
    private String nom;
    private String prenom;

    public Porteur(String nom, String prenom){
        this.nom=nom;
        this.prenom=prenom;
    }
    public void assiocierBadge(Badge b){
        this.badges.add(b);
    }

    public Porteur(List<Badge> badges, String nom, String prenom) {
        this.badges = badges;
        this.nom = nom;
        this.prenom = prenom;
    }

    public List<Badge> getBadges() {
        return badges;
    }






}
