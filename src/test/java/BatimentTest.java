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




}
