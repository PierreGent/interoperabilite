package eu.wdaqua.wikidataPrivate;


import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;


public class Main {
    final static String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";

    public static void main(String[] args) throws MediaWikiApiErrorException {

        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection con = new ApiConnection("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/api.php");

        try {
            //Put in the first place the user with which you created the bot account
            //Put as password what you get when you create the bot account
            con.login("Root", "D063520Bot@sp5048gejd1nodi5n6o2tj5ocanj063o");
        } catch (LoginFailedException e) {
            e.printStackTrace();
        }



        //Example for getting information about an entity, here the example of The Laboratoire Huber Curien, Q900
        //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/FetchOnlineDataExample.java
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
        ItemDocument laboratoireHC = (ItemDocument) wbdf.getEntityDocument("Q900");
        System.out.println(laboratoireHC.getEntityId());
        System.out.println(laboratoireHC.getLabels());
        System.out.println(laboratoireHC.getStatementGroups());
        System.out.println(laboratoireHC.getDescriptions());

        //Example for writing information about an entity, here the example of creating Florance Garrelie working at Laboratoire Huber Curien
        //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/EditOnlineDataExample.java
        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);
        PropertyDocument propertyTravaille = (PropertyDocument) wbdf.getEntityDocument("P242");
        System.out.println(propertyTravaille.getLabels());

        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
        Statement statement1 = StatementBuilder
                .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
                .withValue(laboratoireHC.getItemId()).build();
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel("Florence Garrelie", "en")
                .withLabel("Florence Garrelie", "fr")
                .withStatement(statement1).build();
        try {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                    "Statement created by our bot");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created Florence Garrelie");
        System.out.println("Created statement: Florence Garrelie travaille Laboratoire Huber Curien");
        System.out.println(itemDocument.getItemId().getId());


    }
}
