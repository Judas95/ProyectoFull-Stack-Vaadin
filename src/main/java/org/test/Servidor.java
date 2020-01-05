package org.test;

public class Servidor {

    private int idservidor;
    private String name;
    private String region;
    private int capacidad;
    private String img;



    public Servidor(int idservidor, String name, String region, int capacidad, String img) {
        this.idservidor = idservidor;
        this.name = name;
        this.region = region;
        this.capacidad = capacidad;
        this.img = img;
    }

    public int getIdservidor() {
        return idservidor;
    }

    public void setIdservidor(int idservidor) {
        this.idservidor = idservidor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
