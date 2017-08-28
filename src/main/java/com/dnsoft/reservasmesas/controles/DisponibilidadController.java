package com.dnsoft.reservasmesas.controles;

import com.dnsoft.reservasmesas.entidades.Disponibilidad;
import com.dnsoft.reservasmesas.controles.util.JsfUtil;
import com.dnsoft.reservasmesas.controles.util.JsfUtil.PersistAction;
import com.dnsoft.reservasmesas.entidades.Pdv;

import java.io.Serializable;
import java.util.Calendar;
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

@Named("disponibilidadController")
@SessionScoped
public class DisponibilidadController implements Serializable {

    @EJB
    private DisponibilidadFacade ejbFacade;
    @EJB
    private PdvFacade ejbPdvFacade;
    @EJB
    private MesaFacade ejbMesaFacade;
    
    private List<Disponibilidad> items = null;
    private Disponibilidad selected;
    private Date desde = new Date();
    private Date hasta = new Date();
    private Pdv pdv;


    public DisponibilidadController() {
    }

    public Disponibilidad getSelected() {
        return selected;
    }

    public void setSelected(Disponibilidad selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DisponibilidadFacade getFacade() {
        return ejbFacade;
    }

    public Disponibilidad prepareCreate() {
        selected = new Disponibilidad();
        initializeEmbeddableKey();
        return selected;
    }

    public void crearFechas() {
        List<Pdv> pdvs = ejbPdvFacade.findAll();
        Integer lugares = ejbMesaFacade.findLugares();
        for (Pdv pdv : pdvs) {
            ejbFacade.fechas(pdv, lugares);
        }

    }

    public List<Disponibilidad> disponibilidadEntreFechasPorPDV() {
        items = ejbFacade.getDisponibilidadEntreFechas(desde, hasta, pdv);
        return items;

    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FechasCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FechasUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FechasDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Disponibilidad> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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

    public Disponibilidad getFechas(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Disponibilidad> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Disponibilidad> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Disponibilidad.class)
    public static class FechasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DisponibilidadController controller = (DisponibilidadController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "DisponibilidadController");
            return controller.getFechas(getKey(value));
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
            if (object instanceof Disponibilidad) {
                Disponibilidad o = (Disponibilidad) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Disponibilidad.class.getName()});
                return null;
            }
        }

    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Pdv getPdv() {
        return pdv;
    }

    public void setPdv(Pdv pdv) {
        this.pdv = pdv;
    }

 

}
