package eu.wdaqua.wikidataPrivate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseurCSV {

    /**
     * Ressource utilisé : https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
     */


    //

    String csvFile = "./fr-en-adresse-et-geolocalisation-etablissements-premier-et-second-degre.csv";
    BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ";";

    public ArrayList<Ecole> ParseurCSV() {
        int compteur =0;

        int ignored=0;

        ArrayList<Info> listeInfo = new ArrayList<Info>();
        ArrayList<Ecole> listeEcole = new ArrayList<Ecole>();
        try {

            try {
                br = new BufferedReader(new FileReader(csvFile));
            } catch (FileNotFoundException e1) {
                System.out.println("Fichier non trouvé !");
                e1.printStackTrace();
            }
            Ecole e;
            while (true) {
                try {
                    if (!((line = br.readLine()) != null)) break;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // use comma as separator
                String[] ligne = line.split(cvsSplitBy);


                /**
                 * on se limite aux établissement du haut-lignon
                 */
                List<String> codePostal = Arrays.asList("43190","43400","43520","43200");

                if(codePostal.contains(ligne[8])){//si l'etablissement est dans le haut lignon alors :
                    e =new Ecole();

                    listeInfo.add(new Info(ligne[1],"a_pour_code",ligne[0]));
                    e.setCode(Integer.parseInt(ligne[0]));
                    e.setNom(ligne[1]);
                    listeInfo.add(new Info(ligne[1],"est_une",ligne[2]));
                    //a faire
                    listeInfo.add(new Info(ligne[1],"est_un_établissement",ligne[4]));
                    listeInfo.add(new Info(ligne[1],"est_situé_dans_la_commune",ligne[10]));
                    e.setCommune(ligne[10]);
                    String adresse = ligne[5];
                    adresse = adresse.concat(" ");
                    adresse = adresse.concat(ligne[10]);
                    e.setAdresse(adresse);
                    listeInfo.add(new Info(ligne[1],"est_a_l'adresse",adresse));
                    String coordonee = ligne[11];
                    coordonee = coordonee.concat("-");
                    coordonee = coordonee.concat(ligne[12]);
                    listeInfo.add(new Info(ligne[1],"a_pour_coordonnee",coordonee));
                    //est situe : propriete P10
                    listeInfo.add(new Info(ligne[1],"est_une",ligne[19]));//XXX est une ECOLE DE NIVEAU PRIMAIRE
                    listeInfo.add(new Info(ligne[1],"est_dans_l'academie_de",ligne[28]));

                    // System.out.println("Code postal ? " + ligne[8] );
                    listeEcole.add(e);
                }else{
                    ignored++;
                }
                compteur++;
            }

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Nombre de lignes traitées : "+compteur);
        System.out.println("Nombre de lignes ignorées : "+ignored);
        System.out.println("Nombre d'établissement trouvés : "+ (compteur-ignored));
        System.out.println("Nombre d'informations trouvées : "+listeInfo.size());
        return listeEcole;
    }





}
