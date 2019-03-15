package eu.wdaqua.wikidataPrivate;


import lombok.Data;
import org.openrdf.query.algebra.Str;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;



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

import java.util.List;
import java.util.logging.Logger;

@Data
public class Service {
    private String nom;
    private String responsable;
    private String numero;


    final static String siteIri = "http://qanswer-svc1.univ-st-etienne.fr/index.php";



    public void transfer() throws MediaWikiApiErrorException {

        Service service = this;


        System.out.println("Service : NOM : "+service.getNom()+" ");
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

        PropertyDocument propertyDirigeant = (PropertyDocument) wbdf.getEntityDocument("P938");//dirigeant

        PropertyDocument instance = (PropertyDocument) wbdf.getEntityDocument("P16");//instance of

        PropertyDocument propertyDivision = (PropertyDocument) wbdf.getEntityDocument("P179");//est une division de ...
        PropertyDocument propertyNumero = (PropertyDocument) wbdf.getEntityDocument("P981");//numero de telephone
  //      PropertyDocument propertyNom = (PropertyDocument) wbdf.getEntityDocument("P395"); //nom du service
        ItemIdValue noid = ItemIdValue.NULL; // used when creating new items


        /**
         * Le service est une division de la mairie
         */
        StatementBuilder estDivision =StatementBuilder.forSubjectAndProperty(noid, propertyDivision.getPropertyId());
        estDivision.withValue(Datamodel.makeItemIdValue("Q83",siteIri));
        Statement statDivision = estDivision.build();


        /**
         * instance of de service municipale (q115)
         */
        StatementBuilder estServiceMunicipal = StatementBuilder.forSubjectAndProperty(noid,instance.getPropertyId());
        estServiceMunicipal.withValue(Datamodel.makeItemIdValue("Q115", siteIri));
        Statement statServiceMunicipal = estServiceMunicipal.build();


        /**
         * Le service a pour nom ...
         */
        /*
        System.out.println("NOM du service : "+ service.getNom());
        StatementBuilder nom = StatementBuilder.forSubjectAndProperty(noid,propertyNom.getPropertyId());
        //nom.withValue(Datamodel.makeStringValue(service.getNom()));
        nom.withValue(Datamodel.makeStringValue(service.getNom()));
        Statement statNom = nom.build();
*/
        /**
         * le service a le numero
         */


        System.out.println("Numero 8/8 : "+ service.getNumero());
        StatementBuilder numero = StatementBuilder.forSubjectAndProperty(noid,propertyNumero.getPropertyId());
        numero.withValue(Datamodel.makeStringValue(service.getNumero()));
        Statement statNumero = numero.build();
/*
        StatementBuilder dirigeant = StatementBuilder.forSubjectAndProperty(noid,propertyDirigeant.getPropertyId());
        dirigeant.withValue(Datamodel.makeItemIdValue("Q202",siteIri));
        Statement statDirigeant = dirigeant.build();

*/



        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel(service.getNom(), "fr")
                .withStatement(statDivision)
                //.withStatement(statNom)
                .withStatement(statNumero)
                .withStatement(statServiceMunicipal)

                .build();


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


    public static String getResponsable(String identite) {


        System.out.println("Get resonsable !");
        String responsable = "";

        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection con = new ApiConnection("http://qanswer-svc1.univ-st-etienne.fr/api.php");

        try {
            //Put in the first place the user with which you created the bot account
            //Put as password what you get when you create the bot account
            con.login("Groupe-1", "3aVHR4dUqnbEHn7");
        } catch (LoginFailedException e) {
            e.printStackTrace();
        }


        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);

        try {


            List<WbSearchEntitiesResult> entities = wbdf.searchEntities(identite);
            for (WbSearchEntitiesResult entity : entities) {
                ItemDocument ent = (ItemDocument) wbdf.getEntityDocument(entity.getEntityId());
                //ent.getStatementGroups().get(0).getStatements().get(0).getClaim().getValue().toString();
                System.out.println("ENTITE pour " + identite + " TROUVEES ! \n ID  : " + entity.getEntityId());
            }

            if (entities.isEmpty()) {

                System.out.println("AUCUNE INFORMATION TROUVEES POUR "+identite);
                String[] splited = identite.split("\\s+");
                Personne p = new Personne();
                p.setNom(splited[0]);
                p.setPrenom(splited[1]);

               // p.transfer();
                /*

                aucune info trouvées pour DUPRE Fabienne alors qu'elle existe !
                 */
                getResponsable(identite);
            }

            return responsable;
        } catch (MediaWikiApiErrorException e) {
            e.printStackTrace();
        }

        return responsable;
    }


    public String toString(){
        String result="";
        result=result.concat("Nom : "+this.getNom()+"\n " +
                "Responsable : "+this.getResponsable()+"" +
                "Numero = "+this.getNumero());

        return result;

    }

    }
