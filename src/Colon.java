
package projet_paa.src;

import java.util.ArrayList;
import java.util.Objects;

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
        this.preference = new ArrayList<>();
        this.ressourceAttribuee = null;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Colon colon = (Colon) o;
        return Objects.equals(nomColon, colon.nomColon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomColon);
    }

    public String getNomRessourceIntoColon()
    {
        return ressourceAttribuee.getNomRessource();
    }
}
