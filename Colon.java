
package projet_paa;
import java.util.ArrayList;

public class Colon {
    
    /*
    Classe permettant de representer un Colon avec son nom (String) et ses preferences en terme de ressources (ArrayList).
    */

    private String nomColon; //Nom du colon
    private ArrayList<Ressource> preference; //Modelisation de la preference d'un colon comme ArrayList car dynamique
    private Ressource ressourceAttribuee; /*Ressources obtenue par le colon*/
    private boolean jaloux = false; // Attribut pour indiquer si le colon est jaloux ou non

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
        return ressourceAttribuee;
    }

    public void setRessourceAttribue(Ressource r){
        ressourceAttribuee = r;
    }

    public boolean isJaloux() { //vérifie si le colon est jaloux
        return jaloux;
    }

    public void setJaloux(boolean jaloux) { //affecter le caractère jaloux au colon
        this.jaloux = jaloux;
    }

    public String toString() {
        return "Nom du colon : " + nomColon + "\nRessources préférées : " + preference + "\nRessource attribuée : " + ressourceAttribuee + "\nEst jaloux : " + jaloux;
    }
}
