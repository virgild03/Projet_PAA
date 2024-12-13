package projet_paa.src;

import java.util.*;
import java.io.*;

public class Simulateur {
	
    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.
    
    /*
    methode pour la partie 2 du projet.
    Sert à initialiser un colon via un fichier txt donné en parametre.
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


    public static void main(String[] args) throws ColonException {
        //Ajout de la prise en compte de ColonException pour les methodes appelées ci dessous.
        Scanner sc = new Scanner(System.in);
        Colonie colonie = null;

        if(args.length>0){

            try {
                colonie = configurationColonieFichierTxt(args[0]);
            } catch (Exception e) {
                System.out.println("Erreur lors de la configuration de la colonie : " + e.getMessage());
            }

            while (true) { //menu pour l'utilisateur
                System.out.println("\nQue souhaitez-vous faire ? ");
                System.out.println("1) Résolution automatique");
                System.out.println("2) Sauvegarder la solution actuelle");
                System.out.println("3) Fin");

                int choix3 = sc.nextInt();
                sc.nextLine();
                if (choix3 == 1 && colonie != null) {
                    Affectation.init(colonie); 
                    Affectation.affectationAutomatique(colonie);
                }
                if (choix3 == 2 && colonie != null) {
                    Affectation.sauvegarde(Affectation.affectation);
                }
                if (choix3 == 3 && colonie != null) {
                    break;
                }
            }

        } else{
            System.out.println("Nombre de colons dans la colonie ? ");
            int nbColons = sc.nextInt(); //lit le nombre de colons
            sc.nextLine();
            colonie = new Colonie(nbColons);//initialise une colonie

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

                    Colon colon1 = colonie.trouverColon(nom1); //récupère le premier colon
                    Colon colon2 = colonie.trouverColon(nom2); //récupère le second colon

                    if (colon1 != null && colon2 != null) { //vérifie que les 2 colons existent
                        colonie.ajouterMauvaiseRelation(colon1, colon2);
                        System.out.println("Relation ajoutée entre " + nom1 + " et " + nom2);
                    } else { //sinon renvoie un message d'erreur
                        System.out.println("L'un des colons n'a pas été trouvé.");
                    }

                } else if (choix2 == 2) { //ajoute les preferences d'un colon (du préféré au pire)
                    System.out.println("Nom du colon :");
                    String nomColon = sc.nextLine(); //lit le nom du colon
                    Colon colon = colonie.trouverColon(nomColon); //récupère le colon

                    if (colon != null) { //si le colon existe
                        System.out.println("Entrez les numéros de ressources préférées de " + colon.getNomColon() + " en séparant chaque numéro par des espaces :");
                        String[] ressourcesPref = sc.nextLine().split(" "); //crée un tableau avec les préférences du colon

                        for (String resPref : ressourcesPref) {
                            try {
                                int numRes = Integer.parseInt(resPref); //convertit le numéro de la ressource de String à entier
                                Ressource ressource = colonie.trouverRessource(numRes); //récupère la ressource associée
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
                        System.out.println("\nLes colons suivants " + nomColonRestant + " n'ont pas encore reçu de préférence, obligatoire pour terminer la configuration !!!");
                    }
                } else {
                    System.out.println("Choix invalide.");
                }
            }
        }
        //Fin de la construction de la colonie en fonction de la presence d'un argument ou non, ainsi que affichage des menus correspondants selon cas de figure




        if(args.length<=0){
            Affectation.init(colonie);
            System.out.println("\nAffectation des ressources");
            System.out.println("Entrez l'ordre des colons (un nom par ligne) pour l'affectation :");

            for (int i = 0; i < colonie.getNbColon(); i++) {
                String nomColon = sc.nextLine(); //lit le nom du colon
                Colon colon = colonie.trouverColon(nomColon); //récupère le colon associé

                if (colon != null) { //si le colon existe
                    Affectation.affectationNaive(colon); //effectue l'affectation naïve
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
        }

        // Calcul du cout
        
        for (Colon colon : colonie.getListeColons()) {
            colon.setJaloux(false);
        }
        //Les lignes de codes precedemment placées ici sont déplacées plus bas dans une methode calculerCout(c);
        cout = colonie.calculerCout();
        Affectation.cout = cout;

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

                Colon colon1 = colonie.trouverColon(nom1);
                Colon colon2 = colonie.trouverColon(nom2);

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


    }

}
