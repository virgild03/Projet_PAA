

package projet_paa.src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

        for (int i = 0; i < nbColon; i++) { //pour chaque colon
            String nomColon = genererNomColon(i); //génère un nom
            Colon colon = new Colon(nomColon); //initialise le colon
            listeColons.add(colon); //ajoute le colon à la liste
            relations.put(colon, new HashSet<>()); //initialise les relations du colon
            Ressource ressource = new Ressource(i + 1); //initialise une ressource avec un numéro
            listeRessource.add(ressource); //ajoute la ressource créée à la liste des ressources
        }
    }

    public Colonie()
    {
        this.listeRessource = new ArrayList<>();
        this.listeColons = new ArrayList<>();
        this.relations = new HashMap<>();
    }
    private String genererNomColon(int index) { //génère un nom pour un colon (A, B, ...)
        StringBuilder nom = new StringBuilder();
        index++;

        while (index > 0) {
            index--;
            char lettre = (char) ('A' + (index % 26)); //code ASCII de 'A' + index < 26 (ex: 'A' + 1 = 'B')
            nom.insert(0, lettre); //ajoute la lettre à "nom"
            index = index / 26; //permet d'inserer une 2eme lettre si index > 26
        }
        return nom.toString(); //convertit "nom" en String
    }

    public ArrayList<Ressource> getListeRessource() {
        return listeRessource;
    }
    
    public void addListeRessource(Ressource r)
    {
        listeRessource.add(r);
    }

    public int getNbColon() {
        return nbColon;
    }

    public  ArrayList<Colon> getListeColons() {
        return listeColons;
    }
    
    public void addListeColons(Colon c)
    {
        listeColons.add(c);
        relations.put(c, new HashSet<>());
        nbColon = listeColons.size(); //Fait en sorte que nbColon est bien mis à jour
    }


    /*
    permet d'ajouter une MAUVAISE relation entre deux colons entrés en parametre

    */
    public void ajouterMauvaiseRelation(Colon colon1, Colon colon2) {
        relations.get(colon1).add(colon2); //ajoute le colon2 à la liste de mauvaise relation de colon1
        relations.get(colon2).add(colon1); //l'énoncé demande des relations symetriques, donc on fait de meme pour colon2
    }



    /*
    Renvoie true si les deux colons en parametres entretiennent une MAUVAISE relation
    sinon false.
    */
    public boolean ontMauvaiseRelation(Colon colon1, Colon colon2) throws ColonException {
        if (colon1 != null && colon2 != null)
        {
            return relations.get(colon1).contains(colon2);
        }
        else
        {
            //Gestion du cas où le colon n'existe pas.
            throw new ColonException("l'un des colons est inexistant");
        }
        
    }


    /*
    permet de récupérer l'ensemble des colons avec lesquels un colon donné en parametre a une relation "ne s'aiment pas"
    */
    public Set<Colon> getVoisins(Colon colon) throws ColonException{
        if (colon != null)
        {
            return relations.get(colon);
        }
        else 
        {
            //Gestion du cas où le colon n'existe pas.
            throw new ColonException("le colon est inexistant.");
        }
    }


    /*permet d'échanger les ressources entre deux colons*/
    public void echangeRessource(Colon colon1, Colon colon2) throws ColonException {
        if (colon1 != null && colon2 != null)
        {
            Ressource r = colon1.getRessourceAttribue();
            colon1.setRessourceAttribue(colon2.getRessourceAttribue());
            colon2.setRessourceAttribue(r);
        }
        else
        {
            //Gestion du cas où le colon n'existe pas.
            throw new ColonException("l'un des colons est inexistant");
        }
    }


    //vérifie si un colon appartient bien à la colonie
    public Colon trouverColon(String nom) {
        for (Colon c : getListeColons()) {
            if (c.getNomColon().equalsIgnoreCase(nom)) { //ignore les différences de majuscules et minuscules
                return c;
            }
        }
        return null;
    }


    //vérifie si une ressource appartient bien à la liste des ressources
    public Ressource trouverRessource(int numero) {
        for (Ressource r : getListeRessource()) {
            if (r.getNumeroRessource() == numero) {
                return r;
            }
        }
        return null;
    }


    public int calculerCout() throws ColonException {
        int cout = 0;

        for (Colon colon : getListeColons()) {
            Set<Colon> mauvaisesRelations = getVoisins(colon);
            Ressource ressourceAttribuee = colon.getRessourceAttribue();
            List<Ressource> preferences = colon.getPreference();

            int indexAttribue = preferences.indexOf(ressourceAttribuee);
            if (indexAttribue == -1) {
                indexAttribue = preferences.size();
            }

            boolean estJaloux = false;
            for (int i = 0; i < indexAttribue && !estJaloux; i++) {
                Ressource ressourcePreferee = preferences.get(i);

                for (Colon rival : mauvaisesRelations) {
                    if (rival.getRessourceAttribue() != null &&
                            rival.getRessourceAttribue().equals(ressourcePreferee)) {

                        cout++;
                        colon.setJaloux(true);
                        estJaloux = true;
                        break; // Sort de la boucle 'rival'
                    }
                }
            }
        }

        return cout; // Retourne le coût total
    }

    public void sauvegarde(){

        if(getListeColons().getFirst().getRessourceAttribue()==null){
            System.out.println("Il faut affecter les ressources aux colons avant de pouvoir sauvegarder !!!");
        }
        else {
            System.out.println("\nChemin du fichier dans lequel il faut sauvegarder la colonie ? ");
            Scanner sc = new Scanner(System.in);
            String chemin = sc.nextLine();

            try( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(chemin)))) {
                pw.println("Cout="+ this.calculerCout());
                for(Colon c : getListeColons()){

                    System.out.println(c.getNomColon()+" : "+c.getRessourceAttribue());
                    pw.println(c.getNomColon()+":"+c.getRessourceAttribue());
                }
            }
            catch(IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (ColonException e) {
                throw new RuntimeException(e);
            }
            System.out.println("La sauvegarde à été effectuée !!!");
        }
    }
}
