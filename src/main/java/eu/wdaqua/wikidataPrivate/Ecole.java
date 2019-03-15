package eu.wdaqua.wikidataPrivate;

import lombok.Data;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;

@Data
public class Ecole {
    private String code;
    private String nom;
    private String adresse;
    private String codePostal;


    final static String siteIri = "http://qanswer-svc1.univ-st-etienne.fr/index.php";


    public void transfer() throws MediaWikiApiErrorException{


        Ecole e = this;
        /*
        Ecole e = new Ecole();
        e.setNom("Test");
        e.setAdresse("3 rue de Jean");
        e.setCodePostal("42300");
        e.setCode("123456789");
*/
        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection con = new ApiConnection("http://qanswer-svc1.univ-st-etienne.fr/api.php");

        try {
            //Put in the first place the user with which you created the bot account
            //Put as password what you get when you create the bot account
           // con.login("Admin", "my_bot@bia7riffnss8ujf108a0a67hs8qqmhl2");
            con.login("Groupe-1", "3aVHR4dUqnbEHn7");
        } catch (LoginFailedException e2) {
            e2.printStackTrace();
        }


        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);

        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);

        PropertyDocument propertyArdesse = (PropertyDocument) wbdf.getEntityDocument("P10");
        PropertyDocument propertyAppartient = (PropertyDocument) wbdf.getEntityDocument("P1090");//appartient a
        PropertyDocument propertyCode = (PropertyDocument) wbdf.getEntityDocument("P1067"); //code academique
        //PropertyDocument property = (PropertyDocument) wbdf.getEntityDocument("P173");

        //Q82 district scolaire

        PropertyDocument instance = (PropertyDocument) wbdf.getEntityDocument("P16");
        //System.out.println(propertyArdesse.getLabels());
        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
//        Statement statement1 = StatementBuilder
//                .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
//                .withValue(Datamodel.makeStringValue("bluuuu")).build();



        StatementBuilder s = StatementBuilder.forSubjectAndProperty(noid, propertyArdesse.getPropertyId());
        //on cree la declaration de ville

        if(e.codePostal.equals(Integer.toString(43190))){
            s.withValue(Datamodel.makeItemIdValue("Q30",siteIri));
        }else if(e.codePostal.equals(Integer.toString(43400))){
            s.withValue(Datamodel.makeItemIdValue("Q2",siteIri));
        }else if(e.codePostal.equals(Integer.toString(43520))){
            s.withValue(Datamodel.makeItemIdValue("Q29",siteIri));
        }else if(e.codePostal.equals(Integer.toString(43200))){
            s.withValue(Datamodel.makeItemIdValue("Q2357",siteIri));
        }else{
            s.withValue(Datamodel.makeItemIdValue("Q2",siteIri));
        }

        Statement stat = s.build();


        StatementBuilder estEcole = StatementBuilder.forSubjectAndProperty(noid,instance.getPropertyId());
        estEcole.withValue(Datamodel.makeItemIdValue("Q44", siteIri));
        Statement stat3 = estEcole.build();

        StatementBuilder estDans = StatementBuilder.forSubjectAndProperty(noid,propertyAppartient.getPropertyId());
        estDans.withValue(Datamodel.makeItemIdValue("Q2356", siteIri));
        Statement stat4 = estDans.build();

        StatementBuilder codeAca = StatementBuilder.forSubjectAndProperty(noid,propertyCode.getPropertyId());
        codeAca.withValue(Datamodel.makeStringValue(e.getCode()));
        Statement stat5 = codeAca.build();

        /*
        on fous le nom de l'école
         */
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel(e.getNom(), "fr")
                .withStatement(stat)
                .withStatement(stat4)
                .withStatement(stat5)
                .withStatement(stat3).build();
        try {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                    "Statement created by our bot");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        System.out.println("Created Ecole in the Haut Lignon");
        System.out.println(itemDocument.getItemId().getId());

    }


}
