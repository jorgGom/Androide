package com.example.jorge.agenda.model;

/**
 * Created by Jorge on 08/12/2017.
 */

public class Eventos {
    private String Titulo, Descripcion, Fecha, Hora, Localizacion;

    public Eventos(){

    }

    public Eventos(String titulo, String descripcion, String fecha, String hora, String localizacion) {
        Titulo = titulo;
        Descripcion = descripcion;
        Fecha = fecha;
        Hora = hora;
        Localizacion = localizacion;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getLocalizacion() {
        return Localizacion;
    }

    public void setLocalizacion(String localizacion) {
        Localizacion = localizacion;
    }
}
