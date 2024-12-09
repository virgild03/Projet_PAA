package projet_paa;

import java.util.*;
import java.io.*;

public class Simulateur {

    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.
    public  static HashMap<Colon, Ressource> affectation; //association des ressources aux colon avec un Dictionnaire.
    public static ArrayList<Ressource> ressourcesDisponibles; //liste des ressources dispo
    
    public static void affectationNaive(Colon c) {
        int objetPrefDispo = 0;
        while (!affectation.containsKey(c) && objetPrefDispo < c.getPreference().size()) {
        /*Tant que le colon n'a pas reçu
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
    
    /*
    methode pour la partie 2 du projet.
    Sert à initialiser un colon via un fichier txt donné en parametre.
    nom : pour le nom du colon à récuperer
    nomR : pour le nom de la ressource
    nom1 & nom2 : pour lire le nom des colons et ajouter mauvaise relation.
    c1 & c2 : pour affecter instance de colons avec les noms : nom1 et nom2.
    */
    public static void configurationColonieFichierTxt(String fichier) throws IOException, IllegalArgumentException, ColonException
    {
        //initialisation des variables
        Colonie colonie = new Colonie();
        String nom = null; //nom du colon à la lecture du fichier txt
        String nomR = null; //nom ressource
        String nom1 = null; //nom à recuperer pour les colons et prendre en compte leur mauvaise relation
        String nom2 = null; //nom à recuperer pour les colons et prendre en compte leur mauvaise relation
        Colon c1 = null; //instance pour les colons qui auront le nom nom1 ou nom2
        Colon c2 = null; //instance pour les colons qui auront le nom nom1 ou nom2
        int compteurRessource = 1; //pour affecter un numero de ressource car la classe Ressource implique un numero. Ajout egalement d'un nom de ressource pour la partie 2 du projet.
        Colon c; //pour manipuler la création de colon
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) //bufferedReader pour lire fichier
        {
            String ligne; //pour lire la ligne
            
            while((ligne = br.readLine()) != null) //Tant qu'il y a des lignes 
            {
                if(ligne.startsWith("colon")) //Si ligne commence par colon
                {
                    if(nom != null) //Vérifie si nom est déjà assigné
                    {
                        throw new IllegalArgumentException("Il ne peut pas y avoir deux lignes pour le nom");
                    }
                    nom = ligne.split("()")[1].trim(); //assigne le nom lu par le br à nom pour creer une instance
                    c = new Colon(nom); //creation instance
                    colonie.addListeColons(c); //ajout le nouveau colon à la colonie courante
                    nom = null; //désaffecte le nom pour pouvoir passer à la ligne suivante
                }
                
                if(ligne.startsWith("ressource"))
                {
                    if(nomR != null)
                    {
                        throw new IllegalArgumentException("Il ne peut pas y avoir deux lignes pour le nom de ressource");
                    }
                    nomR = ligne.split("()")[1].trim();
                    Ressource r = new Ressource(compteurRessource, nomR);
                    colonie.addListeRessource(r);
                    compteurRessource ++;
                    nomR = null;
                }
                
                if(ligne.startsWith("deteste"))
                {
                    if(nom1 != null || nom2 != null)
                    {
                        throw new IllegalArgumentException("Il ne peut pas y avoir deux lignes pour les noms de colons qui se detestent");
                    }
                    nom1 = ligne.split("(,)")[1].trim();
                    nom2 = ligne.split("(,)")[2].trim();
                    for(Colon colon : colonie.getListeColons())
                    {
                        if(nom1.equals(colon.getNomColon()))
                        {
                            c1 = colon;
                        }
                        if(nom2.equals(colon.getNomColon()))
                        {
                            c2 = colon;
                        }
                    }
                    if(c1 != null && c2 != null)
                    {
                        colonie.ajouterMauvaiseRelation(c1, c2);
                    }
                    else
                    {
                        throw new ColonException("Pas de colon à ce nom enregistré");
                    }
                    
                }
                if(ligne.startsWith("preferences")) {
                    //Remplir avec les preferences données la ArrayList de preferences du nom colon donné en premier apres la parenthese.
                }
                
                //Gerer les cas ou il y a un nombre de colons differents de celui des ressources etc...
            }
        }
    }



    public static void main(String[] args) throws ColonException {
        //Ajout de la prise en compte de ColonException pour les methodes appelées ci dessous.
        affectation = new HashMap<>(); //créé la hashmap
        Scanner sc = new Scanner(System.in);

        System.out.println("Nombre de colons dans la colonie ? ");
        int nbColons = sc.nextInt(); //lit le nombre de colons
        sc.nextLine();
        Colonie colonie = new Colonie(nbColons);//initialise une colonie
        ressourcesDisponibles = new ArrayList<>(colonie.getListeRessource()); //créé la liste des ressources dispo.

        while (true) { //menu pour l'utilisateur
            System.out.println("\nQue souhaitez-vous faire ? ");
            System.out.println("1. Ajouter une relation entre 2 colons");
            System.out.println("2. Ajouter les préférences d'un colon");
            System.out.println("0. Fin de la configuration");

            int choix = sc.nextInt(); //lit le choix de l'utilisateur
            sc.nextLine();

            if (choix == 1) { //ajoute une mauvaise relation entre 2 colon
                System.out.println("Nom du premier colon :");
                String nom1 = sc.nextLine(); //lit le nom du premier colon
                System.out.println("Nom du second colon :");
                String nom2 = sc.nextLine(); //lit le nom du second colon

                Colon colon1 = trouverColon(colonie, nom1); //récupère le premier colon
                Colon colon2 = trouverColon(colonie, nom2); //récupère le second colon

                if (colon1 != null && colon2 != null) { //vérifie que les 2 colons existent
                    colonie.ajouterMauvaiseRelation(colon1, colon2);
                    System.out.println("Relation ajoutée entre " + nom1 + " et " + nom2);
                } else { //sinon renvoie un message d'erreur
                    System.out.println("L'un des colons n'a pas été trouvé.");
                }

            } else if (choix == 2) { //ajoute les preferences d'un colon (du préféré au pire)
                System.out.println("Nom du colon :");
                String nomColon = sc.nextLine(); //lit le nom du colon
                Colon colon = trouverColon(colonie, nomColon); //récupère le colon

                if (colon != null) { //si le colon existe
                    System.out.println("Entrez les numéros de ressources préférées de " + colon.getNomColon() + " en séparant chaque numéro par des espaces :");
                    String[] ressourcesPref = sc.nextLine().split(" "); //crée un tableau avec les préférences du colon

                    for (String resPref : ressourcesPref) {
                        try {
                            int numRes = Integer.parseInt(resPref); //convertit le numéro de la ressource de String à entier
                            Ressource ressource = trouverRessource(colonie, numRes); //récupère la ressource associée
                            if (ressource != null) { //si la ressource existe
                                colon.setPreference(ressource); //met à jour la préférence du colon
                            } else { //sinon renvoie un message d'erreur
                                System.out.println("Ressource " + numRes + " non trouvée.");
                            }
                        } catch (NumberFormatException e) { //si le numéro entré n'est pas valide
                            System.out.println(resPref + " n'est pas un numéro valide.");
                        }
                    }
                } else { //sinon renvoie un message d'erreur
                    System.out.println("Colon " + nomColon + " non trouvé.");
                }

            } else if (choix == 0) { //quitte le menu
                //SI Tous les colons ont des preferences on fait le break, sinon message qu'il manque des preferences

                ArrayList<String> nomColonRestant = new ArrayList<>(); // Permet de renvoyer les noms des colons sans préférence

                //Parcours de la liste de colon pour vérifier leur preference
                for(int i=0; i< nbColons;i++){
                   if(colonie.getListeColons().get(i).getPreference().isEmpty()){
                       nomColonRestant.add(colonie.getListeColons().get(i).getNomColon()); // Si pas de preference pour le colon
                    }
               }

                if(nomColonRestant.isEmpty()){
                    break;
                } else {
                    System.out.println("\nLes colons  "+nomColonRestant +" n'ont pas encore reçu de préférence, obligatoire pour terminer la configuration !!!");
                }
            } else {
                System.out.println("Choix invalide.");
            }
        }
        //Fin de la construction de la colonie

        // Affectation des ressources
        /*
        this.ressourcesDisponibles = new ArrayList<>(colonie.getListeRessource());
        System.out.println("\nAffectation des ressources");
        System.out.println("Entrez l'ordre des colons (un nom par ligne) pour l'affectation :");
        

        */
        
        System.out.println("\nAffectation des ressources");
        System.out.println("Entrez l'ordre des colons (un nom par ligne) pour l'affectation :");

        for (int i = 0; i < colonie.getNbColon(); i++) {
            String nomColon = sc.nextLine(); //lit le nom du colon
            Colon colon = trouverColon(colonie, nomColon); //récupère le colon associé

            if (colon != null) { //si le colon existe
                affectationNaive(colon); //effectue l'affectation naïve
                System.out.println("Ressource attribuée à " + colon.getNomColon() + " : " + colon.getRessourceAttribue());
            } else { //sinon renvoie un message d'erreur
                System.out.println("Colon " + nomColon + " non trouvé.");
                try{
                    /*
                    Gestion de l'erreur si un colon rentré est nul.
                    */
                    throw new ColonException("Colon est nul !");                
                }
                catch(ColonException e){
                    System.out.println("Colon nul : " + e.getMessage());
                }
                i--; //l'utilsateur peut redonner un nom valable
            }
        }

        // Calcul du cout
        cout = 0;
        for (Colon colon : colonie.getListeColons()) { //pour chaque colon de la colonie
            Set<Colon> mauvaisesRelations = colonie.getVoisins(colon); //récupère les mauvaises relations du colon
            Ressource ressourceAttribuee = colon.getRessourceAttribue(); //récupère la ressource attribuée au colon

            List<Ressource> preferences = colon.getPreference(); //récupère les préférences du colon
            int indexAttribue = preferences.indexOf(ressourceAttribuee); //récupère l'indice de la ressource attribuée dans la liste des préférences

            if (indexAttribue == -1) { //si la ressource attribuée ne fait pas parti des préférences du colon
                indexAttribue = preferences.size(); //retourne l'indice maximal
            }

            //pour chaque ressource préférée que le colon n'a pas eu, on vérifie si un rival l'a eu
            for (int i = 0; i < indexAttribue; i++) {
                Ressource ressourcePreferee = preferences.get(i);
                for (Colon rival : mauvaisesRelations) {
                    if (rival.getRessourceAttribue() != null && rival.getRessourceAttribue().equals(ressourcePreferee)) {
                        cout++;
                        colon.setJaloux(true); /*Le colon devient jaloux que si un autre colon avec qui il a une mauvaise relation,
                        a une ressource qu'il aurait préféré avoir*/
                        break;
                    }
                }
            }
        }

        System.out.println("\nLe coût de jalousie est : " + cout);
        System.out.println("Les colons jaloux sont :");
        for (Colon colon : colonie.getListeColons()) {
            if (colon.isJaloux()) {
                System.out.println(colon.getNomColon());

                System.out.println("est jaloux de :");
                Set<Colon> voisins = colonie.getVoisins(colon);
                List<String> nomsVoisins = new ArrayList<>();
                for (Colon voisin : voisins) {
                    nomsVoisins.add(voisin.getNomColon());
                }
                System.out.println(nomsVoisins);
            }
        }

        while (true) { //menu pour echanger des ressources
            System.out.println("\nSouhaitez vous echangez des ressources ? ");
            System.out.println("1. Oui");
            System.out.println("0. Non");

            int choix = sc.nextInt(); //lit le choix de l'utilisateur
            sc.nextLine();

            if (choix == 1) {
                System.out.println("Nom du premier colon :");
                String nom1 = sc.nextLine(); //lit le nom du premier colon
                System.out.println("Nom du second colon :");
                String nom2 = sc.nextLine(); //lit le nom du second colon

                Colon colon1 = trouverColon(colonie, nom1);
                Colon colon2 = trouverColon(colonie, nom2);

                if (colon1 != null && colon2 != null) { //vérifie que les 2 colons existent
                    colonie.echangeRessource(colon1, colon2);
                    System.out.println("Ressources échangéss entre " + nom1 + " et " + nom2);
                } else { //sinon renvoie un message d'erreur
                    System.out.println("L'un des colons n'a pas été trouvé.");
                }

            } else if (choix == 0) { //quitte le menu
                break;
            } else {
                System.out.println("Choix invalide.");
            }
        }


        // Calcul du cout
        cout = 0;
        for (Colon colon : colonie.getListeColons()) { //pour chaque colon de la colonie
            Set<Colon> mauvaisesRelations = colonie.getVoisins(colon); //récupère les mauvaises relations du colon
            Ressource ressourceAttribuee = colon.getRessourceAttribue(); //récupère la ressource attribuée au colon

            List<Ressource> preferences = colon.getPreference(); //récupère les préférences du colon
            int indexAttribue = preferences.indexOf(ressourceAttribuee); //récupère l'indice de la ressource attribuée dans la liste des préférences

            if (indexAttribue == -1) { //si la ressource attribuée ne fait pas parti des préférences du colon
                indexAttribue = preferences.size(); //retourne l'indice maximal
            }

            //pour chaque ressource préférée que le colon n'a pas eu, on vérifie si un rival l'a eu
            for (int i = 0; i < indexAttribue; i++) {
                Ressource ressourcePreferee = preferences.get(i);
                for (Colon rival : mauvaisesRelations) {
                    if (rival.getRessourceAttribue() != null && rival.getRessourceAttribue().equals(ressourcePreferee)) {
                        cout++;
                        colon.setJaloux(true); /*Le colon devient jaloux que si un autre colon avec qui il a une mauvaise relation,
                        a une ressource qu'il aurait préféré avoir*/
                        break;
                    }
                }
            }
        }

        System.out.println("\nLe coût de jalousie est : " + cout);
        System.out.println("Les colons jaloux sont :");
        for (Colon colon : colonie.getListeColons()) {
            if (colon.isJaloux()) {
                System.out.println(colon.getNomColon());

                System.out.println("est jaloux de :");
                Set<Colon> voisins = colonie.getVoisins(colon);
                List<String> nomsVoisins = new ArrayList<>();
                for (Colon voisin : voisins) {
                    nomsVoisins.add(voisin.getNomColon());
                }
                System.out.println(nomsVoisins);
            }
        }
    }

    //vérifie si un colon appartient bien à la colonie
    private static Colon trouverColon(Colonie colonie, String nom) {
        for (Colon c : colonie.getListeColons()) {
            if (c.getNomColon().equalsIgnoreCase(nom)) { //ignore les différences de majuscules et minuscules
                return c;
            }
        }
        return null;
    }

    //vérifie si une ressource appartient bien à la liste des ressources
    private static Ressource trouverRessource(Colonie colonie, int numero) {
        for (Ressource r : colonie.getListeRessource()) {
            if (r.getNumeroRessource() == numero) {
                return r;
            }
        }
        return null;
    }
}
