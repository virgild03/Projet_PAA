package projet_paa.src;

import java.util.Objects;

public class Ressource {
    /*
    Classe permettant de representer des instances de Ressources qui devront etre partagées entre les Colons
    */
    private int NumeroRessource; //Defini un numero pour la ressource instanciée
    private String nomRessource;


    public Ressource(int i) // Constructeur de la ressource
    {
        this.NumeroRessource = i;
    }
    
    public Ressource(int i, String n)
    {
        this.NumeroRessource = i;
        this.nomRessource = n;
    }

    public int getNumeroRessource() //fonction pour recuperer le numero de la ressource instanciée
    {
        return NumeroRessource;
    }

    public void setNumeroRessource(int num){
        this.NumeroRessource = num;
    }
    
    public String getNomRessource()
    {
        return nomRessource;
    }
    
    public void setNomRessource(String n)
    {
        nomRessource = n;
    }
    
    @Override
    public String toString() { //affiche le numéro de la ressource
        return "Ressource " + NumeroRessource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ressource that = (Ressource) o;
        return NumeroRessource == that.NumeroRessource &&
                Objects.equals(nomRessource, that.nomRessource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NumeroRessource, nomRessource);
    }
}
