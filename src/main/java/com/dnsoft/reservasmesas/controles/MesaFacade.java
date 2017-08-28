/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Mesa;
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
public class MesaFacade extends AbstractFacade<Mesa> {

    @PersistenceContext(unitName = "com.dnsoft_ReservasMesas_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MesaFacade() {
        super(Mesa.class);
    }

    public List<Mesa> findAllOrderByPdvMesa() {
        Query qr = em.createQuery("from Mesa m order by m.pdvId, m.numero");
        return qr.getResultList();
    }

    public Integer findLugares() {
        Query qr = em.createQuery("select sum(m.lugares) from Mesa m ");

        return ((Long) qr.getSingleResult()).intValue();
    }
}
