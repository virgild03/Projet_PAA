package projet_paa;

import java.util.*;

public class Simulateur {

    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Nombre de colons dans la colonie ? ");
        int nbColons = sc.nextInt();
        sc.nextLine();
        Colonie colonie = new Colonie(nbColons);

        while (true) {
            System.out.println("\nQue souhaitez-vous faire ? ");
            System.out.println("1. Ajouter une relation entre 2 colons");
            System.out.println("2. Ajouter les préférences d'un colon");
            System.out.println("0. Fin de la configuration");

            int choix = sc.nextInt();
            sc.nextLine();

            if (choix == 1) { //ajoute une mauvaise relation entre 2 colon
                System.out.println("Nom du premier colon :");
                String nom1 = sc.nextLine();
                System.out.println("Nom du second colon :");
                String nom2 = sc.nextLine();

                Colon colon1 = trouverColon(colonie, nom1);
                Colon colon2 = trouverColon(colonie, nom2);

                if (colon1 != null && colon2 != null) {
                    colonie.ajouterMauvaiseRelation(colon1, colon2);
                    System.out.println("Relation ajoutée entre " + nom1 + " et " + nom2);
                } else {
                    System.out.println("L'un des colons n'a pas été trouvé.");
                }

            } else if (choix == 2) { //ajoute les preferences d'un colon (du préféré au pire)
                // PS: ne gère pas encore le cas où l'utilisateur entre la meme ressource en boucle
                System.out.println("Nom du colon :");
                String nomColon = sc.nextLine();
                Colon colon = trouverColon(colonie, nomColon);

                if (colon != null) {
                    System.out.println("Entrez les numéros de ressources préférées de " + colon.getNomColon() + " en séparant chaque numéro par des espaces :");
                    String[] ressourcesPref = sc.nextLine().split(" ");

                    for (String resPref : ressourcesPref) {
                        try {
                            int numRes = Integer.parseInt(resPref);
                            Ressource ressource = trouverRessource(colonie, numRes);
                            if (ressource != null) {
                                colon.setPreference(ressource);
                            } else {
                                System.out.println("Ressource " + numRes + " non trouvée.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(resPref + " n'est pas un numéro valide.");
                        }
                    }
                } else {
                    System.out.println("Colon " + nomColon + " non trouvé.");
                }

            } else if (choix == 0) {
                break;
            } else {
                System.out.println("Choix invalide.");
            }
        }
        //Fin de la construction de la colonie

        // Affectation des ressources
        Affectation affectation = new Affectation(colonie);
        System.out.println("\nAffectation des ressources");
        System.out.println("Entrez l'ordre des colons (un nom par ligne) pour l'affectation :");

        for (int i = 0; i < colonie.getNbColon(); i++) {
            String nomColon = sc.nextLine();
            Colon colon = trouverColon(colonie, nomColon);

            if (colon != null) {
                affectation.affectationNaive(colon);
                System.out.println("Ressource attribuée à " + colon.getNomColon() + " : " + colon.getRessourceAttribue());
            } else {
                System.out.println("Colon " + nomColon + " non trouvé.");
                i--;
            }
        }

        // Calcul du cout
        cout = 0;
        for (Colon colon : colonie.getListeColons()) {
            Set<Colon> mauvaisesRelations = colonie.getVoisins(colon);
            Ressource ressourceAttribuee = colon.getRessourceAttribue();

            List<Ressource> preferences = colon.getPreference();
            int indexAttribue = preferences.indexOf(ressourceAttribuee);

            if (indexAttribue == -1) {
                indexAttribue = preferences.size();
            }

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
            }
        }
    }

    private static Colon trouverColon(Colonie colonie, String nom) {
        for (Colon c : colonie.getListeColons()) {
            if (c.getNomColon().equalsIgnoreCase(nom)) { //ignore les différences de majuscules et minuscules
                return c;
            }
        }
        return null;
    }

    private static Ressource trouverRessource(Colonie colonie, int numero) {
        for (Ressource r : colonie.getListeRessource()) {
            if (r.getNumeroRessource() == numero) {
                return r;
            }
        }
        return null;
    }
}
