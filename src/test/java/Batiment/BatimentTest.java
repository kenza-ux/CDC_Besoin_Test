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
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.associer(personne);
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
        assertTrue(porte.ouvertureDemande());

    }

    @Test //test 11 : 1 badge dissocié de son porteur n'ouvre plus porte
    public void casBadgeDissocie() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.associer(personne);

        badge.dissocier();
        lecteur.simulerDetecBadge(badge);
        moteur.interroger();
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

    @Test // test 12   // on bloque l'accès à TOUTES les portes un jour
    public void casAccesAPorteSemaine(){
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

        // 1ere porte du batiment 1 //LocalDate.of(2024,04,21)
        /* le bloc à décom si on veut que le test soit assertFalse car date blocage et aujrd c'est la meme
        moteur.setDateAujourdhui(LocalDate.now());  // le jour où je veux acceder
        moteur.blocPorteAccessPorteur(personne,LocalDate.now());
        */
         
        moteur.setDateAujourdhui(LocalDate.of(2024,04,21));  // le jour où je veux acceder
        moteur.blocPorteAccessPorteur(personne,LocalDate.of(2024,04,21)); // le jour où je veux bloquer l'accès aux badgesS de ce porteur
        lecteur_porte1.simulerDetecBadge(badge);
        moteur.interroger();

        //assertTrue(porte1.ouvertureDemande()); // porte ouverte test passe car quand la date aujourd'hui et de bloc porteur diff
        assertFalse(porte1.ouvertureDemande()); // ne passe pas car les 2 dates pareil

        lecteur_porte2.simulerDetecBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
    }

    // on bloque l'accès a certaines portes un jour
    }
