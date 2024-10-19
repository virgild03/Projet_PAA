
package projet_paa;


public class Ressource {
    /*
    Classe permettant de representer des instances de Ressources qui devront etre partagées entre les Colons
    */
    private String nomRessource; //Defini un nom pour la ressource instanciée
    
    
    public Ressource(String n) // Constructeur de la ressource
    {
        this.nomRessource = n;
    }
    
    public String getNomRessource() //fonction pour recuperer le nom de la ressource instanciée
    {
        return nomRessource;
    }
    
}
