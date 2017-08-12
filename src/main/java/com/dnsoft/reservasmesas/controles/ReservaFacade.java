/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Mesa;
import com.dnsoft.reservasmesas.entidades.Pdv;
import com.dnsoft.reservasmesas.entidades.Reserva;
import java.util.ArrayList;
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
public class ReservaFacade extends AbstractFacade<Reserva> {

    @PersistenceContext(unitName = "com.dnsoft_ReservasMesas_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservaFacade() {
        super(Reserva.class);
    }

    public Integer lugaresDisponibles(Integer lugares, Date fechaReserva, Mesa mesa) {
        Integer lugaresDisponibles = lugares;

        Query qr = em.createQuery("FROM Reserva AS r where r.fechaReserva= ?1 and r.mesaId= ?2");
        qr.setParameter(1, fechaReserva);
        qr.setParameter(2, mesa);

        List<Reserva> reservas = qr.getResultList();
        Integer pax = 0;
        for (Reserva reserva : reservas) {
            pax = pax + reserva.getPax();
        }
        lugaresDisponibles = lugares - pax;
        return lugaresDisponibles;
    }

    public List<Reserva> ReservasPorFecha(Date fechaReserva, Pdv pdv) {
        List<Reserva> listReservas = new ArrayList<>();

        Query qr = em.createQuery("FROM Reserva AS r where r.fechaReserva= ?1 and r.mesaId.pdvId= ?2 order by r.mesaId.numero");
        qr.setParameter(1, fechaReserva);
        qr.setParameter(2, pdv);
        listReservas.addAll(qr.getResultList());

        return listReservas;
    }

    public List<Reserva> ReservasPorFechaYMesa(Date fechaReserva, Mesa mesa) {
        List<Reserva> listReservas = new ArrayList<>();

        Query qr = em.createQuery("FROM Reserva AS r where r.fechaReserva= ?1 and r.mesaId= ?2 ");
        qr.setParameter(1, fechaReserva);
        qr.setParameter(2, mesa);
        listReservas.addAll(qr.getResultList());

        return listReservas;
    }

    public void guardar(Object toBD) {
        em.merge(toBD);
    }
}
