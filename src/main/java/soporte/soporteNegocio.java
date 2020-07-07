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
import dto.Dependencia;
import dto.Estado;
import dto.Solicitud;
import dto.Soporte;
import dto.Usuario;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
         con=Conexion.getConexion();
    }
    
    public List<Usuario> getUsuarios(){
        ujc=new UsuarioJpaController(con.getBd());
        return ujc.findUsuarioEntities();
    }
    public List<Soporte> getSoporte(){
        sjc=new SoporteJpaController(con.getBd());
        return sjc.findSoporteEntities();
    }
    public List<Solicitud> getSolicitudes(){
        sojc=new SolicitudJpaController(con.getBd());
        return sojc.findSolicitudEntities();
    }
    public List<Dependencia> getDependencia(){
        djc=new DependenciaJpaController(con.getBd());
        return djc.findDependenciaEntities();
    }
    public List<Estado> getEstado(){
        ejc=new EstadoJpaController(con.getBd());
        return ejc.findEstadoEntities();
    }
    
    public int login(String email,String clave){
        List<Usuario> lu=this.getUsuarios();
        for (Usuario usuario : lu) {
             if(usuario.getEmail().equals(email) && usuario.getClave().equals(clave)){
                     if(usuario.getSoporte() == null){
                         return 1;
                     }
                     return 2;
                 }
        }
        return 0;
    }
    //public Solicitud(Integer id, Date fechasolicitud, String descripcion, String texto, Date fecharespuesta, String respuesta, int calificacion, String observaciones)
   
    
}
