package eu.wdaqua.wikidataPrivate;public class Info {
    /**
     * Classe "interface" qui nous servira à stocker les informations trouvées
     */
    private String sujet;
    private String predicat;
    private String objet;

    public Info(String sujet, String predicat, String objet) {
        this.sujet = sujet;
        this.predicat = predicat;
        this.objet = objet;
    }

    public Info(String sujet, String predicat) {
        this.sujet = sujet;
        this.predicat = predicat;
    }


    public String getSujet() {
        return sujet;
    }

    @Override
    public String toString() {
        return "Info{" +
                "sujet='" + sujet + '\n' +
                "predicat='" + predicat + '\n' +
                "objet='" + objet + '\n' +
                '}';
    }

    //
    public String getPredicat() {
        return predicat;
    }

    public String getObjet() {
        return objet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public void setPredicat(String predicat) {
        this.predicat = predicat;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }
}