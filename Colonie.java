
package projet_paa;

import java.util.*;

public class Colonie {
    /*
    Permet de faire une instance de colonie avec : 
    une collection de colons
    une collection de ressources
    la gestion de relation
    */
    
    
    private ArrayList<Ressource> listeRessource;
    
    //La clé est un colon, la valeur est un ensemble de colons avec lesquels ce colon a une relation "ne s'aiment pas".
    //Systeme de dictionnaire avec en clé un colon et en valeur un ensemble de colons avec qui la clé a une relation
    private HashMap<Colon, Set<Colon>> relations; 

    public Colonie() {
        // Constructeur de la colonie en créant la hashMap (dictionnaire) associant à chaque colon ses relations
        //On ajoute à chaque ensemble "valeur" d'un colon tous les colons avec qui il ne s'entend pas.
        this.relations = new HashMap<>();
    }

    
    /*
    Si le colon rentré en parametre n'existe pas dans la hashmap relation alors on ajoute une clé colon
    rentré en parametre et on lui attribue un set (ensemble vide) des colons avec qui sa relation est mauvaise
    */
    public void ajouterColon(Colon colon) {
        if (!relations.containsKey(colon)) {
            relations.put(colon, new HashSet<>());
        }
    }

    /*
    permet d'ajouter une MAUVAISE relation entre deux colons entrés en parametre
    
    */
    public void ajouterMauvaiseRelation(Colon colon1, Colon colon2) {
        ajouterColon(colon1); //Permet d'ajouter le colon1 à la colonie si ce nest pas deja fait, ne fait rien sinon
        ajouterColon(colon2); //De meme pour le colon2
        relations.get(colon1).add(colon2); //ajoute le colon2 à la liste de mauvaise relation de colon1
        relations.get(colon2).add(colon1); //l'énoncé demande des relations symetriques, donc on fait de meme pour colon2
    }

    /*
    Renvoie true si les deux colons en parametres entretiennent une MAUVAISE relation
    sinon false.
    */
    public boolean ontMauvaiseRelation(Colon colon1, Colon colon2) {
        return relations.get(colon1).contains(colon2);
        //        || relations.get(colon2).contains(colon1);
        //On pourrait ajouter le OU mais dans la methode d'ajout de mauvaise relation on gere deja la reciprocité
    }

    
    /*
    permet de récupérer l'ensemble des colons avec lesquels un colon donné en parametre a une relation "ne s'aiment pas"
    */
    public Set<Colon> getVoisins(Colon colon) {
        return relations.get(colon);
    }
    
}
