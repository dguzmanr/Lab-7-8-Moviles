/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;

/**
 *
 * @author David Guzman
 */
public class Profesor implements Serializable{
      private String cedula;
    private String nombre;
    private String email;
    private int telefono;

    public Profesor() {
        this.cedula = "";
        this.cedula = "";
        this.cedula = " ";
        this.telefono = 0;
    }

    public Profesor(String cedula, String nombre, int telefono,String email) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

//    @Override
//    public String toString() {
//        return "Profesor{" +
//                "cedula='" + cedula + '\'' +
//                ", nombre='" + nombre + '\'' +
//                ", email='" + email + '\'' +
//                ", telefono=" + telefono +
//                '}';
//    }

    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesor)) return false;

        Profesor profesor = (Profesor) o;

        if (getTelefono() != profesor.getTelefono()) return false;
        if (getCedula() != null ? !getCedula().equals(profesor.getCedula()) : profesor.getCedula() != null)
            return false;
        if (getNombre() != null ? !getNombre().equals(profesor.getNombre()) : profesor.getNombre() != null)
            return false;
        return getEmail() != null ? getEmail().equals(profesor.getEmail()) : profesor.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getCedula() != null ? getCedula().hashCode() : 0;
        result = 31 * result + (getNombre() != null ? getNombre().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + getTelefono();
        return result;
    }
}
