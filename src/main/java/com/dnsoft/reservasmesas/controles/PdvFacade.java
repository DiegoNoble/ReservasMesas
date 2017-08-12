/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Pdv;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Diego
 */
@Stateless
public class PdvFacade extends AbstractFacade<Pdv> {

    @PersistenceContext(unitName = "com.dnsoft_ReservasMesas_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PdvFacade() {
        super(Pdv.class);
    }
    
}
