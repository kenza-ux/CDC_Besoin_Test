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
    	// ETANT DONNE une Porte relier a un lecteur
    	//Et aucun badge n'est presenté a ce lecteur
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("acha","adam");
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        badge.attribuer(personne);
       //QUAND le Moteur d'Ouverture effectue une interrogation des lecteurs
        moteur.interroger();
        //Alors aucun signal n'est envoyé a la porte
        assertFalse(porte.ouvertureDemande());
    }
    //2
    @Test
    public void casPasInterrogation(){
    	// ETANT DONNE une Porte relier a un lecteur
    	//Et un badge est presenté a ce lecteur
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        //quand il ya pas d'interogation effectuée par le moteur d'ouverture, juste simulation de badge
        lecteur.simulerDetectionBadge();
       //Alors aucun signal D'ouverture n'est envoyé a la porte et reste fermée
        assertFalse(porte.ouvertureDemande());
    }

    @Test //test 3
    public void casDeuxPortesDeuxLecteursAvecBadgeConnu(){
    	// ETANT DONNE deux Portes reliées a deux  lecteur successivement
    	//Et un  badge qui peut ouvrir ces deux porte est presenté a une seule porte
        var porteDevantOuvrir = new PorteSpy();
        var porteResteFermee = new PorteSpy();
        var lecteurDevantOuvrir = new LecteurFake();
        var lecteurResteFermee = new LecteurFake();
        
        Porteur personne = new Porteur("acha","adam");
        var badge = new Badge(1);
        badge.attribuer(personne);
        
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porteResteFermee,lecteurResteFermee);
        moteur.associer(porteDevantOuvrir,lecteurDevantOuvrir);
        //quand j'essaie d'ouvrir la porte devant s'ouvrir
        lecteurDevantOuvrir.simulerDetectionBadge(badge);       
        moteur.interroger();
        //alors un signal d'ouverture est envoyé seulement a la porte devant s'ouvrir
        assertTrue(porteDevantOuvrir.ouvertureDemande());
        //et l'autre reste fermée
        assertFalse(porteResteFermee.ouvertureDemande());
    }

    @Test //test 4
    public void casDeuxPortesDeuxLecteursBadgeNonConnu(){
    	// ETANT DONNE deux Porte relier a deux  lecteur
    	
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();
        var lecteur1 = new LecteurFake();
        var lecteur2 = new LecteurFake();

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte1,lecteur1);
        moteur.associer(porte2,lecteur2);
        
        //Et aucun badge attribué à aucun porteur
        //le badge dans simuler est par défaut donc non associé à un porteur
        //quand j'essaie d'ouvrir la porte 1
        lecteur1.simulerDetectionBadge();
        moteur.interroger();
        //alors elle reste fermée
        assertFalse(porte1.ouvertureDemande());

        //quand j'essaie d'ouvrir porte 2
        lecteur2.simulerDetectionBadge();
        moteur.interroger();
        //alors elle reste fermée aussi
        assertFalse(porte2.ouvertureDemande());
    }


    @Test //test 5
    public void cas2Lecteurs1porte(){
    	//Etant donné une porte reliée a 2 lecteurs
    	//Et un badge qui peut ouvrir 
        var porte = new PorteSpy();
        var lecteur1 = new LecteurFake();
        var lecteur2 = new LecteurFake();
        var badge = new Badge(1);
        
        Porteur personne = new Porteur("kz","mz");
        badge.attribuer(personne);  
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur1);
        moteur.associer(porte,lecteur2);
        // si je simule les 2 lecteurs de la meme porte
        lecteur1.simulerDetectionBadge(badge);
        lecteur2.simulerDetectionBadge(badge);
        //et je demande l'ouverture
        moteur.interroger();
        // ALORS un seul signal de demande d'ouverture sera accepté et la porrte s'ouvre
        Assert.assertEquals(1,porte.getNbSignals());
        assertTrue(porte.ouvertureDemande());

    }

    @Test //test 6
    public void casBadgeBloque() {
    	//Etant donnée une porte relié a un lecteur
    	//ET un badge capable d'ouvrir cette porte
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge();

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        //SI ce badge est bloqué
        moteur.bloquerBadge(badge);
        //Quand le moteur interoge
        lecteur.simulerDetectionBadge(badge);
        moteur.interroger();
        //Alors Aucun signal n'est envoyé 
        assertFalse(porte.ouvertureDemande());
    }

    @Test //test 7
    public void casBadgeDébloque() {
    	//Etant donnée une porte relié a un lecteur
    	//ET un badge capable d'ouvrir cette porte
    	//ET ce badge est bloqué
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge();
        Porteur personne = new Porteur("kz","mz");
        badge.attribuer(personne);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        moteur.bloquerBadge(badge);
        
        //Qaund on debloque ce badge et j'essaie d'ouvrir la porte
        moteur.débloquerBadge(badge);
        
        lecteur.simulerDetectionBadge(badge);
        moteur.interroger();
        //alors la porte s'ouvre
        assertTrue(porte.ouvertureDemande());
    }

    @Test //test 8
    public void cas2Badges() {
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge1 = new Badge(1);
        var badge2 = new Badge(2);

        Porteur personne = new Porteur("kz","mz");
        badge1.attribuer(personne);
        badge2.attribuer(personne);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        lecteur.simulerDetectionBadge(badge1);
        moteur.interroger();
        assertTrue(porte.ouvertureDemande()); // porte ouverte avec 1 badge non bloqué

        lecteur.simulerDetectionBadge(badge2);
        moteur.bloquerBadge(badge2); // on bloque le 2ème
        moteur.interroger();
        assertTrue(porte.ouvertureDemande()); // porte reste ouverte grace au badge 1

        assertEquals(1,porte.getNbSignals()); // le nbre de signal d'ouverture est tjr 1


    }

    @Test //test 9 : 1 badge sans porteur n'ouvre pas porte 
    public void casBadgeSansPorteur() {
    	//Etant donné une porte Et  un badge  qui na pas de porteur
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);

        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        lecteur.simulerDetectionBadge(badge);
        //Qaund le moteur interroge
        moteur.interroger();
        //Alors aucun signal d'ouvertur n'est envoyé 
        assertFalse(porte.ouvertureDemande());
    }

    @Test //test 10 : 1 badge avec porteur ouvre porte
    public void casBadgeAvecPorteur() {
    	//Etant  une porte relié a un lecteur
    	//porteur a un badge attribué a un porteur
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        
        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.attribuer(personne);
        //Si le porteur presente un badge a un lecteur
        lecteur.simulerDetectionBadge(badge);
        //Quand le moteur interroge
        moteur.interroger();
        //ALORS un signal d'ouverture est envoyé
        assertTrue(porte.ouvertureDemande());

    }

    @Test //test 11 : 1 badge liberer de son porteur n'ouvre plus porte
    public void casBadgeDissocie() {
    	
    	//Etant une porte relier a un lecteur et un  badge est attribué a une personne 
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porte, lecteur);
        badge.attribuer(personne);
        //Si on dissocie ce badge de son porteur
        badge.liberer();
        //ET on essaie d'ouvrir une porte du batiment avec ce dernier
        lecteur.simulerDetectionBadge(badge);
        moteur.interroger();
        //Alors Aucun signale d'ouverture n'est envoyé
        assertFalse(porte.ouvertureDemande());
    }


    @Test // test 12   // on bloque l'accès à TOUTES les portes pour une journée
    public void casAccesNonAutoriserToutePorteUneJournée(){
    	//Etant donée qu on a deux portes  du batiment relier a deux lecteurs
    	//Et un badge qui peut ouvrir ces deux porte et attribué a une personne
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();
        var lecteur_porte1= new LecteurFake();
        var lecteur_porte2 = new LecteurFake();
        MoteurOuverture moteur = new MoteurOuverture();
        //ET LA date d'aujourd'hui est 2024/04/21
        moteur.setDateAujourdhui(LocalDate.of(2024, 04, 21));
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        badge.attribuer(personne);
        moteur.associer(porte1, lecteur_porte1);
        moteur.associer(porte2, lecteur_porte2);
        //Si On bloque  la date 2024/04/21
        moteur.blocPorteAccessPorteur(personne,LocalDate.of(2024,04,21));
        lecteur_porte1.simulerDetectionBadge(badge);
        //Quand le moteur interroge
        moteur.interroger();
        //Alors Aucun signal n'est envoyé
        assertFalse(porte2.ouvertureDemande());
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        moteur.débloquerBadge(badge);
        //Et si la date d'aujourd'hui et differente de la date du blocage
        moteur.setDateAujourdhui(LocalDate.of(2024, 05, 21));
        lecteur_porte1.simulerDetectionBadge(badge);
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        //Alors un signal d'ouverture est envoyé
        assertTrue(porte1.ouvertureDemande());
        assertTrue(porte2.ouvertureDemande());

    }
    
    @Test // test 13   // on bloque l'accès plusieur jours
    public void casAccesNonAutoriserToutePortePlusieurJournée(){
    	//Etant donée qu on a deux portes  du batiment relier a deux lecteurs
    	//Et un badge qui peut ouvrir ces deux porte et attribué a une personne
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();
        var lecteur_porte1= new LecteurFake();
        var lecteur_porte2 = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");
        MoteurOuverture moteur = new MoteurOuverture();
        badge.attribuer(personne);
        moteur.associer(porte1, lecteur_porte1);
        moteur.associer(porte2, lecteur_porte2);
        //ET LA date d'aujourd'hui est 2024/04/21
        moteur.setDateAujourdhui(LocalDate.of(2024,04,21));
        //Si on bloque l'access a cette personne dans le 2024,04,2 et le 2024, 04, 22
        //ET cette personne essaie d ouvrire les portes du batiment dans l'une de ces journée
        moteur.blocPorteAccessPorteur(personne,LocalDate.of(2024,04,21),LocalDate.of(2024, 04, 22));
        lecteur_porte1.simulerDetectionBadge(badge);
        moteur.interroger();
        //Alors Aucun signal d'ouverture n'est envoyé a aucune des deux portes
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        moteur.setDateAujourdhui(LocalDate.of(2024,04,22));
        lecteur_porte1.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte1.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        //Et si la personne essaie d'acceder a des porte du batiment 
        //ET la date du jours n'est pas une date  ou cette personne est bloqué
        moteur.setDateAujourdhui(LocalDate.of(2024,05,23));
        lecteur_porte1.simulerDetectionBadge(badge);
        //Alors un signale d'ouverture est envoyé
        moteur.interroger();
        assertTrue(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        assertTrue(porte2.ouvertureDemande());
    }
    
    
    @Test //test 14 bloque l access a une personne durant une periode de temps
    public void casAccesNonAutoriserToutePorteIntervaleTemp(){
    	//Etant donée qu on a deux portes  du batiment relier a deux lecteurs
    	//Et un badge qui peut ouvrir ces deux porte et attribué a une personne
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();
        var lecteur_porte1= new LecteurFake();
        var lecteur_porte2 = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        
        badge.attribuer(personne);
        moteur.associer(porte1, lecteur_porte1);
        moteur.associer(porte2, lecteur_porte2);
        //Et la date du jour est le 2024,04,21
        moteur.setDateAujourdhui(LocalDate.of(2024,04,21));
        //ET cette personne et bloquée pendant le 21/04 jusqu'au 25/04
        moteur.blocAccessDurant(personne,LocalDate.of(2024,04,21),LocalDate.of(2024, 04, 25));
       //SI il essaie d acceder a ces deux porte de battiment
        lecteur_porte1.simulerDetectionBadge(badge);
        moteur.interroger();
        //ALORS aucun signal d'ouverture n'est envoyé
        assertFalse(porte1.ouvertureDemande());
        moteur.setDateAujourdhui(LocalDate.of(2024,04,23));
        lecteur_porte1.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(porte2.ouvertureDemande());
        // SI la date du jour n'est pas entre le 21/04 et le 25/04
        moteur.setDateAujourdhui(LocalDate.of(2024,05,23));
        lecteur_porte1.simulerDetectionBadge(badge);
        moteur.interroger();
        //ALORS il n ya plus de blocage et le signal d'ouverture est envoyé
        assertTrue(porte1.ouvertureDemande());
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        assertTrue(porte2.ouvertureDemande());
    }
    
    @Test//15  on bloque l'accès a certaines portes un jour
    public void casBloquerCertainesPortesPourUnePeronneJourPrecis(){
    	//ETANT donnée qu on a deux porte
    	//Et une personne avec un badge qui ouvre ces deux porte
        var porte1 = new PorteSpy();
        var porte2 = new PorteSpy();

        var lecteur_porte1= new LecteurFake();
        var lecteur_porte2 = new LecteurFake();

        var badge = new Badge(1);
        Porteur personne = new Porteur("kz","mz");

        MoteurOuverture moteur = new MoteurOuverture();
        badge.attribuer(personne);

        moteur.associer(porte1, lecteur_porte1);
        moteur.associer(porte2, lecteur_porte2);
        //Quand on bloque l'access a une personne pour la premiere porte aujourd'hui
        moteur.bloquerPorteAccesPorteurJourPrecis(personne, porte1,LocalDate.now());
        lecteur_porte1.simulerDetectionBadge(badge); 
        moteur.interroger();
        //alors la premiere porte ne souvre pas
        assertFalse(porte1.ouvertureDemande()); // le test n passe pas, porte bloquée pour ce user

        //quand j'essaie d'ouvrir la porte 2 le meme jour
        lecteur_porte2.simulerDetectionBadge(badge);
        moteur.interroger();
        //ALORS elle s'ouvre
        assertTrue(porte2.ouvertureDemande());
    }
    
    
    @Test //test 16
    public void casPorteDefaillante() throws Exception {
        //Etant Donnée qu'une porte est dafaillante
    	//ET une porte non Defaillante
    	//ET un badge qui ouvre les deux porte
        var porteNormale = new PorteSpy();

        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        Porteur personne = new Porteur("Dupont", "Jean");
        MoteurOuverture moteur = new MoteurOuverture();
        moteur.associer(porteNormale, lecteur);
        badge.attribuer(personne);
        lecteur.simulerDetectionBadge(badge);
        //QUAND on essaie d'acceder a la porte non defaillante
        moteur.interroger();
        //alors la porte s'ouvre
        assertTrue(porteNormale.ouvertureDemande()); // La porte normale s'ouvre

       //ET si on essaie d'acceder a la porte defaillante
        try{

            var porteDefaillante = new PorteDummy();
            moteur.associer(porteDefaillante, lecteur);

            lecteur.simulerDetectionBadge(badge);
            moteur.interroger();
            assertFalse(porteDefaillante.isOuverte());
            //ALORS aucun signale n'est envoyé au moteur 
            } catch (Exception e) {
                assertEquals("cette porte est défaillante", e.getMessage());

            }

    }
    
    
    	@Test //test17
  		public void declecherAlarm() {
    	//Etant donnée une porte relié a un lecteur
    	//Etant donné un badge qui na pas de porteur
        var porte = new PorteSpy();
        var lecteur = new LecteurFake();
        var badge = new Badge(1);
        MoteurOuverture moteur= new MoteurOuverture();
        moteur.associer(porte,lecteur);
        lecteur.simulerDetectionBadge(badge);
        //SI ce badge est presenté 
        //Et Si on refait la tentative 2 autre fois
        moteur.interroger();
        assertFalse(moteur.isAlarm());
        lecteur.simulerDetectionBadge(badge);
        moteur.interroger();
        assertFalse(moteur.isAlarm());
        lecteur.simulerDetectionBadge(badge);
        moteur.interroger();
        //Alors la porte ne s'ouvre pas
        assertFalse(porte.ouvertureDemande());
        //ET une Alarm est declencher
        assertTrue(moteur.isAlarm());
    }
    	
    	@Test//2 personnes avec une porte bloquée affiliée sur un jour précis, mais ont accès à la porte de l'autre
    	public void testAccesBloqueJourPrecisDifferentesPersonnes() {
            // Création des objets
            var porteBloqueAlice = new PorteSpy();
            var lecteur1 = new LecteurFake();
            var badgeAlice = new Badge(1);
            var alice = new Porteur("Alice", "Durant");
            var moteur = new MoteurOuverture();

            var porteBloqueBob = new PorteSpy();
            var lecteur2 = new LecteurFake();
            var bob = new Porteur("Bob", "Martin");
            Badge badgeBob = new Badge(2);

            // Association des éléments Alice
            moteur.associer(porteBloqueAlice, lecteur1);
            badgeAlice.attribuer(alice);


            // Blocage dee l'accès pour Alice aujourd'hui et donc ne peut pas y accéder
            moteur.bloquerPorteAccesPorteurJourPrecis(alice, porteBloqueAlice, LocalDate.now());
            lecteur1.simulerDetectionBadge(badgeAlice);
            moteur.interroger();
            assertFalse(porteBloqueAlice.ouvertureDemande()); // La porte ne s'ouvre pas

            // Association des éléments Bob
            moteur.associer(porteBloqueBob, lecteur2);
            badgeBob.attribuer(bob);

            // Blocage de l'accès pour Bob 01/04/2024 et donc ne peut pas y accéder ce jour là
            //moteur.setDateAujourdhui(LocalDate.of(2024, 4, 1));
            moteur.bloquerPorteAccesPorteurJourPrecis(bob, porteBloqueBob, LocalDate.now());
            lecteur2.simulerDetectionBadge(badgeBob);
            moteur.interroger();
            assertFalse(porteBloqueBob.ouvertureDemande()); // La porte ne s'ouvre pas pour bob

            //alice essaye d'acceder à la porte 2
            lecteur2.simulerDetectionBadge(badgeAlice);
            moteur.interroger();
            assertTrue(porteBloqueBob.ouvertureDemande()); // L'autre porte s'ouvre pour Alice

            //bob essaye d'acceder à la porte 1
            lecteur1.simulerDetectionBadge(badgeBob);
            moteur.interroger();
            assertTrue(porteBloqueAlice.ouvertureDemande()); // L'autre porte s'ouvre pour bob



        }

    
    
    
    }
