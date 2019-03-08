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
import java.util.ArrayList;
import java.util.List;


public class Main {
    final static String siteIri = "http://qanswer-svc1.univ-st-etienne.fr/index.php";
    // final static String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";

    public static void main(String[] args) throws MediaWikiApiErrorException {


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
        PropertyDocument propertyTravaille = (PropertyDocument) wbdf.getEntityDocument("P580");
        System.out.println(propertyTravaille.getLabels());

        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
//        Statement statement1 = StatementBuilder
//                .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
//                .withValue(Datamodel.makeStringValue("bluuuu")).build();

        StatementBuilder s = StatementBuilder.forSubjectAndProperty(noid, propertyTravaille.getPropertyId());
        s.withValue(Datamodel.makeItemIdValue("Q2",siteIri));

        Statement stat = s.build();

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


    }
}
