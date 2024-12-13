package projet_paa;

import java.util.*;
import java.io.*;


public class Affectation {
	
    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.
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

    // Affectation initiale : on associe chaque colon à la ressource correspondante par l'indice
    for (int i = 0; i < listeColons.size(); i++) {
        Colon colonCourant = listeColons.get(i);
        Ressource ressourceInitiale = listeRessources.get(i);
        affectation.put(colonCourant, ressourceInitiale);
        colonCourant.setRessourceAttribue(ressourceInitiale);
    }

    int coutTemporaire = colonie.calculerCout();

    // Tentative d'améliorer l'affectation
    for(int i = 0; i < listeColons.size(); i++) {
        for(int j = 0; j < listeRessources.size(); j++) {
            Colon colonCourant = listeColons.get(i);
            Ressource ressourceCourante = affectation.get(colonCourant);
            Ressource ressourceCandidate = listeRessources.get(j);

            // On ne tente l'échange que si la ressource candidate est différente de la ressource courante du colon
            if (!ressourceCandidate.equals(ressourceCourante)) {
                // Trouver le colon qui possède actuellement la ressource candidate
                Colon autreColon = null;

                /*
                boucle pour rechercher autreColon à qui est associé la ressource ressourceCandidate.
                Nouveau concept : Map.Entry.
                C'est une interface en java qui permet de parcourir les clé-valeurs d'une hashmap.
                entrySet() est une méthode de Map qui retourne un ensemble (de type Set) de toutes les paires clé-valeur présentes dans la hashmap affectationLocale.
                getKey() retourne la clé : un colon dans notre cas.
                getValue() retourne une valeur : une ressource dans notre cas.
                 */
                for (Map.Entry<Colon, Ressource> entry : affectation.entrySet()) //Pour toutes les "clé-valeurs" dans affectationLocale
                {
                    if (entry.getValue().equals(ressourceCandidate))  //Si une valeur de "clé-valeurs" = ressourceCandidate
                    {
                        autreColon = entry.getKey(); //autreColon devient la clé qui correspond à la valeur ressourceCandidate
                        break; //évitons de boucler pour rien tout de meme, la planète a des ressources limitées !
                    }
                }

                // S'il existe un autre colon détenant déjà la ressourceCandidate, on tente un échange
                if (autreColon != null && !autreColon.equals(colonCourant)) {
                    // Sauvegarde de l'état initial avant l'échange
                    Ressource ancienneRessourceAutreColon = affectation.get(autreColon);

                    // Échange : le colon courant prend la ressource candidate, l'autre colon prend la ressource courante
                    affectation.put(colonCourant, ressourceCandidate);
                    colonCourant.setRessourceAttribue(ressourceCandidate);

                    affectation.put(autreColon, ressourceCourante);
                    autreColon.setRessourceAttribue(ressourceCourante);

                    // Calcul du coût après l'échange
                    int nouveauCout = colonie.calculerCout();

                    // Si le coût ne baisse pas on revient à l'état initial (échange inverse).
                    if (nouveauCout >= coutTemporaire) {
                        affectation.put(colonCourant, ressourceCourante);
                        colonCourant.setRessourceAttribue(ressourceCourante);

                        affectation.put(autreColon, ancienneRessourceAutreColon);
                        autreColon.setRessourceAttribue(ancienneRessourceAutreColon);
                    } else {
                        // Le coût s'est amélioré, on met à jour coutTemporaire
                        coutTemporaire = nouveauCout;
                    }
                }
            }
        }
    }

    // La HashMap affectationLocale contient maintenant l'affectation minimisée
    return affectation;
}

    
	public static void sauvegarde(HashMap<Colon, Ressource> map){

		if(map.isEmpty()){
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
