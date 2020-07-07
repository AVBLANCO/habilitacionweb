/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporte;

import dao.Conexion;
import dao.DependenciaJpaController;
import dao.EstadoJpaController;
import dao.SolicitudJpaController;
import dao.SoporteJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Dependencia;
import dto.Estado;
import dto.Solicitud;
import dto.Soporte;
import dto.Usuario;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class soporteNegocio {

    Conexion con;
    UsuarioJpaController ujc;
    SoporteJpaController sjc;
    SolicitudJpaController sojc;
    DependenciaJpaController djc;
    EstadoJpaController ejc;

    //singleton 
    public soporteNegocio() {
        con = Conexion.getConexion();
    }

    public List<Usuario> getUsuarios() {
        ujc = new UsuarioJpaController(con.getBd());
        return ujc.findUsuarioEntities();
    }

    public List<Soporte> getSoporte() {
        sjc = new SoporteJpaController(con.getBd());
        return sjc.findSoporteEntities();
    }

    public List<Solicitud> getSolicitudes() {
        sojc = new SolicitudJpaController(con.getBd());
        return sojc.findSolicitudEntities();
    }

    public List<Dependencia> getDependencia() {
        djc = new DependenciaJpaController(con.getBd());
        return djc.findDependenciaEntities();
    }

    public List<Estado> getEstado() {
        ejc = new EstadoJpaController(con.getBd());
        return ejc.findEstadoEntities();
    }

    public int login(String email, String clave) {
        List<Usuario> lu = this.getUsuarios();
        for (Usuario usuario : lu) {
            if (usuario.getEmail().equals(email) && usuario.getClave().equals(clave)) {
                if (usuario.getSoporte() == null) {
                    return 1;
                }
                return 2;
            }
        }
        return 0;
    }
    //public Solicitud(Integer id, Date fechasolicitud, String descripcion, String texto, Date fecharespuesta, String respuesta, int calificacion, String observaciones)

    public boolean registrarSolicitud(Solicitud s) {
        sojc = new SolicitudJpaController(con.getBd());
        List<Solicitud> lists = this.getSolicitudes();
        for (Solicitud so : lists) {
            if (so.getDescripcion().equalsIgnoreCase(s.getDescripcion())) {
                return false;
            }
        }
        sojc.create(s);
        return true;
    }

    public Dependencia findDependencia(int i) {
        djc = new DependenciaJpaController(con.getBd());
        return djc.findDependencia(i);
    }

    public Estado findEstado(int i) {
        ejc = new EstadoJpaController(con.getBd());
        return ejc.findEstado(i);
    }

    public Usuario findUsuario(String email) {
        List<Usuario> lu = this.getUsuarios();
        for (Usuario usuario : lu) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    public void Eliminar(int id) throws NonexistentEntityException {
        try {
            sojc = new SolicitudJpaController(con.getBd());
            sojc.destroy(id);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Solicitud> getSolicitudes(Usuario u) {
        sojc = new SolicitudJpaController(con.getBd());
        Usuario user = this.findUsuario(u.getEmail());
        return user.getSolicitudList();
    }

    public Solicitud solicitudBus(int id) {
        sojc = new SolicitudJpaController(con.getBd());
        return sojc.findSolicitud(id);
    }

    public boolean responderSolicitud(Solicitud s) {

        try {
            sojc = new SolicitudJpaController(con.getBd());
            sojc.edit(s);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean calificar(int id, int calificacion, String observacion) {

        sojc = new SolicitudJpaController(con.getBd());
        Solicitud s = sojc.findSolicitud(id);
        if (s != null) {
            try {
                s.setCalificacion(calificacion);
                String ob = s.getObservaciones();
                String newObservacion = "----Observaciones de Calificacion------- \n" + "\n " + observacion + "\n Observaciones generales: \n " + ob;
                s.setObservaciones(newObservacion);
                sojc.edit(s);

                return true;
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public Soporte buscarSoporte(Usuario u) {
        List<Soporte> lu = this.getSoporte();
        for (Soporte soporte : lu) {
            if (soporte.getUsuario().getEmail().equals(u.getEmail())) {
                return soporte;
            }
        }
        return null;
    }

    public void asignar(Solicitud s, Soporte sop) {
        try {
            sojc = new SolicitudJpaController(con.getBd());
            sjc = new SoporteJpaController(con.getBd());
            sop.getSolicitudList().add(s);
            sjc.edit(sop);

            s.setTecnico(sop);
            sojc.edit(s);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(soporteNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Solicitud filtrar(String frase) {
        sojc = new SolicitudJpaController(con.getBd());
        List<Solicitud> su = sojc.findSolicitudEntities();
        for (Solicitud usu : su) {
            if (usu.getDescripcion().contains(frase)) {

                return usu;
            }

        }
        return null;

    }

    public Solicitud findSolicitud(int id) {
        sojc = new SolicitudJpaController(con.getBd());
        return sojc.findSolicitud(id);
    }
    
   public List<Solicitud> getResultados(String buscar){
        sojc=new SolicitudJpaController(con.getBd());
        List<Solicitud> ls=sojc.findSolicitudEntities();
        
        for (Solicitud l : ls) {
            if(l.getUsuario().getUsuario().equalsIgnoreCase(buscar) ){
                return l.getUsuario().getSolicitudList();
            }
        }
        return null;
    }

}
