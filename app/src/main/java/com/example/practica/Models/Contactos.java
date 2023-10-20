package com.example.practica.Models;

public class Contactos {
    private Integer id;
    private String nombre;
    private Integer telefono;
    private String nota;
    private String pais;
    private byte[] img;

    public Contactos(Integer id, String nombre, Integer telefono, String nota, String pais, byte[] img) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.pais = pais;
        this.img = img;
    }

    public Contactos() {

    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public String getNota() {
        return nota;
    }

    public String getPais() {
        return pais;
    }
    public byte[] getImg() {
        return img;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    public void setImg(byte[] img) {
        this.img = img;
    }


}

