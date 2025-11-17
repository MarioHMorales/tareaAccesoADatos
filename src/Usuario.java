/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.Serializable;

/**
 * Clase Usuario que representa a un usuario del sistema.
 * Implementa Serializable para permitir su almacenamiento en un fichero binario.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String identificador;
    private String contraseña;
    private String direccion;
    private int anioNacimiento;

    /**
     * Constructor de la clase Usuario.
     *
     * @param identificador identificador único (4 números + 1 letra)
     * @param contraseña contraseña (6-8 caracteres)
     * @param direccion dirección con nombre libre + número
     * @param anioNacimiento año de nacimiento (4 dígitos)
     */
    public Usuario(String identificador, String contraseña, String direccion, int anioNacimiento) {
        this.identificador = identificador;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.anioNacimiento = anioNacimiento;
    }

    //==================== GETTERS & SETTERS ====================//

    public String getIdentificador() {
        return identificador;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    /**
     * Representación en texto del usuario.
     */
    @Override
    public String toString() {
        return "Identificador: " + identificador
                + " | Contraseña: " + contraseña
                + " | Año de nacimiento: " + anioNacimiento
                + " | Dirección: " + direccion;
                
    }
}

