package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Reserva;
import com.dnsoft.reservasmesas.controles.util.JsfUtil;
import com.dnsoft.reservasmesas.controles.util.JsfUtil.PersistAction;
import com.dnsoft.reservasmesas.entidades.Disponibilidad;
import com.dnsoft.reservasmesas.entidades.Mesa;
import com.dnsoft.reservasmesas.entidades.Pdv;
import com.dnsoft.reservasmesas.entidades.SituacionReserva;
import com.dnsoft.reservasmesas.entidades.Usuario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("reservaController")
@SessionScoped
public class ReservaController implements Serializable {

    @EJB
    private ReservaFacade ejbReservasFacade;
    @EJB
    private DisponibilidadFacade ejbFechasFacade;
    @EJB
    private MesaFacade ejbMesaFacade;
    private List<Reserva> items = null;
    private Reserva selected;
    private Date fechaReserva = new Date();
    private Pdv pdv;
    private Integer disponibilidad;
    private List<Mesa> mesasDisponibles;

    public ReservaController() {
    }

    public Reserva getSelected() {

        return selected;
    }

    public void setSelected(Reserva selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ReservaFacade getFacade() {
        return ejbReservasFacade;
    }

    public Reserva prepareCreate() {
        selected = new Reserva();
        selected.setFechaReserva(fechaReserva);
        disponibilidad = null;
        initializeEmbeddableKey();
        return selected;
    }

    public List<Mesa> getMesasDisponiblesPorPDV() {
        mesasDisponibles = ejbMesaFacade.findMesasDisponiblesPorPDVyDia(pdv, fechaReserva);
        return mesasDisponibles;
    }

    void actualizaDatosFecha() {
        List<Reserva> ReservasPorFecha = ejbReservasFacade.ReservasPorFecha(fechaReserva, pdv);
        Integer pax = 0;
        Integer lugares = ejbMesaFacade.findLugares();
        for (Reserva reserva : ReservasPorFecha) {
            pax = pax + reserva.getPax();
        }

        Disponibilidad datosFecha = ejbFechasFacade.getDisponibilidad(fechaReserva, pdv);
        datosFecha.setDisponibilidad(lugares - pax);
        datosFecha.setPaxTotales(pax);
        datosFecha.setLugaresTotales(lugares);
        ejbFechasFacade.actualizarFecha(datosFecha);
    }

    public Integer getDisponiblidadDiaMesa() {

        disponibilidad = ejbReservasFacade.lugaresDisponibles(selected.getMesaId().getLugares(), selected.getFechaReserva(), selected.getMesaId());
        return disponibilidad;
    }

    public List<Reserva> getMesasPorFechaYPdv() {

        items = getFacade().ReservasPorFecha(fechaReserva, pdv);
        return items;
    }

    public List<Reserva> getItems() {
        if (items == null) {
            //items = getFacade().findAll();
        }
        return items;
    }

    public List<Reserva> findAllItems() {
        items = getFacade().findAll();
        return items;
    }

    public Reserva getReserva(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Reserva> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Reserva> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void create() {
        try {
            selected.setFechaHoraRegistro(new Date());
            Integer lugaresDisponibles = getFacade().lugaresDisponibles(selected.getMesaId().getLugares(), selected.getFechaReserva(), selected.getMesaId()) - selected.getPax();
            selected.setLugaresDisponibles(lugaresDisponibles);

            selected.setSituacionReserva(SituacionReserva.CONFIRMADA);
            Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Usuario");
            selected.setUsuarioId(usuario);
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ReservaCreated"));

            List<Reserva> reservas = getFacade().ReservasPorFechaYMesa(selected.getFechaReserva(), selected.getMesaId());

            for (Reserva reserva : reservas) {
                reserva.setLugaresDisponibles(lugaresDisponibles);
                getFacade().guardar(reserva);

            }
            actualizaDatosFecha();
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
            }

            getMesasPorFechaYPdv();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error");
            //e.printStackTrace();
        }
    }

    public void update() {
        selected.setFechaHoraRegistro(new Date());
        //Integer lugaresDisponibles = getFacade().lugaresDisponibles(selected.getMesaId().getLugares(), selected.getFechaReserva(), selected.getMesaId()) - selected.getPax();
        //selected.setLugaresDisponibles(lugaresDisponibles);

        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ReservaUpdated"));
        Integer lugaresDisponibles = getFacade().lugaresDisponibles(selected.getMesaId().getLugares(), selected.getFechaReserva(), selected.getMesaId());
        List<Reserva> reservas = getFacade().ReservasPorFechaYMesa(selected.getFechaReserva(), selected.getMesaId());
        for (Reserva reserva : reservas) {
            reserva.setLugaresDisponibles(lugaresDisponibles);
            getFacade().guardar(reserva);

        }
        actualizaDatosFecha();

        getMesasPorFechaYPdv();
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ReservaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }

        Integer lugaresDisponibles = getFacade().lugaresDisponibles(selected.getMesaId().getLugares(), selected.getFechaReserva(), selected.getMesaId()) - selected.getPax();
        List<Reserva> reservas = getFacade().ReservasPorFechaYMesa(selected.getFechaReserva(), selected.getMesaId());
        for (Reserva reserva : reservas) {
            reserva.setLugaresDisponibles(lugaresDisponibles);
            getFacade().guardar(reserva);

        }
        actualizaDatosFecha();
        getMesasPorFechaYPdv();
    }

    private void persist(PersistAction persistAction, String successMessage) {

        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {

                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    @FacesConverter(forClass = Reserva.class)
    public static class ReservaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReservaController controller = (ReservaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reservaController");
            return controller.getReserva(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Reserva) {
                Reserva o = (Reserva) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Reserva.class.getName()});
                return null;
            }
        }

    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Pdv getPdv() {
        return pdv;
    }

    public void setPdv(Pdv pdv) {
        this.pdv = pdv;
    }

    public Integer getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Integer disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}
