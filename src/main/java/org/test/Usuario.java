package org.test;

public class Usuario {
    private int idusuario;
    private String tag;
    private String correo;



    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Usuario(int idusuario, String tag, String correo) {
        this.idusuario = idusuario;
        this.tag = tag;
        this.correo = correo;
    }

    public Usuario() {

    }
}
