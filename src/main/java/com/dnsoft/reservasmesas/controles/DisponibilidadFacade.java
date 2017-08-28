/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Disponibilidad;
import com.dnsoft.reservasmesas.entidades.Pdv;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    public Disponibilidad getDisponibilidad(Date fecha, Pdv pdv) {
        Disponibilidad toReturn;
        Query qr = em.createQuery("FROM Disponibilidad AS d where d.fecha= ?1 and d.pdv = ?2");
        qr.setParameter(1, fecha);
        qr.setParameter(2, pdv);
        toReturn = (Disponibilidad) qr.getSingleResult();

        return toReturn;
    }

    public List<Disponibilidad> getDisponibilidadEntreFechas(Date desde, Date hasta, Pdv pdv) {
        Query qr = em.createQuery("FROM Disponibilidad AS d where d.fecha between  ?1 and ?2 and d.pdv = ?3");
        qr.setParameter(1, desde);
        qr.setParameter(2, hasta);
        qr.setParameter(3, pdv);

        return qr.getResultList();
    }

    public void fechas(Pdv pdv, Integer lugarss) {

        Calendar fecha = Calendar.getInstance();
        fecha.set(2017, 7, 01);

        Disponibilidad dia;

        for (int i = 0; i < 1825; i++) {
            fecha.add(Calendar.DAY_OF_YEAR, 1);
            dia = new Disponibilidad(fecha.getTime());
            dia.setPdv(pdv);
            dia.setDisponibilidad(lugarss);
            dia.setLugaresTotales(lugarss);
            em.merge(dia);
        }

    }

    public void actualizarFecha(Disponibilidad fecha) {
        em.merge(fecha);
    }
}
