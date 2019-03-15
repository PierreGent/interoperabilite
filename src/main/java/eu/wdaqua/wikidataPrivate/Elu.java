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
public class Elu {
    public String nom;
    public String prenom;
    public String codePostal;
    public String dateNaissance;


    final static String siteIri = "http://qanswer-svc1.univ-st-etienne.fr/index.php";

    public void transfer() throws MediaWikiApiErrorException {

        Elu elu = this;


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

        PropertyDocument propertyArdesse = (PropertyDocument) wbdf.getEntityDocument("P10");//adresse / commune
        PropertyDocument propertyNom = (PropertyDocument) wbdf.getEntityDocument("P1078");//nom
        PropertyDocument propertyPrenom = (PropertyDocument) wbdf.getEntityDocument("P1068"); //prenom
        PropertyDocument propertyCP = (PropertyDocument) wbdf.getEntityDocument("P1095"); //code postal
        PropertyDocument propertyDateNaissance = (PropertyDocument) wbdf.getEntityDocument("P1091");//date de naissance

        //PropertyDocument property = (PropertyDocument) wbdf.getEntityDocument("P173");



        PropertyDocument instance = (PropertyDocument) wbdf.getEntityDocument("P16");//un elu est une instance
        //System.out.println(propertyArdesse.getLabels());
        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items
//        Statement statement1 = StatementBuilder
//                .forSubjectAndProperty(noid, propertyTravaille.getPropertyId())
//                .withValue(Datamodel.makeStringValue("bluuuu")).build();



        StatementBuilder st_localisation = StatementBuilder.forSubjectAndProperty(noid, propertyArdesse.getPropertyId());
        //on cree la declaration de ville

        if(elu.getCodePostal().equals(Integer.toString(43190))){
            st_localisation.withValue(Datamodel.makeItemIdValue("Q30",siteIri));
        }else if(elu.getCodePostal().equals(Integer.toString(43400))){
            st_localisation.withValue(Datamodel.makeItemIdValue("Q2",siteIri));
        }else if(elu.getCodePostal().equals(Integer.toString(43520))){
            st_localisation.withValue(Datamodel.makeItemIdValue("Q29",siteIri));
        }else if(elu.getCodePostal().equals(Integer.toString(43200))){
            st_localisation.withValue(Datamodel.makeItemIdValue("Q2357",siteIri));
        }else{
            st_localisation.withValue(Datamodel.makeItemIdValue("Q2",siteIri));
        }

        Statement statLocalisation = st_localisation.build();

        StatementBuilder estHumain = StatementBuilder.forSubjectAndProperty(noid,instance.getPropertyId());
        estHumain.withValue(Datamodel.makeItemIdValue("Q1298", siteIri));
        Statement statHumain = estHumain.build();

        StatementBuilder nom = StatementBuilder.forSubjectAndProperty(noid,propertyNom.getPropertyId());
        nom.withValue(Datamodel.makeStringValue(elu.getNom()));
        Statement statNom = nom.build();

        StatementBuilder prenom = StatementBuilder.forSubjectAndProperty(noid,propertyPrenom.getPropertyId());
        prenom.withValue(Datamodel.makeStringValue(elu.getPrenom()));
        Statement statPrenom = prenom.build();

        StatementBuilder codePostal = StatementBuilder.forSubjectAndProperty(noid,propertyCP.getPropertyId());


        if(elu.getCodePostal().equals(Integer.toString(43190))){
            codePostal.withValue(Datamodel.makeItemIdValue("Q2404",siteIri));
        }else if(elu.getCodePostal().equals(Integer.toString(43400))){
            codePostal.withValue(Datamodel.makeItemIdValue("Q2405",siteIri));
        }else if(elu.getCodePostal().equals(Integer.toString(43520))){
            codePostal.withValue(Datamodel.makeItemIdValue("Q2406",siteIri));
        }else if(elu.getCodePostal().equals(Integer.toString(43200))){
            codePostal.withValue(Datamodel.makeItemIdValue("Q2407",siteIri));
        }else{
            codePostal.withValue(Datamodel.makeItemIdValue("Q2405",siteIri));
        }

        Statement statCP = codePostal.build();



        StatementBuilder dateNaissance = StatementBuilder.forSubjectAndProperty(noid,propertyDateNaissance.getPropertyId());
        prenom.withValue(Datamodel.makeStringValue(elu.getDateNaissance()));
        Statement statDN = dateNaissance.build();



        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel(elu.getNom(), "fr")
                .withStatement(statCP)
                .withStatement(statDN)
                .withStatement(statHumain)
                .withStatement(statLocalisation)
                .withStatement(statNom)
                .withStatement(statPrenom).build();


        try {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                    "Statement created by our bot");
        } catch (IOException e2) {
            System.out.println("ERREUR ICI");
            e2.printStackTrace();
        }
        System.out.println("Created Elu in the Haut Lignon");
        System.out.println(itemDocument.getItemId().getId());

    }

}
