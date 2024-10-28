
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

    //La clé est un colon, la valeur est un ensemble de colons avec lesquels ce colon a une relation "ne s'aiment pas".
    //Systeme de dictionnaire avec en clé un colon et en valeur un ensemble de colons avec qui la clé a une relation
    private HashMap<Colon, Set<Colon>> relations; 

    /* <constructeur qui crée la colonie en définissant leur nom en fonction des lettre de l'alphabets selon le nombre
    donner par l'utilisateur
     */
    public Colonie(int nb) {
        this.nbColon = nb;
        this.listeRessource = new ArrayList<>();
        this.listeColons = new ArrayList<>();
        this.relations = new HashMap<>();

        for (int i = 0; i < nbColon; i++) {
            String nomColon = genererNomColon(i);
            Colon colon = new Colon(nomColon);
            listeColons.add(colon);
            relations.put(colon, new HashSet<>());
            Ressource ressource = new Ressource(i + 1);
            listeRessource.add(ressource);
        }
    }

    private String genererNomColon(int index) {
        StringBuilder nom = new StringBuilder();
        index++;

        while (index > 0) {
            index--;
            char lettre = (char) ('A' + (index % 26)); //code ASCII de 'A' + index < 26 (ex: 'A' + 1 = 'B')
            nom.insert(0, lettre); //ajoute la lettre à "nom"
            index = index / 26; //permet d'inserer une 2eme lettre si index > 26
        }
        return nom.toString();
    }
    /*public Colonie() {
        // Constructeur de la colonie en créant la hashMap (dictionnaire) associant à chaque colon ses relations
        //On ajoute à chaque ensemble "valeur" d'un colon tous les colons avec qui il ne s'entend pas.
        this.nbColon = 0;
        this.listeRessource = new ArrayList<>();
        this.listeColons = new ArrayList<>();
        this.relations = new HashMap<>();
    }*/
    public ArrayList<Ressource> getListeRessource() {
        return listeRessource;
    }

    public int getNbColon() {
        return nbColon;
    }

    public ArrayList<Colon> getListeColons() {
        return listeColons;
    }

    /*
    Si le colon rentré en parametre n'existe pas dans la hashmap relation alors on ajoute une clé colon
    rentré en parametre et on lui attribue un set (ensemble vide) des colons avec qui sa relation est mauvaise
    */
    /*public void ajouterColon(Colon colon) {
        if (!relations.containsKey(colon)) {
            listeColons.add(colon);
            relations.put(colon, new HashSet<>());
        }
        nbColon++;
    }*/

    /*
    permet d'ajouter une MAUVAISE relation entre deux colons entrés en parametre
    
    */
    public void ajouterMauvaiseRelation(Colon colon1, Colon colon2) {
        /*ajouterColon(colon1); //Permet d'ajouter le colon1 à la colonie si ce nest pas deja fait, ne fait rien sinon
        ajouterColon(colon2); //De meme pour le colon2*/ //test louis
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

    /*public void remplirPreferences(char x){
        Scanner scan = new Scanner(System.in); //Créé un scanner.
        Colon colon = null;

        // Trouver le colon avec le nom correspondant au caractère x
        for (Colon c : listeColons) { //Iteration implicite avec for each
            if (c.getNomColon().charAt(0) == x) { //Cherche le colon avec le nom (lettre entrée en parametre)
                colon = c; //colon = colon trouvé avec le nom x
                break; //stop la boucle
            }
        }

        if (colon != null) { //on gère le cas où le colon n'est pas trouvé
            ArrayList<Ressource> prefs = new ArrayList<>();
            System.out.println("Entrez les préférences de " + colon.getNomColon() + " :"); //print d'indication


            //for (int i = 0; i < nbColon; i++) { // Boucle qui passe nbColon fois

                int choix = scan.nextInt(); // Scanner où l'utilisateur entre son choix

                Ressource ressource = null; // Initialise ressource à null
                for (Ressource r : listeRessource) { // For each r in listeRessource
                    if (r.getNumeroRessource() == choix) { // Si r est la ressource rentrée par l'utilisateur
                        ressource = r; // Ressource devient le choix de l'utilisateur
                        break;
                    }
                }
                if (ressource != null) { // Si cette même ressource n'est pas null
                    prefs.add(ressource); // Ajoute la ressource à la liste prefs qui sera ajoutée en valeur pour le colon
                } else {
                    System.out.println("Ressource non trouvée. Veuillez entrer un numéro valide."); // Cas où la ressource n'est pas bonne
                }
            //}
            colon.getPreference().addAll(prefs); //ajout de prefs à preference de colon
        } else {
            System.out.println("Colon non trouvé.");
        }
    }*/
}
