/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dto.Adjunto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Solicitud;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class AdjuntoJpaController implements Serializable {

    public AdjuntoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Adjunto adjunto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitud solicitud = adjunto.getSolicitud();
            if (solicitud != null) {
                solicitud = em.getReference(solicitud.getClass(), solicitud.getId());
                adjunto.setSolicitud(solicitud);
            }
            em.persist(adjunto);
            if (solicitud != null) {
                solicitud.getAdjuntoList().add(adjunto);
                solicitud = em.merge(solicitud);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Adjunto adjunto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adjunto persistentAdjunto = em.find(Adjunto.class, adjunto.getId());
            Solicitud solicitudOld = persistentAdjunto.getSolicitud();
            Solicitud solicitudNew = adjunto.getSolicitud();
            if (solicitudNew != null) {
                solicitudNew = em.getReference(solicitudNew.getClass(), solicitudNew.getId());
                adjunto.setSolicitud(solicitudNew);
            }
            adjunto = em.merge(adjunto);
            if (solicitudOld != null && !solicitudOld.equals(solicitudNew)) {
                solicitudOld.getAdjuntoList().remove(adjunto);
                solicitudOld = em.merge(solicitudOld);
            }
            if (solicitudNew != null && !solicitudNew.equals(solicitudOld)) {
                solicitudNew.getAdjuntoList().add(adjunto);
                solicitudNew = em.merge(solicitudNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = adjunto.getId();
                if (findAdjunto(id) == null) {
                    throw new NonexistentEntityException("The adjunto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adjunto adjunto;
            try {
                adjunto = em.getReference(Adjunto.class, id);
                adjunto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adjunto with id " + id + " no longer exists.", enfe);
            }
            Solicitud solicitud = adjunto.getSolicitud();
            if (solicitud != null) {
                solicitud.getAdjuntoList().remove(adjunto);
                solicitud = em.merge(solicitud);
            }
            em.remove(adjunto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Adjunto> findAdjuntoEntities() {
        return findAdjuntoEntities(true, -1, -1);
    }

    public List<Adjunto> findAdjuntoEntities(int maxResults, int firstResult) {
        return findAdjuntoEntities(false, maxResults, firstResult);
    }

    private List<Adjunto> findAdjuntoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Adjunto.class));
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

    public Adjunto findAdjunto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Adjunto.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdjuntoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Adjunto> rt = cq.from(Adjunto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
