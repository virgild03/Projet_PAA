
package projet_paa;
import java.util.HashMap;

public class Affectation {
    
    /*
    Classe pour representer les affectations Colon / Ressource.
    On utilise HashMap parce que c'est la plus adaptée à notre situation au vue de la synchronisation
    et elle permet egalement de prendre des valeurs null. On en aura besoin parce que l'on va construire 
    Dynamiquement les associations.
    */
    
    HashMap<Colon, Ressource> affectation;


}
