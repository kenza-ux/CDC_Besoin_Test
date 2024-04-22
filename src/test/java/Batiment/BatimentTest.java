package Batiment;

import Batiment.Utils.LecteurFake;
import Batiment.Utils.PorteSpy;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
public class BatimentTest {

    @Test
    public void casNominal(){
        //use case
    /// etant donne
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        lecteur.simulerDetecBadge();
//quand
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.interroger(); // on interroge un seul lecteur
/// alors
        assertTrue(porte.ouvertureDemande());

    }
    @Test // sans la simulation de badge pour voir si la porte s'ouvre
    public void casPasdeSimulation(){
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        //lecteur.simulerDetecBadge();
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.interroger();
        assertFalse(porte.ouvertureDemande());
    }

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

    @Test //test 4
    public void casDeuxPortesDeuxLecteurs(){
        var porteDevantOuvrir = new PorteSpy();
        var porteResteFermee = new PorteSpy();
        var lecteurDevantOuvrir = new LecteurFake();
        var lecteurResteFermee = new LecteurFake();

        lecteurDevantOuvrir.simulerDetecBadge();

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porteResteFermee,lecteurResteFermee);
        moteur.associer(porteDevantOuvrir,lecteurDevantOuvrir);

        moteur.interroger();
        assertTrue(porteDevantOuvrir.ouvertureDemande());
        assertFalse(porteResteFermee.ouvertureDemande());
    }

    @Test //test 6
    public void casDeuxPortesDeuxLecteursInverse(){
        var porteDevantOuvrir = new PorteSpy();
        var porteResteFermee = new PorteSpy();
        var lecteurDevantOuvrir = new LecteurFake();
        var lecteurResteFermee = new LecteurFake();

        lecteurDevantOuvrir.simulerDetecBadge();

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porteDevantOuvrir,lecteurDevantOuvrir);
        moteur.associer(porteResteFermee,lecteurResteFermee);

        moteur.interroger();
        assertFalse(porteResteFermee.ouvertureDemande());
        assertTrue(porteDevantOuvrir.ouvertureDemande());
    }

    @Test //test 5
    public void cas2Lecteurs1porte(){
        var porte = new PorteSpy();

        var lecteur1 = new LecteurFake();
        var lecteur2 = new LecteurFake();

        lecteur1.simulerDetecBadge();
        lecteur2.simulerDetecBadge();

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur1);
        moteur.associer(porte,lecteur2);

        moteur.interroger();
        Assert.assertEquals(1,porte.getNbSignals());
        assertTrue(porte.ouvertureDemande());

    }

    @Test //test 7
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

    @Test //test 8
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

    @Test //test 9
    public void cas2Badges() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge1 = new Badge(1);
        var badge2 = new Badge(2);

        lecteur.simulerDetecBadge(badge1);
        lecteur.simulerDetecBadge(badge2);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.bloquerBadge(badge1);

        moteur.interroger();
        assertTrue(porte.ouvertureDemande());
        assertEquals(2,moteur.getNumBadgePasse());
        assertNotEquals(1,moteur.getNumBadgePasse());

    }

    @Test //test 10
    public void cas2BadgesAutre() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge1 = new Badge(1);
        var badge2 = new Badge(2);

        lecteur.simulerDetecBadge(badge1);
        lecteur.simulerDetecBadge(badge2);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.bloquerBadge(badge1); // je bloque les 2 badges
        moteur.bloquerBadge(badge2);

        moteur.débloquerBadge(badge1); //on débloque le premier
        moteur.interroger();
        assertTrue(porte.ouvertureDemande());

        assertEquals(1,moteur.getNumBadgePasse());
        assertNotEquals(2,moteur.getNumBadgePasse());
    }




}