package eu.wdaqua.wikidataPrivate;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseurHTML {

    private String url;

    public ParseurHTML(String url) {
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Info> parseur(){

        ArrayList<Info> listeInfo = new ArrayList<Info>();
        String url = this.getUrl();
        try {
            Document doc = Jsoup.connect(url).get();
            // doc.select("style").remove();
            // doc.select("xml").remove();
            Element niveau1 = doc.selectFirst("div#content_size > div");
            //System.out.println(niveau1.toString());
            String test = niveau1.toString();
            String test2 = Jsoup.parse(test).text();
            // System.out.println("\n\n"+test2);
            String[] infoBrut = test2.split(" - ");
            Pattern pattern;
            Matcher matcher = null;
            ArrayList<String[]> result = new ArrayList<String[]>();
            String[] matrice = null;
            //
            for(int i=0;i<infoBrut.length;i++){
                result.add(infoBrut[i].split(":"));
            }
            for (int i = 0 ; i<result.size();i++){

                /**
                 *
                 *
                 * ICI !
                 *
                 */
                // System.out.println(Arrays.toString(result.get(i)));
                switch (result.get(i).length) {
                    case 3:
                        //enlever les Tél et tél
                        for (int k = 0; k < result.get(i).length; k++) {
                            result.get(i)[k] = result.get(i)[k].replaceAll("Tél", "");
                            result.get(i)[k] = result.get(i)[k].replaceAll("tél", "");
                        }
                        /**
                         * format :
                         * SERVICE , NOM RESPONSABLE , NUMERO
                         *
                         * Ici , madame XXX est responsable de Service YYY
                         *
                         * et
                         *
                         * madame XXX à le numéro UUU
                         */
                        listeInfo.add(new Info(result.get(i)[1], "est_responsable_du_service", result.get(i)[0]));
                        listeInfo.add(new Info(result.get(i)[1], "a_le_numéro", result.get(i)[2]));
                        break;

                    /**
                     *
                     * Il faut traiter les cas non-conventionelle
                     * les lignes où il y a plus ou moins de 3 items
                     * e.g. Horaires d'ouverture ou dernieres lignes
                     *
                     * Eventuellement ajouté les SERVICES MUNICIPAUX ...
                     */
                }
            }
            System.out.println("Informations trouvées : "+listeInfo.size());
        }catch (IOException e){
            System.out.println("Erreur IOException ! \n"+e);
        }
        return listeInfo;
    }
}
