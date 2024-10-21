
package projet_paa;


public class Ressource {
    /*
    Classe permettant de representer des instances de Ressources qui devront etre partagées entre les Colons
    */
    private int NumeroRessource; //Defini un numero pour la ressource instanciée
    
    
    public Ressource(int i) // Constructeur de la ressource
    {
        this.NumeroRessource = i;
    }
    
    public int getNumeroRessource() //fonction pour recuperer le numero de la ressource instanciée
    {
        return NumeroRessource;
    }
    
}
