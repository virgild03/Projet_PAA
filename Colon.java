
package projet_paa;

import java.util.ArrayList;

public class Colon {
    
    /*
    Classe permettant de representer un Colon avec son nom (String) et ses preferences en terme de ressources (ArrayList).
    */
    
    private String nomColon; //Nom du colon
    private ArrayList<Ressource> preference;  //Modelisation de la preference d'un colon comme ArrayList car dynamique
    
    
    public Colon(String n)  //Constructeur qui initialise le nom et la liste vide
    {
        this.nomColon = n;
        preference = new ArrayList<>();
    }
    
    public String getNomColon() //fonction pour recuperer le nom ailleur
    {
        return nomColon;
    }
    
    public ArrayList<Ressource> getPreference() //fonction pour recuperer la liste de preference pour chaque instance de colon
    {
        return preference;
    }
    
    
}
