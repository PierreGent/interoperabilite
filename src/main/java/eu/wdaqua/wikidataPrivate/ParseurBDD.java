package eu.wdaqua.wikidataPrivate;

import java.sql.*;
import java.util.ArrayList;

public class ParseurBDD {

    public Statement st;
    public Connection c;
    public ResultSet rs ;

    public ArrayList<Elu> ParseurBDD() throws SQLException {



        ArrayList<Elu> listeElu = new ArrayList<Elu>();




        ArrayList<Info> listeInfo = new ArrayList<Info>();
        //on charge le driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Bloc catch gÃ©nÃ©rÃ© automatiquement
            e.printStackTrace();
        }
        //connexion
        //String url = "jdbc:mysql://mira2.univ-st-etienne.fr/ba02996q";
        String url = "jdbc:mysql://localhost/interop";
        String identifiant = "root";
        String mdp = "4ag2ncarnot";
        c = DriverManager.getConnection(url, identifiant, mdp);
        c.setAutoCommit(false);
        st = c.createStatement();
        try {
            st = c.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            rs = st.executeQuery("SELECT * FROM elu WHERE DPT=43 AND CODECOM IN (51,199,244,69,129,130)");
            /**
             * CODECOM représente le code commune, ici nous nous limitons aux communes du Haut Lignon
             */
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                Elu elu = new Elu();
                String nom = rs.getString(5);
                elu.setNom(nom);
                String prenom = rs.getString(6);
                elu.setPrenom(prenom);
                String commune = rs.getString(3);
                String identite = nom;
                identite=identite.concat(" ");
                identite=identite.concat(prenom);
                listeInfo.add(new Info(identite,"est_élu_dans_la_commune",commune));
                String codecom = rs.getString(2);

                String naissance = rs.getString(8);

                elu.setDateNaissance(naissance);

                if (codecom.length() == 2){
                    codecom = "0".concat(codecom);
                    //si le code commune a 2 caractere on ajoute le zero pour faire le code postal
                }





                String cp = rs.getString(1);
                if (cp.length() == 1){
                    cp="0".concat(cp);
                }

                cp = cp.concat(codecom);
                //le code postal est le numero du departement a 2 chiffres suivi du code commune a 3 chiffres

                elu.setCodePostal(cp);
                listeInfo.add(new Info(commune,"a_pour_code_postal",cp));

                listeElu.add(elu);
            }

        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //

        System.out.println("Analyse base de donnée terminée :\n" +
                "Informations trouvées : "+listeInfo.size());
        return  listeElu;

    }
}
