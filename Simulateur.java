package projet_paa;

import java.util.Scanner;

public class Simulateur {

    public static int cout; //Permet d'initialiser un cout (jalousie) à 0 lors de la création du simulateur. 
    
    
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Nombre de colon dans la colonie ? ");
        Colonie colonie = new Colonie(sc.nextInt());

        System.out.println("Que souhaitez-vous faire ? ");
        System.out.println("Ajouter une relation entre 2 colons ? --> 1 ");
        System.out.println("Ajouter les préferences d'un colon ? --> 2 ");
        System.out.println("Fin --> 0");

        if(sc.nextInt() == 1){
            System.out.println("Colon 1 ?");
            Colon colon1 = new Colon(sc.next());   /*Possibilité d'améliorer sans construire de nouveau colon*/
            System.out.println("Colon 2 ?");
            Colon colon2 = new Colon(sc.next());
            colonie.ajouterMauvaiseRelation(colon1, colon2);

        } else if(sc.nextInt() == 2){

            System.out.println("De quel colon ?");
            /* A finir*/
        }

    }
    
  
}
