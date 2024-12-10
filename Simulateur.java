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
    public static Colonie configurationColonieFichierTxt(String fichier) throws IOException, IllegalArgumentException, ColonException
    {
        //initialisation des variables
        Colonie colonie = new Colonie();

        boolean trueColon = false; //Permet de savoir quand on a fini les lignes sur les colons
        boolean trueRessource = false; //Permet de savoir quand on a fini les lignes sur les ressources
        boolean trueDeteste = false; //Permet de savoir quand on a fini les lignes sur les relations

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) //bufferedReader pour lire fichier
        {
            String ligne; //pour lire la ligne
            
            while((ligne = br.readLine()) != null) //Tant qu'il y a des lignes
            {
                ligne = ligne.trim(); //Pour que la chaine de caractères soit propre même si il y a des espaces en trop

                if (ligne.isEmpty()){
                    continue; //On ignore la ligne si elle est vide
                }

                else if(ligne.startsWith("colon(")) //Si ligne commence par colon
                {
                    if (trueRessource || trueDeteste){ //Vérifie qu'on a les lignes dans le bon ordre
                        throw new IllegalArgumentException("L'ordre des lignes n'est pas respecté");
                    }

                    int start = ligne.indexOf("("); //On prend la partie de la phrase entre "(" et ")."
                    int end = ligne.indexOf(").");
                    if (start == - 1 || end == -1){ //Erreur si "(" ou ")." n'a pas été trouvé
                        throw new IllegalArgumentException("La ligne doit respecter le bon format 1 !");
                    }

                    String nomColon = ligne.substring(start+1, end).trim(); //Prend la chaine de caractère présente entre start et end

                    Colon c = new Colon(nomColon); //creation instance
                    colonie.addListeColons(c); //ajout le nouveau colon à la colonie courante

                }
                
                else if (ligne.startsWith("ressource("))
                {
                    if (trueDeteste){ //Vérifie qu'on a les lignes dans le bon ordre
                        throw new IllegalArgumentException("L'ordre des lignes n'est pas respecté");
                    }

                    trueColon = true; //Vrai si on a bien traité tous les colons

                    int start = ligne.indexOf("(");
                    int end = ligne.indexOf(").");
                    if (start == - 1 || end == -1){
                        throw new IllegalArgumentException("La ligne doit respecter le bon format 2 !");
                    }

                    String nomR = ligne.substring(start+1, end).trim();
                    int numero = colonie.getListeRessource().size() + 1;

                    Ressource r = new Ressource(numero, nomR);
                    colonie.addListeRessource(r);

                }
                
                else if(ligne.startsWith("deteste("))
                {
                    trueRessource = true;

                    int start = ligne.indexOf("(");
                    int end = ligne.indexOf(").");
                    if (start == - 1 || end == -1){
                        throw new IllegalArgumentException("La ligne doit respecter le bon format 3 !");
                    }

                    String str = ligne.substring(start+1, end).trim();
                    String[] noms = str.split(","); //Crée un tableau avec le nom des deux colons
                    if (noms.length != 2){ //Vérifie qu'on a bien 2 noms dans le tableau
                        throw new IllegalArgumentException("La ligne doit respecter le bon format 4 !");
                    }
                    String nom1 = noms[0].trim(); //Récupère les deux noms
                    String nom2 = noms[1].trim();

                    Colon c1 = null;
                    Colon c2 = null;

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
                else if(ligne.startsWith("preferences(")) {

                    trueDeteste = true;

                    int start = ligne.indexOf("(");
                    int end = ligne.indexOf(").");
                    if (start == - 1 || end == -1){
                        throw new IllegalArgumentException("La ligne doit respecter le bon format 5 !");
                    }

                    String str = ligne.substring(start+1, end).trim();

                    String[] noms = str.split(",");
                    if (noms.length < 2){
                        System.out.println("a : " + noms[0]);
                        System.out.println("a : " + noms[1]);
                        throw new IllegalArgumentException("La ligne doit respecter le bon format 6 !");
                    }

                    String nomColon = noms[0].trim();
                    Colon c = null;
                    for (Colon col : colonie.getListeColons()){
                        if (col.getNomColon().equals(nomColon)){ //Si le colon existe on l'enregistre dans "c"
                            c = col;
                            break;
                        }
                    }

                    if (c == null){ //Erreur si le colon n'existe pas
                        throw new ColonException("Colon introuvable !");
                    }

                    //Pour chaque ressource de la ligne
                    for (int i = 1; i < noms.length; i++){ //On commence à i = 1 car i = 0 correspond au nom du colon
                        String nomR = noms[i].trim();
                        Ressource ressourceActuelle = null;
                        for (Ressource r : colonie.getListeRessource()){ //Si la ressource existe alors elle est assigné à "ressourceActuelle"
                            if (r.getNomRessource() != null && r.getNomRessource().equals(nomR)){
                                ressourceActuelle = r;
                                break;
                            }
                        }
                        if (ressourceActuelle == null){ //Erreur si la ressource n'existe pas
                            throw new IllegalArgumentException("La ressource actuelle n'existe pas !");
                        }
                        c.setPreference(ressourceActuelle); //Ajout de la ressource aux preferences du colon
                    }
                }
                else {
                    throw new IllegalArgumentException("Cette ligne n'est pas dans le bon format !");
                }
            }
        }
        return colonie;
    }


    /*
       Méthode qui renvoie une HashMap<Colon, Ressource> telle que:
       - Pour chaque colon, la ressource associée dans la HashMap correspond
         à une affectation minimisant le coût de jalousie.
       - Chaque ressource est affectée à un unique colon.
       - Lorsqu'on teste une nouvelle ressource pour un colon, on procède à un échange
         avec le colon qui la détient déjà (si besoin).
   */
    public static HashMap<Colon, Ressource> affectationAutomatique(Colonie colonie) throws ColonException {
        ArrayList<Colon> listeColons = colonie.getListeColons();
        ArrayList<Ressource> listeRessources = colonie.getListeRessource();

        // Affectation initiale : on associe chaque colon à la ressource correspondante par l'indice
        HashMap<Colon, Ressource> affectationLocale = new HashMap<>();
        for (int i = 0; i < listeColons.size(); i++) {
            Colon colonCourant = listeColons.get(i);
            Ressource ressourceInitiale = listeRessources.get(i);
            affectationLocale.put(colonCourant, ressourceInitiale);
            colonCourant.setRessourceAttribue(ressourceInitiale);
        }

        int coutTemporaire = calculerCout(colonie);

        // Tentative d'améliorer l'affectation
        for(int i = 0; i < listeColons.size(); i++) {
            for(int j = 0; j < listeRessources.size(); j++) {
                Colon colonCourant = listeColons.get(i);
                Ressource ressourceCourante = affectationLocale.get(colonCourant);
                Ressource ressourceCandidate = listeRessources.get(j);

                // On ne tente l'échange que si la ressource candidate est différente de la ressource courante du colon
                if (!ressourceCandidate.equals(ressourceCourante)) {
                    // Trouver le colon qui possède actuellement la ressource candidate
                    Colon autreColon = null;

                    /*
                    boucle pour rechercher autreColon à qui est associé la ressource ressourceCandidate.
                    Nouveau concept : Map.Entry.
                    C'est une interface en java qui permet de parcourir les clé-valeurs d'une hashmap.
                    entrySet() est une méthode de Map qui retourne un ensemble (de type Set) de toutes les paires clé-valeur présentes dans la hashmap affectationLocale.
                    getKey() retourne la clé : un colon dans notre cas.
                    getValue() retourne une valeur : une ressource dans notre cas.
                     */
                    for (Map.Entry<Colon, Ressource> entry : affectationLocale.entrySet()) //Pour toutes les "clé-valeurs" dans affectationLocale
                    {
                        if (entry.getValue().equals(ressourceCandidate))  //Si une valeur de "clé-valeurs" = ressourceCandidate
                        {
                            autreColon = entry.getKey(); //autreColon devient la clé qui correspond à la valeur ressourceCandidate
                            break; //évitons de boucler pour rien tout de meme, la planète a des ressources limitées !
                        }
                    }

                    // S'il existe un autre colon détenant déjà la ressourceCandidate, on tente un échange
                    if (autreColon != null && !autreColon.equals(colonCourant)) {
                        // Sauvegarde de l'état initial avant l'échange
                        Ressource ancienneRessourceAutreColon = affectationLocale.get(autreColon);

                        // Échange : le colon courant prend la ressource candidate, l'autre colon prend la ressource courante
                        affectationLocale.put(colonCourant, ressourceCandidate);
                        colonCourant.setRessourceAttribue(ressourceCandidate);

                        affectationLocale.put(autreColon, ressourceCourante);
                        autreColon.setRessourceAttribue(ressourceCourante);

                        // Calcul du coût après l'échange
                        int nouveauCout = calculerCout(colonie);

                        // Si le coût ne baisse pas on revient à l'état initial (échange inverse).
                        if (nouveauCout >= coutTemporaire) {
                            affectationLocale.put(colonCourant, ressourceCourante);
                            colonCourant.setRessourceAttribue(ressourceCourante);

                            affectationLocale.put(autreColon, ancienneRessourceAutreColon);
                            autreColon.setRessourceAttribue(ancienneRessourceAutreColon);
                        } else {
                            // Le coût s'est amélioré, on met à jour coutTemporaire
                            coutTemporaire = nouveauCout;
                        }
                    }
                }
            }
        }

        // La HashMap affectationLocale contient maintenant l'affectation minimisée
        return affectationLocale;
    }



    public static void main(String[] args) throws ColonException {
        //Ajout de la prise en compte de ColonException pour les methodes appelées ci dessous.
        affectation = new HashMap<>(); //créé la hashmap

        Scanner sc = new Scanner(System.in);
        Colonie colonie = null;

        while (true) { //menu pour l'utilisateur
            System.out.println("\nQue souhaitez-vous faire ? ");
            System.out.println("1. Construction de la colonie via un fichier");
            System.out.println("2. Construction de la colonie à la main");

            int choix = sc.nextInt(); //lit le choix de l'utilisateur
            sc.nextLine();

            if (choix == 1) {
                System.out.println("Entrez le chemin du fichier de configuration de la colonie : ");
                String nomFichier = sc.nextLine();

                try {
                    colonie = configurationColonieFichierTxt(nomFichier);
                    break;
                } catch (Exception e) {
                    System.out.println("Erreur lors de la configuration de la colonie : " + e.getMessage());
                }

            } else if (choix == 2) {
                System.out.println("Nombre de colons dans la colonie ? ");
                int nbColons = sc.nextInt(); //lit le nombre de colons
                sc.nextLine();
                colonie = new Colonie(nbColons);//initialise une colonie
                ressourcesDisponibles = new ArrayList<>(colonie.getListeRessource()); //créé la liste des ressources dispo.

                while (true) { //menu pour l'utilisateur
                    System.out.println("\nQue souhaitez-vous faire ? ");
                    System.out.println("1. Ajouter une relation entre 2 colons");
                    System.out.println("2. Ajouter les préférences d'un colon");
                    System.out.println("0. Fin de la configuration");

                    int choix2 = sc.nextInt(); //lit le choix de l'utilisateur
                    sc.nextLine();

                    if (choix2 == 1) { //ajoute une mauvaise relation entre 2 colon
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

                    } else if (choix2 == 2) { //ajoute les preferences d'un colon (du préféré au pire)
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

                    } else if (choix2 == 0) { //quitte le menu
                        //SI Tous les colons ont des preferences on fait le break, sinon message qu'il manque des preferences

                        ArrayList<String> nomColonRestant = new ArrayList<>(); // Permet de renvoyer les noms des colons sans préférence

                        //Parcours de la liste de colon pour vérifier leur preference
                        for (int i = 0; i < nbColons; i++) {
                            if (colonie.getListeColons().get(i).getPreference().isEmpty()) {
                                nomColonRestant.add(colonie.getListeColons().get(i).getNomColon()); // Si pas de preference pour le colon
                            }
                        }

                        if (nomColonRestant.isEmpty()) {
                            break;
                        } else {
                            System.out.println("\nLes colons  " + nomColonRestant + " n'ont pas encore reçu de préférence, obligatoire pour terminer la configuration !!!");
                        }
                    } else {
                        System.out.println("Choix invalide.");
                    }
                }
            }
        }
        //Fin de la construction de la colonie





        ressourcesDisponibles = new ArrayList<>(colonie.getListeRessource());
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
        //Les lignes de codes precedemment placées ici sont déplacées plus bas dans une methode calculerCout(c);
        cout = calculerCout(colonie);


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

/*

CODE DUPLIQUé JE NE SAIS POURQUOI ON EST JUSTE DES DEBILES PARDON

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
                        colon.setJaloux(true); //Le colon devient jaloux que si un autre colon avec qui il a une mauvaise relation,
                        //a une ressource qu'il aurait préféré avoir
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

 */
    }
    public static int calculerCout(Colonie colonie) throws ColonException {
        int cout = 0; // Initialisation du coût à 0

        for (Colon colon : colonie.getListeColons()) { // Pour chaque colon de la colonie
            Set<Colon> mauvaisesRelations = colonie.getVoisins(colon); // Récupère les mauvaises relations du colon
            Ressource ressourceAttribuee = colon.getRessourceAttribue(); // Récupère la ressource attribuée au colon

            List<Ressource> preferences = colon.getPreference(); // Récupère les préférences du colon
            int indexAttribue = preferences.indexOf(ressourceAttribuee); // Indice de la ressource attribuée dans les préférences

            // Si la ressource attribuée ne fait pas partie des préférences
            if (indexAttribue == -1) {
                indexAttribue = preferences.size(); // Indice maximal
            }

            // Pour chaque ressource préférée non attribuée
            for (int i = 0; i < indexAttribue; i++) {
                Ressource ressourcePreferee = preferences.get(i);

                // Vérifier si un rival (mauvaise relation) a obtenu cette ressource préférée
                for (Colon rival : mauvaisesRelations) {
                    if (rival.getRessourceAttribue() != null &&
                            rival.getRessourceAttribue().equals(ressourcePreferee)) {

                        cout++; // Incrémente le coût
                        colon.setJaloux(true); // Le colon devient jaloux
                        break; // Arrête la vérification pour cette ressource préférée
                    }
                }
            }
        }
        return cout; // Retourne le coût total
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
