/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnsoft.reservasmesas.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "disponibilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disponibilidad.findAll", query = "SELECT f FROM Disponibilidad f")})
public class Disponibilidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "disponibilidad")
    private Integer disponibilidad = 0;
    @Column(name = "pax_totales")
    private Integer paxTotales = 0;
    @Column(name = "lugares_totales")
    private Integer lugaresTotales = 0;
    @ManyToOne(optional = false)
    private Pdv pdv;

    public Disponibilidad() {
    }

    public Disponibilidad(Long id) {
        this.id = id;
    }

    public Disponibilidad(Date fecha) {
        this.fecha = fecha;
    }

    public Disponibilidad(Long id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Integer disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Integer getPaxTotales() {
        return paxTotales;
    }

    public void setPaxTotales(Integer paxTotales) {
        this.paxTotales = paxTotales;
    }

    public Integer getLugaresTotales() {
        return lugaresTotales;
    }

    public void setLugaresTotales(Integer lugaresTotales) {
        this.lugaresTotales = lugaresTotales;
    }

    public Pdv getPdv() {
        return pdv;
    }

    public void setPdv(Pdv pdv) {
        this.pdv = pdv;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Disponibilidad)) {
            return false;
        }
        Disponibilidad other = (Disponibilidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dnsoft.reservasmesas.entidades.Fechas[ id=" + id + " ]";
    }

}
