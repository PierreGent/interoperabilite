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
public class Personne {
    private String nom;
    private String prenom;


    final static String siteIri = "http://qanswer-svc1.univ-st-etienne.fr/index.php";

    public void transfer() throws MediaWikiApiErrorException {

        Personne personne = this;
        //Personne personne = new Personne();
       // personne.setPrenom("Test ");
        //personne.setNom("tesst");

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

        PropertyDocument propertyRole = (PropertyDocument) wbdf.getEntityDocument("P56");//dans le role de

        PropertyDocument instance = (PropertyDocument) wbdf.getEntityDocument("P16");//un elu est une instance

        PropertyDocument propertyNom = (PropertyDocument) wbdf.getEntityDocument("P1078");//nom
        PropertyDocument propertyPrenom = (PropertyDocument) wbdf.getEntityDocument("P1068"); //prenom
        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items


        StatementBuilder estHumain = StatementBuilder.forSubjectAndProperty(noid,instance.getPropertyId());
        estHumain.withValue(Datamodel.makeItemIdValue("Q1298", siteIri));
        Statement statHumain = estHumain.build();


        StatementBuilder nom = StatementBuilder.forSubjectAndProperty(noid,propertyNom.getPropertyId());
        nom.withValue(Datamodel.makeStringValue(personne.getNom()));
        Statement statNom = nom.build();

        StatementBuilder prenom = StatementBuilder.forSubjectAndProperty(noid,propertyPrenom.getPropertyId());
        prenom.withValue(Datamodel.makeStringValue(personne.getPrenom()));
        Statement statPrenom = prenom.build();

        StatementBuilder role = StatementBuilder.forSubjectAndProperty(noid,propertyRole.getPropertyId());
        role.withValue(Datamodel.makeItemIdValue("Q202",siteIri));
        Statement statRole = role.build();

        System.out.println(personne.getNom()+" "+personne.getPrenom());
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel(personne.getNom().concat(" "+personne.getPrenom()), "fr")
                .withStatement(statHumain)
                .withStatement(statNom)
                .withStatement(statRole)
                .withStatement(statPrenom).build();


        /*


        Ajouter propriete lieu de travail :

        P332 de la mairie (Q83)
         */


        try {
            ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                    "Statement created by our bot");
        } catch (IOException e2) {
            System.out.println("ERREUR ICI");
            e2.printStackTrace();
        }
        System.out.println("Created Personne in the Haut Lignon");
        System.out.println(itemDocument.getItemId().getId());

    }



}
