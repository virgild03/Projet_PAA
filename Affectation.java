
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
    private List<Ressource> ressourcesDisponibles; //liste des ressources dispo

    public Affectation(Colonie colonie) {
        this.colonie = colonie;
        this.affectation = new HashMap<>();
        this.ressourcesDisponibles = new ArrayList<>(colonie.getListeRessource());
    }

    /*
    affectation naive presentée dans le sujet. pour un colon rentré
    en parametre, lui affecte son objet dispo le plus haut dans ses preferences
     */
    public void affectationNaive(Colon c) {
        int objetPrefDispo = 0;
        while (!affectation.containsKey(c) && objetPrefDispo < c.getPreference().size()) {/*Tant que le colon n'a pas reçu
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

}
