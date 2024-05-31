package Batiment;

import Batiment.Utils.LecteurFake;
import Batiment.Utils.PorteDummy;
import Batiment.Utils.PorteSpy;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
public class BatimentTest {

    //TEST 1
    @Test // sans la simulation de badge pour voir si la porte s'ouvre
    public void casPasdeSimulation(){
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("acha","adam");
        //lecteur.simulerDetecBadge();
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        badge.associer(personne);
        moteur.interroger();
        assertFalse(porte.ouvertureDemande());
    }
    //2
    @Test
    public void casPasInterrogation(){
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        lecteur.simulerDetecBadge();
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        //moteur.interroger();
        assertFalse(porte.ouvertureDemande());
    }

    @Test //test 3
    public void casDeuxPortesDeuxLecteursAvecBadgeConnu(){
        var porteDevantOuvrir = new PorteSpy();
        var porteResteFermee = new PorteSpy();
        var lecteurDevantOuvrir = new LecteurFake();
        var lecteurResteFermee = new LecteurFake();
        Porteur personne = new Porteur("acha","adam");
        var badge = new Badge(1);

        badge.associer(personne);
        lecteurDevantOuvrir.simulerDetecBadge(badge);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porteResteFermee,lecteurResteFermee);
        moteur.associer(porteDevantOuvrir,lecteurDevantOuvrir);

        moteur.interroger();
        assertTrue(porteDevantOuvrir.ouvertureDemande());
        assertFalse(porteResteFermee.ouvertureDemande());
    }

    @Test //test 4
    public void casDeuxPortesDeuxLecteursBadgeNonConnu(){
        var porteDevantOuvrir = new PorteSpy();
        var porteResteFermee = new PorteSpy();
        var lecteurDevantOuvrir = new LecteurFake();
        var lecteurResteFermee = new LecteurFake();
        //le badge dans simuler est par défaut donc non associé à un porteur
        lecteurDevantOuvrir.simulerDetecBadge();

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porteResteFermee,lecteurResteFermee);
        moteur.associer(porteDevantOuvrir,lecteurDevantOuvrir);

        moteur.interroger();
        assertFalse(porteDevantOuvrir.ouvertureDemande());
        assertFalse(porteResteFermee.ouvertureDemande());
    }


    @Test //test 5
    public void cas2Lecteurs1porte(){
        var porte = new PorteSpy();

        var lecteur1 = new LecteurFake();
        var lecteur2 = new LecteurFake();

        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        badge.associer(personne);

        lecteur1.simulerDetecBadge(badge);
        lecteur2.simulerDetecBadge(badge);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur1);
        moteur.associer(porte,lecteur2);
        moteur.interroger();
        Assert.assertEquals(1,porte.getNbSignals());
        assertTrue(porte.ouvertureDemande());

    }

    @Test //test 6
    public void casBadgeBloque() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge();

        lecteur.simulerDetecBadge(badge);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.bloquerBadge(badge);

        moteur.interroger();
        assertFalse(porte.ouvertureDemande());
    }

    @Test //test 7
    public void casBadgeDébloque() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge();
        Porteur personne = new Porteur("kz","mz");
        badge.associer(personne);

        lecteur.simulerDetecBadge(badge);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.bloquerBadge(badge);
        moteur.débloquerBadge(badge);

        moteur.interroger();
        assertTrue(porte.ouvertureDemande());
    }

    @Test //test 8
    public void cas2Badges() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge1 = new Badge(1);
        var badge2 = new Badge(2);

        Porteur personne = new Porteur("kz","mz");
        badge1.associer(personne);
        badge2.associer(personne);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        lecteur.simulerDetecBadge(badge1);
        moteur.interroger();
        assertTrue(porte.ouvertureDemande()); // porte ouverte avec 1 badge non bloqué

        lecteur.simulerDetecBadge(badge2);
        moteur.bloquerBadge(badge2); // on bloque le 2ème
        moteur.interroger();
        assertTrue(porte.ouvertureDemande()); // porte reste ouverte grace au badge 1

        assertEquals(1,porte.getNbSignals()); // le nbre de signal d'ouverture est tjr 1


    }

    @Test //test 9 : 1 badge sans porteur n'ouvre pas porte NOUVEAU CAS NOMINAL
    public void casBadgeSansPorteur() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte.ouvertureDemande());
    }

    @Test //test 10 : 1 badge avec porteur ouvre porte
    public void casBadgeAvecPorteur() {
    	//Etant donne un porteur a un badge associer
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        
        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.associer(personne);
        //Si le porteur presente un badge a un lecteur
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        //ALORS la porte s'ouvre
        assertTrue(porte.ouvertureDemande());

    }

    @Test //test 11 : 1 badge dissocié de son porteur n'ouvre plus porte
    public void casBadgeDissocie() {
    	//Etant donné qu un badge est associer a une personne 
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.associer(personne);
        //Si on dissocie ce badge de son porteur
        //ET on essaie d'ouvrir une porte du batiment avec ce dernier
        badge.dissocier();
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        //alors la porte ne s'ouvre pas
        assertFalse(porte.ouvertureDemande());
    }
/*
    @Test //test 12 : porte dummy
    public void casPorteDefaillante() throws Exception {
        var porteNormale = new PorteSpy();
        var porteDefaillante= new PorteDummy();
       // var porteDefaillante= new PorteSpy();
       // porteDefaillante.porteDefaillante();
        assertTrue(porteDefaillante.Exception.getMessage(), contains(expectedMessage));
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        MoteurOuverture moteur = new MoteurOuverture();

        moteur.associer(porteNormale, lecteur);
        badge.associer(personne);
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        assertTrue(porteNormale.ouvertureDemande());

        moteur.associer(porteDefaillante, lecteur);
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        //assertFalse(porteDefaillante.ouverrir());


    }

*/

    @Test // test 12   // on bloque l'accès à TOUTES les portes pour une journée
    public void casAccesNonAutoriserToutePorteUneJournée(){
    	//Etant donée qu on a des porte  du batiment
    	//ET les on bloque l acces a toute les porte des batiment pour aujourd'hui
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();
        var lecteur_porte1= new LecteurFake();
        var lecteur_porte2 = new LecteurFake();
        MoteurOuverture moteur = new MoteurOuverture();        
        moteur.setDateAujourdhui(LocalDate.of(2024, 04, 21));
        //ET UN porteur   avec un badge tente d'ouvrire une porte
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        badge.associer(personne);
        moteur.associer(porte1, lecteur_porte1);
        moteur.associer(porte2, lecteur_porte2);
        //Si le porteur essaie d'acceder a des portes des batiment
        moteur.blocPorteAccessPorteur(personne,LocalDate.of(2024,04,21));
        lecteur_porte1.simulerDetecBadge(badge);
        moteur.interroger();
        //Alors les portes ne s'ouvre pas
        assertFalse(porte2.ouvertureDemande());
        lecteur_porte2.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        moteur.débloquerBadge(badge);
        moteur.setDateAujourdhui(LocalDate.of(2024, 05, 21));
        lecteur_porte1.simulerDetecBadge(badge);
        moteur.interroger();
        assertTrue(porte1.ouvertureDemande());

    }
    
    @Test // test 13   // on bloque l'accès plusieur jours
    public void casAccesNonAutoriserToutePortePlusieurJournée(){
    	//Etant donnée qu une personne avec un badge
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();
        var lecteur_porte1= new LecteurFake();
        var lecteur_porte2 = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        MoteurOuverture moteur = new MoteurOuverture();
        badge.associer(personne);
        moteur.associer(porte1, lecteur_porte1);
        moteur.associer(porte2, lecteur_porte2);
        moteur.setDateAujourdhui(LocalDate.of(2024,04,21));
        //Si on bloque l'access a cette personne dans des jours precise
        //ET cette personne essaie d ouvrire les portes du batiment dans l'une de ces journée
        moteur.blocPorteAccessPorteur(personne,LocalDate.of(2024,04,21),LocalDate.of(2024, 04, 22));
        lecteur_porte1.simulerDetecBadge(badge);
        moteur.interroger();
        //Alors les porte ne vont pas s'ouvrir.
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        
        moteur.setDateAujourdhui(LocalDate.of(2024,04,22));
        lecteur_porte1.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte1.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        //Et si la personne essaie d'acceder a des porte du batiment 
        //ET la date du jours n'est pas une date  ou cette personne est bloqué
        moteur.setDateAujourdhui(LocalDate.of(2024,05,23));
        lecteur_porte1.simulerDetecBadge(badge);
        //Alors les portes vont s'ouvrir
        moteur.interroger();
        assertTrue(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetecBadge(badge);
        moteur.interroger();
        assertTrue(porte2.ouvertureDemande());
    }
    
    
    
    
    
    
    }
