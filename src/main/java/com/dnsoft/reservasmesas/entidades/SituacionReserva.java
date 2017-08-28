/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.entidades;

/**
 *
 * @author Diego Noble
 */
public enum SituacionReserva {

    CONFIRMADA("Confirmada"), CHECK_IN("Check in"), 
    CHECK_OUT("Check out"), CANCELADA("Cancelada"), 
    NO_SHOW("No show"), WAITING_LIST("Lista de espera");

    private String situacion;

    private SituacionReserva(String situacion) {
        this.situacion = situacion;
    }

}
