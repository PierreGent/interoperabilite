package eu.wdaqua.wikidataPrivate;


import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    final static String siteIri = "http://qanswer-svc1.univ-st-etienne.fr/index.php";
    // final static String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";

    public static void main(String[] args) throws MediaWikiApiErrorException {

      /*

        Ecole e = new Ecole();
        e.setCode("A1234");
        e.setCodePostal("43520");
        e.setNom("Ecole TEST");
        e.transfer();
*/




        /**
         * Déclarations des listes pour stocker les infos trouvées
         * Une liste par source
         */
        ArrayList<Elu> listeElu = null;
        ArrayList<Service> listeService = null;
        ArrayList<Ecole> listeEcole = null;


        ParseurBDD bdd = new ParseurBDD();
        /**
         * Instanciation du parseur HTML avec l'URL a traitée
         */
        ParseurHTML parseur = new ParseurHTML("https://www.ville-lechambonsurlignon.fr/mairie/les-services-municipaux-3.html");
        ParseurCSV csv = new ParseurCSV();


        System.out.println("Hello World!");


        listeService = parseur.parseur();



        /**
         *
         *
         *
         *
         * lancer le transfer sur la liste :
         *
         *
         * getResponsable ne fonctionne pas ???
         *
         *  04.77.86.85.95
         *
         * quand on met le num de telephone : Malformed input ! Numero nul !!!
         */


        for(int i=0; i<listeService.size();i++){
            System.out.println(listeService.get(i).toString());
            //listeService.get(i).transfer();
            System.out.println("XXXXXXXXX");
            System.out.println(listeService.get(i).getNumero());
            listeService.get(i).setNumero(listeService.get(i).getNumero().substring(1,listeService.get(i).getNumero().length()));

           // listeService.get(i).transfer();
            System.out.println("XXXXXXXXX");
        }
        System.out.println("\n");
        listeEcole = csv.ParseurCSV();
        System.out.println("\n");
        try {
            listeElu = bdd.ParseurBDD();
        } catch (SQLException error) {
            System.out.println("Erreur base de donnée !");
            error.printStackTrace();
        }


        Ecole e;

        for(int i =0 ; i<listeEcole.size();i++){
            e=listeEcole.get(i);
           //e.transfer();
        }




        Service s = new Service();

        String resu = s.getResponsable(listeService.get(1).getResponsable());

        System.out.println(resu);








        Elu elu;
        for(int i=0; i<listeElu.size();i++){
            elu=listeElu.get(i);
            //System.out.println(elu.codePostal + " "+elu.getPrenom() + " "+elu.getDateNaissance());
            //elu.transfer();
        }
        /*

        ArrayList<Ecole> infoCSV = null;
        ParseurCSV csv = new ParseurCSV();
        infoCSV = csv.ParseurCSV();
        //dans infoCSV on a la liste des ecole de la zone du haut lignon

        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection con = new ApiConnection("http://qanswer-svc1.univ-st-etienne.fr/api.php");

        try {
            //Put in the first place the user with which you created the bot account
            //Put as password what you get when you create the bot account
            con.login("Admin", "my_bot@bia7riffnss8ujf108a0a67hs8qqmhl2");
        } catch (LoginFailedException e) {
            e.printStackTrace();
        }



        //Example for getting information about an entity, here the example of Pierre Maret, Q1342
        //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/FetchOnlineDataExample.java
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
        ItemDocument laboratoireHC = (ItemDocument) wbdf.getEntityDocument("Q1342");
        System.out.println(laboratoireHC.getEntityId());
        System.out.println(laboratoireHC.getLabels());
        System.out.println(laboratoireHC.getStatementGroups());
        System.out.println(laboratoireHC.getDescriptions());

        //Example to search for an ID given the label
        List<WbSearchEntitiesResult> entities = wbdf.searchEntities("France");
        for (WbSearchEntitiesResult entity : entities){
            ItemDocument ent = (ItemDocument) wbdf.getEntityDocument(entity.getEntityId());
            //ent.getStatementGroups().get(0).getStatements().get(0).getClaim().getValue().toString();
            System.out.println(entity.getEntityId());
        }

        //Example for writing information about an entity, here the example of creating MisterX living in Chambon Sur Lignon
        //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/EditOnlineDataExample.java
        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);
        PropertyDocument propertyAdesse = (PropertyDocument) wbdf.getEntityDocument("P173");
        PropertyDocument property = (PropertyDocument) wbdf.getEntityDocument("P173");
        System.out.println(propertyAdesse.getLabels());

        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
//        Statement statement1 = StatementBuilder
//                .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
//                .withValue(Datamodel.makeStringValue("bluuuu")).build();

        StatementBuilder s = StatementBuilder.forSubjectAndProperty(noid, propertyAdesse.getPropertyId());
        s.withValue(Datamodel.makeItemIdValue("Q2",siteIri));

        Statement stat = s.build();


        /*
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel("Nom de l'ecole", "fr")
                .withStatement(stat).build();
        try {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                    "Statement created by our bot");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created Mister X lives in chambon");
        System.out.println(itemDocument.getItemId().getId());







ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)


        .withLabel("Mister X", "en")
                .withLabel("Mister X", "fr")
                .withStatement(stat).build();
        try {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                    "Statement created by our bot");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created Mister X lives in chambon");
        System.out.println(itemDocument.getItemId().getId());
         */








    }
}
