package org.test;

public class Personaje {

    private int idpersonaje;
    private String name;
    private String race;
    private int level;
    private String clase;


    public int getIdpersonaje() {
        return idpersonaje;
    }

    public void setIdpersonaje(int idpersonaje) {
        this.idpersonaje = idpersonaje;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public Personaje(int idpersonaje, String name, String race, int level, String clase) {
        this.idpersonaje = idpersonaje;
        this.name = name;
        this.race = race;
        this.level = level;
        this.clase = clase;
    }

    public Personaje() {

    }
}
