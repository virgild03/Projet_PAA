
package projet_paa;
import java.util.ArrayList;

public class Colon {
    
    /*
    Classe permettant de representer un Colon avec son nom (String) et ses preferences en terme de ressources (ArrayList).
    */
    
    private String nomColon; //Nom du colon
    private ArrayList<Ressource> preference; //Modelisation de la preference d'un colon comme ArrayList car dynamique
    private Ressource ressourceAttribue; /*Ressources obtenue par le colon*/


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

    public void setPreference(Ressource r){
        preference.add(r);
    }

    public Ressource getRessourceAttribue(){
        return ressourceAttribue;
    }

    public void setRessourceAttribue(Ressource r){
        ressourceAttribue = r;
    }

    public String toString(){
        return "Nom du colon : " + nomColon + "\nRessources préférés : " + getPreference() +"\nRessource attribué : "+ ressourceAttribue;
    }
    
}
