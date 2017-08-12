/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "com.dnsoft_ReservasMesas_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario iniciarSecion(Usuario usuario) {
        Usuario usr = null;

        Query qr = em.createQuery("FROM Usuario u WHERE u.nombreUsuario = ?1 AND u.password= ?2");
        qr.setParameter(1, usuario.getNombreUsuario());
        qr.setParameter(2, usuario.getPassword());
        
        List<Usuario> fromBD = qr.getResultList();
        if (!fromBD.isEmpty()) {
            usr = fromBD.get(0);
        }

        return usr;

    }

}
