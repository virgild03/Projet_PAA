package projet_paa.src;

import java.io.*;
import java.util.*;

/*
classe qui sera instanciée à chaque verif de fichier.
pour chaque fichier il y aura un appel à la methode de verif.
Gestion des exceptions avec la classe dédiée FichierException
 */
public class VerifFichierTxt {





    public void verifFichierTxt(String fichier) throws IOException {
        // Structures pour vérifier la cohérence
        Set<String> colons = new HashSet<>();          // Ensemble des noms des colons qui seront ajoutés à la colonie
        Set<String> ressources = new HashSet<>();      // Ensemble des noms des ressources qui seront ajoutés à la colonie
        boolean partieColons = true;
        boolean partieRessources = false;
        boolean partieDeteste = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) { //J'ai mis le br dans le try parce que Malik m'a dit de le faire...
            String ligne;
            int numeroLigne = 0;

            while ((ligne = br.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                if (ligne.isEmpty()) {
                    continue; // si ligne vide on passe à l'iteration suivante
                }

                if (ligne.startsWith("colon(")) {
                    // Vérification de l'ordre
                    if (partieRessources || partieDeteste) {
                        throw new FichierException("Erreur a la ligne " + numeroLigne + " : L'ordre des sections n'est pas respecté. Il faut définir tous les collons avant les ressources ou les mauvaises relations.");
                    }

                    partieColons = true;
                    int debut = ligne.indexOf("(");
                    int fin = ligne.indexOf(").");
                    if (debut == -1 || fin == -1) //si la sous chaine n'existe pas
                    {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : La ligne colon doit respecter le format 'colon(nom).'");
                    }

                    String nomColon = ligne.substring(debut + 1, fin).trim(); //nomCOlon se trouve entre les index : debut+1 et fin.
                    if(nomColon.isEmpty()) //gestion nomColon vide
                    {
                        throw new FichierException("Erreur à la ligne "+numeroLigne+" : le nom de colon ne peut etre vide");
                    }

                    if (!nomColon.matches("[a-zA-Z0-9]+")) //gestion nomColon si contient caractères speciaux, espaces, accents.
                    {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : Le nom du colon doit être alphanumérique(MAJUSCULES, minuscules ou chiffres.");
                    }

                    if (!colons.add(nomColon)) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : Le colon " + nomColon + " est déjà défini.");
                    }
                }

                else if (ligne.startsWith("ressource(")) {
                    if (partieDeteste) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : L'ordre des sections n'est pas respecté (ressource après deteste).");
                    }

                    partieColons = false;
                    partieRessources = true;

                    int debut = ligne.indexOf("(");
                    int fin = ligne.indexOf(").");
                    if (debut == -1 || fin == -1) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : La ligne ressource doit respecter le format 'ressource(nom).'");
                    }

                    String nomRessource = ligne.substring(debut + 1, fin).trim(); //nomRessource se trouve entre les index : debut+1 et fin.
                    if(nomRessource.isEmpty())
                    {
                        throw new FichierException("Erreur ligne " + numeroLigne + "le nom de ressource ne peut etre vide.");
                    }
                    if (!nomRessource.matches("[a-zA-Z0-9]+")) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : Le nom de ressource doit être alphanumérique(MAJUSCULES, minuscules ou chiffres.");
                    }

                    if (!ressources.add(nomRessource)) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : la ressource " + nomRessource + " est déjà définie.");
                    }
                }

                else if (ligne.startsWith("deteste(")) {
                    partieRessources = false;
                    partieDeteste = true;

                    int debut = ligne.indexOf("(");
                    int fin = ligne.indexOf(").");
                    if (debut == -1 || fin == -1) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : La ligne deteste doit respecter le format deteste(nom1,nom2).");
                    }

                    String[] noms = ligne.substring(debut + 1, fin).split(","); //noms contiendra : [nomColon1, nomColon2].
                    if (noms.length != 2) //la ligne deteste ne peut avoir moins ou plus que 2 parametres.
                    {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : La ligne deteste doit contenir exactement deux noms.");
                    }

                    if(noms[0].trim().equals(noms[1].trim()))
                    {
                        throw new FichierException("Erreur ligne "+ numeroLigne+ " : Un colon ne peut se detester lui meme malheureusement");
                    }

                    for (String n : noms) {
                        n = n.trim();
                        if (!colons.contains(n)) //SI n n'est pas dans le set colons c'est que le colon n'existe pas.
                        {
                            throw new FichierException("Erreur ligne " + numeroLigne + " : Le colon " + n + " dans deteste n'est pas défini.");
                        }
                    }
                }

                else if (ligne.startsWith("preferences(")) {
                    partieDeteste = true;

                    int debut = ligne.indexOf("(");
                    int fin = ligne.indexOf(").");
                    if (debut == -1 || fin == -1) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : La ligne preferences doit respecter le format preferences(nomColon,ressource1,ressource2,...).");
                    }

                    String[] elements = ligne.substring(debut + 1, fin).split(",");
                    if (elements.length != colons.size() + 1) //Si la taille de elements est different de la taille de colons+1. Ou different de la taille de ressources+1
                    {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : La ligne preferences doit contenir un colon et exactement "+ colons.size() + " ressources.");

                    }

                    String nomColon = elements[0].trim();
                    if (!colons.contains(nomColon)) {
                        throw new FichierException("Erreur ligne " + numeroLigne + " : Le colon " + nomColon + " dans preferences n'est pas défini.");
                    }

                    for (int i = 1; i < elements.length; i++)
                    {
                        String ressource = elements[i].trim();
                        if (!ressources.contains(ressource)) {
                            throw new FichierException("Erreur ligne " + numeroLigne + " : La ressource " + ressource + " dans preferences n'est pas définie.");
                        }
                    }
                }

                else {
                    throw new FichierException("Erreur ligne " + numeroLigne + " : Ligne non reconnue. Les lignes valides commencent par colon, ressource, deteste ou preferences.");
                }
            }

            if (colons.size() != ressources.size()) {
                throw new FichierException("Erreur : Le nombre de colons et de ressources doit être identique.");
            }

            System.out.println("Fichier entré conforme");
        }
    }
}
