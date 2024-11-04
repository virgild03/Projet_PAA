package projet_paa;

import java.util.*;

public class Simulateur {

    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur.

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Nombre de colons dans la colonie ? ");
        int nbColons = sc.nextInt(); //lit le nombre de colons
        sc.nextLine();
        Colonie colonie = new Colonie(nbColons);//initialise une colonie

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
                break;
            } else {
                System.out.println("Choix invalide.");
            }
        }
        //Fin de la construction de la colonie

        // Affectation des ressources
        Affectation affectation = new Affectation(colonie); //initialise les affectations Colon / Ressource de la colonie
        System.out.println("\nAffectation des ressources");
        System.out.println("Entrez l'ordre des colons (un nom par ligne) pour l'affectation :");

        for (int i = 0; i < colonie.getNbColon(); i++) {
            String nomColon = sc.nextLine(); //lit le nom du colon
            Colon colon = trouverColon(colonie, nomColon); //récupère le colon associé

            if (colon != null) { //si le colon existe
                affectation.affectationNaive(colon); //effectue l'affectation naïve
                System.out.println("Ressource attribuée à " + colon.getNomColon() + " : " + colon.getRessourceAttribue());
            } else { //sinon renvoie un message d'erreur
                System.out.println("Colon " + nomColon + " non trouvé.");
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
