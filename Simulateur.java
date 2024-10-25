package projet_paa;

import java.util.Scanner;


public class Simulateur {
    /*
    Permet d'executer les instructions données dans l'énoncé de la partie 1
     */
    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur. 
    
    
    public static void main(String[] args)
    {
        /*
        CONSTRUCTION DE COLONIE
         */
        Scanner sc = new Scanner(System.in);

        System.out.println("Nombre de colon dans la colonie ? ");
        Colonie colonie = new Colonie(sc.nextInt());

        do {
            System.out.println("Que souhaitez-vous faire ? ");
            System.out.println("Ajouter une relation entre 2 colons ? --> 1 ");
            System.out.println("Ajouter les préferences d'un colon ? --> 2 ");
            System.out.println("Fin --> 0");


            if (sc.nextInt() == 1) {
                System.out.println("Colon 1 ?");
                Colon colon1 = new Colon(sc.next());   /*UTILISER SCANNER Possibilité d'améliorer sans construire de nouveau colon*/
                System.out.println("Colon 2 ?");
                Colon colon2 = new Colon(sc.next());
                colonie.ajouterMauvaiseRelation(colon1, colon2);

            } else if (sc.nextInt() == 2) {

                System.out.println("De quel colon ?");
                char c = sc.next().charAt(0); //lit le premier caractere de la chaine entree par utilisateur. rentrer le nom du colon voulu.
                colonie.remplirPreferences(c); //appelle la methode de colonie qui permet de remplir les prefs d'un colon

            }

        }while(sc.nextInt() == 0);

        /*
        CONSTRUCTION DE COLONIE TERMINEE
         */

        /*
        AFFECTATION DES RESSOURCES
         */
        System.out.println("Affectation des ressources NAIVEMENT");
        System.out.println("choisissez l'ordre des colons qui auront leur affectation avec la plus haute preference");
        for (int i=0; i< colonie.getNbColon();i++){
            System.out.println("choisissez un colon");

            sc.nextLine();
            /*
            faire l'affectation dans l'ordre que veut l'utilisateur

            Puis echange de ressource

            Puis le cout de jalousie
            + ajouter attribut boolean jalousie à colon pour pouvoir afficher quel colon est jaloux
             */

        }



    }
    
  
}
