/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Disponibilidad;
import com.dnsoft.reservasmesas.entidades.Pdv;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Diego
 */
@Stateless
public class DisponibilidadFacade extends AbstractFacade<Disponibilidad> {

    @PersistenceContext(unitName = "com.dnsoft_ReservasMesas_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DisponibilidadFacade() {
        super(Disponibilidad.class);
    }

    public Disponibilidad getDatosFecha(Date fecha, Pdv pdv) {
        Disponibilidad toReturn;
        Query qr = em.createQuery("FROM Disponibilidad AS d where d.fecha= ?1 and d.pdv");
        qr.setParameter(1, fecha);
        qr.setParameter(2, pdv);
        toReturn = (Disponibilidad) qr.getSingleResult();

        return toReturn;
    }

    public void fechas(Pdv pdv) {

        Calendar fecha = Calendar.getInstance();
        fecha.set(2017, 8, 01);

        Disponibilidad dia;

        for (int i = 0; i < 1825; i++) {
            fecha.add(Calendar.DAY_OF_YEAR, 1);
            dia = new Disponibilidad(fecha.getTime());
            em.merge(dia);
        }

    }

    public void actualizarFecha(Disponibilidad fecha) {
        em.merge(fecha);
    }
}
