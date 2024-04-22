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
    @Test
    public void cas2(){
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        //lecteur.simulerDetecBadge();
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.interroger();
        assertFalse(porte.ouvertureDemande());
    }




}
