/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Fechas;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Diego
 */
@Stateless
public class FechasFacade extends AbstractFacade<Fechas> {

    @PersistenceContext(unitName = "com.dnsoft_ReservasMesas_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FechasFacade() {
        super(Fechas.class);
    }

    public void fechas() {

        Calendar fecha = Calendar.getInstance();
        fecha.set(2017, 01, 01);

        Fechas dia;

        for (int i = 0; i < 1825; i++) {
            fecha.add(Calendar.DAY_OF_YEAR, 1);
            dia = new Fechas(fecha.getTime());
            em.merge(dia);
        }

    }

}
