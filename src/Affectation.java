package projet_paa.src;

import java.util.*;
import java.io.*;


public class Affectation {

    //public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.
    public  static HashMap<Colon, Ressource> affectation; //association des ressources aux colon avec un Dictionnaire.
    public static ArrayList<Ressource> ressourcesDisponibles; //liste des ressources dispo

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
    public static HashMap<Colon, Ressource> affectationAutomatique(Colonie colonie) throws ColonException {
        ArrayList<Colon> listeColons = colonie.getListeColons();
        ArrayList<Ressource> listeRessources = colonie.getListeRessource();
        affectation = new HashMap<>();

        // Affectation naïve selon les préférences :
        // Pour chaque colon, on parcourt ses ressources préférées dans l'ordre et on lui assigne
        // la première ressource encore disponible.
        List<Ressource> ressourcesDisponibles = new ArrayList<>(listeRessources);
        for (Colon colon : listeColons) {
            boolean assigne = false;
            for (Ressource pref : colon.getPreference()) {
                if (ressourcesDisponibles.contains(pref)) {
                    affectation.put(colon, pref);
                    colon.setRessourceAttribue(pref);
                    ressourcesDisponibles.remove(pref);
                    assigne = true;
                    break;
                }
            }
            // Si on n'a pas pu assigner une ressource préférée (cas improbable, puisque le nb ress = nb colons),
            // on assigne une ressource quelconque (dernière chance)
            if (!assigne && !ressourcesDisponibles.isEmpty()) {
                Ressource r = ressourcesDisponibles.remove(0);
                affectation.put(colon, r);
                colon.setRessourceAttribue(r);
            }
        }

        int coutTemporaire = colonie.calculerCout();
        int nbIterations = 10000; // Nombre d'itérations d'amélioration (modulable)

        for (int iteration = 0; iteration < nbIterations; iteration++) {
            boolean amelioration = false;

            // On mélange l'ordre des colons et ressources à chaque itération
            // pour explorer d'autres configurations et éviter les minima locaux
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

                            // Échange
                            affectation.put(colonCourant, ressourceCandidate);
                            colonCourant.setRessourceAttribue(ressourceCandidate);

                            affectation.put(autreColon, ressourceCourante);
                            autreColon.setRessourceAttribue(ressourceCourante);

                            int nouveauCout = colonie.calculerCout();

                            if (nouveauCout >= coutTemporaire) {
                                // On annule l'échange si pas d'amélioration
                                affectation.put(colonCourant, ressourceCourante);
                                colonCourant.setRessourceAttribue(ressourceCourante);

                                affectation.put(autreColon, ancienneRessourceAutreColon);
                                autreColon.setRessourceAttribue(ancienneRessourceAutreColon);
                            } else {
                                // Amélioration
                                coutTemporaire = nouveauCout;
                                amelioration = true;

                                // Optionnel : si on atteint le coût minimal, on peut arrêter
                                if (coutTemporaire == 1) {
                                    return affectation;
                                }
                            }
                        }
                    }
                }
            }

            if (!amelioration) {
                // Plus aucune amélioration n'est possible
                break;
            }
        }

        return affectation;
    }




    public static void sauvegarde(HashMap<Colon, Ressource> map){

		if(map == null || map.isEmpty()){
			System.out.println("Il faut affecter les ressources aux colons avant de pouvoir sauvegarder !!!");
		}
		else {
			System.out.println("\nChemin du fichier dans lequel il faut sauvegarder la colonie ? ");
			Scanner sc = new Scanner(System.in);
			String chemin = sc.nextLine();

			try( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(chemin)))) {

				for(Map.Entry<Colon,Ressource> entry : map.entrySet()){
					Colon c = entry.getKey();
					Ressource ressource = entry.getValue();
					System.out.println(c.getNomColon());
					System.out.println(ressource);
					pw.println(c.getNomColon()+":"+ressource.getNomRessource());
				}
			}
			catch(IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			System.out.println("La sauvegarde à été effectuée !!!");
		}
	}
}
