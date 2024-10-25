
package projet_paa;
import java.util.HashMap;
import java.util.*;

public class Affectation {
    
    /*
    Classe pour representer les affectations Colon / Ressource.
    On utilise HashMap parce que c'est la plus adaptée à notre situation au vue de la synchronisation
    et elle permet egalement de prendre des valeurs null. On en aura besoin parce que l'on va construire 
    Dynamiquement les associations.
    */

    private Colonie colonie;
    private HashMap<Colon, Ressource> affectation;

    public Affectation(Colonie colonie) {
        this.colonie = colonie;
        this.affectation = new HashMap<>();
    }

    /*
    affectation naive presentée dans le sujet. pour un colon rentré
    en parametre, lui affecte son objet dispo le plus haut dans ses preferences
     */
    public void affectationNaive(Colon c){


        ArrayList<Ressource> listeRessources = colonie.getListeRessource();

        // Créer une copie des ressources disponibles
        ArrayList<Ressource> ressourcesDisponibles = new ArrayList<>(listeRessources);
        int objetPrefDispo = 0;
        while(!affectation.containsKey(c)){
            //si dans ressourcesDisponibles il y a la preference objetPrefDispo-nième
            if (ressourcesDisponibles.contains(c.getPreference().get(objetPrefDispo))){
                // Mettre dans la hashmap affectation la clé colon c et en valeur la ressource la plus haut placée
                affectation.put(c, c.getPreference().get(objetPrefDispo));
                // Retirer cette ressource des ressources disponibles
                ressourcesDisponibles.remove(c.getPreference().get(objetPrefDispo));
            }
        }
    }

}
