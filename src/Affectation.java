package projet_paa.src;

import java.util.*;
import java.io.*;


public class Affectation {

    //public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.
    public  static HashMap<Colon, Ressource> affectation; //association des ressources aux colon avec un Dictionnaire.
    public static ArrayList<Ressource> ressourcesDisponibles; //liste des ressources dispo
    public static int cout;

    public static void init(Colonie colonie) {
        affectation = new HashMap<>();
        ressourcesDisponibles = new ArrayList<>(colonie.getListeRessource());
    }

    public static void affectationNaive(Colon c) {
        int objetPrefDispo = 0;
        while (!affectation.containsKey(c) && objetPrefDispo < c.getPreference().size()) {
        /*Tant que le colon n'a pas reçu
        de ressource et qu'il reste des préférences à vérifier*/
            Ressource resPreferee = c.getPreference().get(objetPrefDispo);
            if (ressourcesDisponibles.contains(resPreferee)) {/*verifie que la ressource préférée n'a pas été attribuée
            à un autre colon*/
                affectation.put(c, resPreferee);//sinon lui donner sa ressource préférée
                c.setRessourceAttribue(resPreferee);
                ressourcesDisponibles.remove(resPreferee);
            } else {
                objetPrefDispo++;//sinon revérifier pour la prochaine ressource préférée
            }
        }
        if (!affectation.containsKey(c)) { //si le colon n'a pas reçu une de ces préférences
            if (!ressourcesDisponibles.isEmpty()) { //si il reste des ressources disponibles
                Ressource ressource = ressourcesDisponibles.get(0); //prendre arbitrairement la première ressource disponible
                affectation.put(c, ressource);
                c.setRessourceAttribue(ressource);
                ressourcesDisponibles.remove(ressource);
            } else { //sinon ne pas donner de ressource au colon
                affectation.put(c, null);
            }
        }
    }

    /*
    Méthode qui renvoie une HashMap<Colon, Ressource> telle que:
   - Pour chaque colon, la ressource associée dans la HashMap correspond
     à une affectation minimisant le coût de jalousie.
   - Chaque ressource est affectée à un unique colon.
   - Lorsqu'on teste une nouvelle ressource pour un colon, on procède à un échange
     avec le colon qui la détient déjà (si besoin).
    */

    public static void affectationAutomatique(Colonie colonie) throws ColonException {
        ArrayList<Colon> listeColons = colonie.getListeColons();
        ArrayList<Ressource> listeRessources = colonie.getListeRessource();
        HashMap<Colon, Ressource> affectation = new HashMap<>();

        // Affectation naïve selon les préférences
        List<Ressource> ressourcesDisponibles = new ArrayList<>(listeRessources);
        for (Colon colon : listeColons) {
            boolean assigne = false;
            for (Ressource pref : colon.getPreference()) {
                if (ressourcesDisponibles.contains(pref)) {
                    affectation.put(colon, pref);
                    colon.setRessourceAttribue(pref);  // Attribuer la ressource au colon
                    ressourcesDisponibles.remove(pref);
                    assigne = true;
                    break;
                }
            }
            if (!assigne && !ressourcesDisponibles.isEmpty()) {
                Ressource r = ressourcesDisponibles.remove(0);
                affectation.put(colon, r);
                colon.setRessourceAttribue(r);  // Attribuer la ressource si aucune ressource préférée n'est disponible
            }
        }

        // Initialisation du coût
        int coutTemporaire = colonie.calculerCout();
        int nbIterations = 10000; // Nombre d'itérations d'amélioration
        double temperature = 1000.0; // Température initiale
        double coolingRate = 0.995; // Taux de refroidissement

        // Algorithme de recuit simulé
        for (int iteration = 0; iteration < nbIterations; iteration++) {
            boolean amelioration = false;

            // Mélange de l'ordre des colons et des ressources à chaque itération pour explorer d'autres configurations
            Collections.shuffle(listeColons);
            Collections.shuffle(listeRessources);

            for (int i = 0; i < listeColons.size(); i++) {
                for (int j = 0; j < listeRessources.size(); j++) {
                    Colon colonCourant = listeColons.get(i);
                    Ressource ressourceCourante = affectation.get(colonCourant);
                    Ressource ressourceCandidate = listeRessources.get(j);

                    if (!ressourceCandidate.equals(ressourceCourante)) {
                        // Trouver le colon qui a la ressource candidate
                        Colon autreColon = null;
                        for (Map.Entry<Colon, Ressource> entry : affectation.entrySet()) {
                            if (entry.getValue().equals(ressourceCandidate)) {
                                autreColon = entry.getKey();
                                break;
                            }
                        }

                        // Tenter l'échange
                        if (autreColon != null && !autreColon.equals(colonCourant)) {
                            Ressource ancienneRessourceAutreColon = affectation.get(autreColon);

                            // Effectuer l'échange via la méthode echangeRessource
                            colonie.echangeRessource(colonCourant, autreColon);

                            int nouveauCout = colonie.calculerCout();

                            // Acceptation de la solution si elle améliore le coût ou selon la température
                            if (nouveauCout < coutTemporaire) {
                                coutTemporaire = nouveauCout;
                                amelioration = true;
                            } else {
                                // On accepte une solution moins bonne avec une certaine probabilité
                                double probability = Math.exp((coutTemporaire - nouveauCout) / temperature);
                                Random rand = new Random();
                                if (rand.nextDouble() < probability) {
                                    coutTemporaire = nouveauCout;
                                    amelioration = true;
                                } else {
                                    // Annuler l'échange si la solution n'est pas acceptée
                                    colonie.echangeRessource(autreColon, colonCourant);
                                }
                            }
                        }
                    }
                }
            }

            // Diminuer la température
            temperature *= coolingRate;

            // Si aucune amélioration n'a eu lieu, on arrête
            if (!amelioration) {
                break;
            }
        }
    }

}
