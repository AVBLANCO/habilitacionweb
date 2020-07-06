/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Dependencia;
import dto.Usuario;
import dto.Soporte;
import dto.Estado;
import dto.Adjunto;
import dto.Solicitud;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class SolicitudJpaController implements Serializable {

    public SolicitudJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Solicitud solicitud) {
        if (solicitud.getAdjuntoList() == null) {
            solicitud.setAdjuntoList(new ArrayList<Adjunto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dependencia dependencia = solicitud.getDependencia();
            if (dependencia != null) {
                dependencia = em.getReference(dependencia.getClass(), dependencia.getId());
                solicitud.setDependencia(dependencia);
            }
            Usuario usuario = solicitud.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                solicitud.setUsuario(usuario);
            }
            Soporte tecnico = solicitud.getTecnico();
            if (tecnico != null) {
                tecnico = em.getReference(tecnico.getClass(), tecnico.getSoporte());
                solicitud.setTecnico(tecnico);
            }
            Estado estado = solicitud.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getId());
                solicitud.setEstado(estado);
            }
            List<Adjunto> attachedAdjuntoList = new ArrayList<Adjunto>();
            for (Adjunto adjuntoListAdjuntoToAttach : solicitud.getAdjuntoList()) {
                adjuntoListAdjuntoToAttach = em.getReference(adjuntoListAdjuntoToAttach.getClass(), adjuntoListAdjuntoToAttach.getId());
                attachedAdjuntoList.add(adjuntoListAdjuntoToAttach);
            }
            solicitud.setAdjuntoList(attachedAdjuntoList);
            em.persist(solicitud);
            if (dependencia != null) {
                dependencia.getSolicitudList().add(solicitud);
                dependencia = em.merge(dependencia);
            }
            if (usuario != null) {
                usuario.getSolicitudList().add(solicitud);
                usuario = em.merge(usuario);
            }
            if (tecnico != null) {
                tecnico.getSolicitudList().add(solicitud);
                tecnico = em.merge(tecnico);
            }
            if (estado != null) {
                estado.getSolicitudList().add(solicitud);
                estado = em.merge(estado);
            }
            for (Adjunto adjuntoListAdjunto : solicitud.getAdjuntoList()) {
                Solicitud oldSolicitudOfAdjuntoListAdjunto = adjuntoListAdjunto.getSolicitud();
                adjuntoListAdjunto.setSolicitud(solicitud);
                adjuntoListAdjunto = em.merge(adjuntoListAdjunto);
                if (oldSolicitudOfAdjuntoListAdjunto != null) {
                    oldSolicitudOfAdjuntoListAdjunto.getAdjuntoList().remove(adjuntoListAdjunto);
                    oldSolicitudOfAdjuntoListAdjunto = em.merge(oldSolicitudOfAdjuntoListAdjunto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Solicitud solicitud) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitud persistentSolicitud = em.find(Solicitud.class, solicitud.getId());
            Dependencia dependenciaOld = persistentSolicitud.getDependencia();
            Dependencia dependenciaNew = solicitud.getDependencia();
            Usuario usuarioOld = persistentSolicitud.getUsuario();
            Usuario usuarioNew = solicitud.getUsuario();
            Soporte tecnicoOld = persistentSolicitud.getTecnico();
            Soporte tecnicoNew = solicitud.getTecnico();
            Estado estadoOld = persistentSolicitud.getEstado();
            Estado estadoNew = solicitud.getEstado();
            List<Adjunto> adjuntoListOld = persistentSolicitud.getAdjuntoList();
            List<Adjunto> adjuntoListNew = solicitud.getAdjuntoList();
            List<String> illegalOrphanMessages = null;
            for (Adjunto adjuntoListOldAdjunto : adjuntoListOld) {
                if (!adjuntoListNew.contains(adjuntoListOldAdjunto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Adjunto " + adjuntoListOldAdjunto + " since its solicitud field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (dependenciaNew != null) {
                dependenciaNew = em.getReference(dependenciaNew.getClass(), dependenciaNew.getId());
                solicitud.setDependencia(dependenciaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                solicitud.setUsuario(usuarioNew);
            }
            if (tecnicoNew != null) {
                tecnicoNew = em.getReference(tecnicoNew.getClass(), tecnicoNew.getSoporte());
                solicitud.setTecnico(tecnicoNew);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getId());
                solicitud.setEstado(estadoNew);
            }
            List<Adjunto> attachedAdjuntoListNew = new ArrayList<Adjunto>();
            for (Adjunto adjuntoListNewAdjuntoToAttach : adjuntoListNew) {
                adjuntoListNewAdjuntoToAttach = em.getReference(adjuntoListNewAdjuntoToAttach.getClass(), adjuntoListNewAdjuntoToAttach.getId());
                attachedAdjuntoListNew.add(adjuntoListNewAdjuntoToAttach);
            }
            adjuntoListNew = attachedAdjuntoListNew;
            solicitud.setAdjuntoList(adjuntoListNew);
            solicitud = em.merge(solicitud);
            if (dependenciaOld != null && !dependenciaOld.equals(dependenciaNew)) {
                dependenciaOld.getSolicitudList().remove(solicitud);
                dependenciaOld = em.merge(dependenciaOld);
            }
            if (dependenciaNew != null && !dependenciaNew.equals(dependenciaOld)) {
                dependenciaNew.getSolicitudList().add(solicitud);
                dependenciaNew = em.merge(dependenciaNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getSolicitudList().remove(solicitud);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getSolicitudList().add(solicitud);
                usuarioNew = em.merge(usuarioNew);
            }
            if (tecnicoOld != null && !tecnicoOld.equals(tecnicoNew)) {
                tecnicoOld.getSolicitudList().remove(solicitud);
                tecnicoOld = em.merge(tecnicoOld);
            }
            if (tecnicoNew != null && !tecnicoNew.equals(tecnicoOld)) {
                tecnicoNew.getSolicitudList().add(solicitud);
                tecnicoNew = em.merge(tecnicoNew);
            }
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getSolicitudList().remove(solicitud);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getSolicitudList().add(solicitud);
                estadoNew = em.merge(estadoNew);
            }
            for (Adjunto adjuntoListNewAdjunto : adjuntoListNew) {
                if (!adjuntoListOld.contains(adjuntoListNewAdjunto)) {
                    Solicitud oldSolicitudOfAdjuntoListNewAdjunto = adjuntoListNewAdjunto.getSolicitud();
                    adjuntoListNewAdjunto.setSolicitud(solicitud);
                    adjuntoListNewAdjunto = em.merge(adjuntoListNewAdjunto);
                    if (oldSolicitudOfAdjuntoListNewAdjunto != null && !oldSolicitudOfAdjuntoListNewAdjunto.equals(solicitud)) {
                        oldSolicitudOfAdjuntoListNewAdjunto.getAdjuntoList().remove(adjuntoListNewAdjunto);
                        oldSolicitudOfAdjuntoListNewAdjunto = em.merge(oldSolicitudOfAdjuntoListNewAdjunto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = solicitud.getId();
                if (findSolicitud(id) == null) {
                    throw new NonexistentEntityException("The solicitud with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitud solicitud;
            try {
                solicitud = em.getReference(Solicitud.class, id);
                solicitud.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The solicitud with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Adjunto> adjuntoListOrphanCheck = solicitud.getAdjuntoList();
            for (Adjunto adjuntoListOrphanCheckAdjunto : adjuntoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Solicitud (" + solicitud + ") cannot be destroyed since the Adjunto " + adjuntoListOrphanCheckAdjunto + " in its adjuntoList field has a non-nullable solicitud field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Dependencia dependencia = solicitud.getDependencia();
            if (dependencia != null) {
                dependencia.getSolicitudList().remove(solicitud);
                dependencia = em.merge(dependencia);
            }
            Usuario usuario = solicitud.getUsuario();
            if (usuario != null) {
                usuario.getSolicitudList().remove(solicitud);
                usuario = em.merge(usuario);
            }
            Soporte tecnico = solicitud.getTecnico();
            if (tecnico != null) {
                tecnico.getSolicitudList().remove(solicitud);
                tecnico = em.merge(tecnico);
            }
            Estado estado = solicitud.getEstado();
            if (estado != null) {
                estado.getSolicitudList().remove(solicitud);
                estado = em.merge(estado);
            }
            em.remove(solicitud);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Solicitud> findSolicitudEntities() {
        return findSolicitudEntities(true, -1, -1);
    }

    public List<Solicitud> findSolicitudEntities(int maxResults, int firstResult) {
        return findSolicitudEntities(false, maxResults, firstResult);
    }

    private List<Solicitud> findSolicitudEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Solicitud.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Solicitud findSolicitud(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Solicitud.class, id);
        } finally {
            em.close();
        }
    }

    public int getSolicitudCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Solicitud> rt = cq.from(Solicitud.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
