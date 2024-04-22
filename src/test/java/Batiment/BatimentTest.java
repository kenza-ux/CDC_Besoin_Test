package Batiment;

import Batiment.Utils.LecteurFake;
import Batiment.Utils.PorteSpy;
import org.junit.Assert;
import org.junit.Test;

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
        badge.associerPersonne(personne);
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

        badge.associerPersonne(personne);
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
        badge.associerPersonne(personne);

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
        badge1.associerPersonne(personne);
        badge2.associerPersonne(personne);

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
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.associerPersonne(personne);
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        assertTrue(porte.ouvertureDemande());

    }





    }
