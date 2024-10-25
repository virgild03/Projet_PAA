
package projet_paa;

import java.util.*;

public class Colonie {
    /*
    Permet de faire une instance de colonie avec : 
    une collection de colons
    une collection de ressources
    la gestion de relation
    */
    private int nbColon; /*Nombre de colon dans la colonie*/

    private ArrayList<Ressource> listeRessource;
    private ArrayList<Colon> listeColons;
    private HashMap<Colon, ArrayList<Ressource>> preferences; /* Hashmap pour rentrer les preferences de chaque colon
    besoin d'une liste pour avoir les preferences de maniere ordonée*/
    
    //La clé est un colon, la valeur est un ensemble de colons avec lesquels ce colon a une relation "ne s'aiment pas".
    //Systeme de dictionnaire avec en clé un colon et en valeur un ensemble de colons avec qui la clé a une relation
    private HashMap<Colon, Set<Colon>> relations; 

    /* <constructeur qui crée la colonie en définissant leur nom en fonction des lettre de l'alphabets selon le nombre
    donner par l'utilisateur
     */
    public Colonie(int nb) {
        this.nbColon = nb;
        this.listeRessource = new ArrayList<>();
        this.preferences = new HashMap<>(); //Constructeur pour la hashmap de preference
        this.relations = new HashMap<>();
        for(int i = 65; i < (i+nbColon); i++) { /* Utilisation de la table ASCII pour nommer les colons*/
            String str = Character.toString((char) i);
            Colon colon = new Colon(str);
            listeColons.add(colon);
            ajouterColon(colon);
            listeRessource.add(new Ressource((listeRessource.size()+1))); /* Création de N ressource autant qu'il y a de colon */
        }
    }
    public Colonie() {
        // Constructeur de la colonie en créant la hashMap (dictionnaire) associant à chaque colon ses relations
        //On ajoute à chaque ensemble "valeur" d'un colon tous les colons avec qui il ne s'entend pas.
        this.nbColon = 0;
        this.listeRessource = new ArrayList<>();
        this.listeColons = new ArrayList<>();
        this.relations = new HashMap<>();
        this.preferences = new HashMap<>(); //Constructeur pour la hashmap de preference
    }

    
    /*
    Si le colon rentré en parametre n'existe pas dans la hashmap relation alors on ajoute une clé colon
    rentré en parametre et on lui attribue un set (ensemble vide) des colons avec qui sa relation est mauvaise
    */
    public void ajouterColon(Colon colon) {
        if (!relations.containsKey(colon)) {
            listeColons.add(colon);
            relations.put(colon, new HashSet<>());
        }
        nbColon++;
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


    /*permet d'échanger les ressources entre deux colons*/
    public void echangeRessource(Colon colon1, Colon colon2) {
        Ressource r = colon1.getRessourceAttribue();
        colon1.setRessourceAttribue(colon2.getRessourceAttribue());
        colon2.setRessourceAttribue(r);
    }

    public void remplirPreferences(char x){
        /*
        A TERMINER
         */
        Scanner scan = new Scanner(System.in);
        for(int i=0; i<preferences.size(); i++)
        {
            //if (/*nom du colon == x*/){
                //colon.get(x) remplir ses preferences;
            //}
        }

    }
}
