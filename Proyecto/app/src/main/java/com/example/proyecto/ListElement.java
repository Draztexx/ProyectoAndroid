package com.example.proyecto;

public class ListElement {


    public String Nombre;
    public int Puntos;


    public ListElement(String nombre, int puntos) {
        Nombre = nombre;
        Puntos = puntos;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getPuntos() {
        return Puntos;
    }

    public void setPuntos(int puntos) {
        Puntos = puntos;
    }

}
