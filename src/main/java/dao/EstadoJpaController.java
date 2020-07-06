/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Estado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Solicitud;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) {
        if (estado.getSolicitudList() == null) {
            estado.setSolicitudList(new ArrayList<Solicitud>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Solicitud> attachedSolicitudList = new ArrayList<Solicitud>();
            for (Solicitud solicitudListSolicitudToAttach : estado.getSolicitudList()) {
                solicitudListSolicitudToAttach = em.getReference(solicitudListSolicitudToAttach.getClass(), solicitudListSolicitudToAttach.getId());
                attachedSolicitudList.add(solicitudListSolicitudToAttach);
            }
            estado.setSolicitudList(attachedSolicitudList);
            em.persist(estado);
            for (Solicitud solicitudListSolicitud : estado.getSolicitudList()) {
                Estado oldEstadoOfSolicitudListSolicitud = solicitudListSolicitud.getEstado();
                solicitudListSolicitud.setEstado(estado);
                solicitudListSolicitud = em.merge(solicitudListSolicitud);
                if (oldEstadoOfSolicitudListSolicitud != null) {
                    oldEstadoOfSolicitudListSolicitud.getSolicitudList().remove(solicitudListSolicitud);
                    oldEstadoOfSolicitudListSolicitud = em.merge(oldEstadoOfSolicitudListSolicitud);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getId());
            List<Solicitud> solicitudListOld = persistentEstado.getSolicitudList();
            List<Solicitud> solicitudListNew = estado.getSolicitudList();
            List<String> illegalOrphanMessages = null;
            for (Solicitud solicitudListOldSolicitud : solicitudListOld) {
                if (!solicitudListNew.contains(solicitudListOldSolicitud)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Solicitud " + solicitudListOldSolicitud + " since its estado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Solicitud> attachedSolicitudListNew = new ArrayList<Solicitud>();
            for (Solicitud solicitudListNewSolicitudToAttach : solicitudListNew) {
                solicitudListNewSolicitudToAttach = em.getReference(solicitudListNewSolicitudToAttach.getClass(), solicitudListNewSolicitudToAttach.getId());
                attachedSolicitudListNew.add(solicitudListNewSolicitudToAttach);
            }
            solicitudListNew = attachedSolicitudListNew;
            estado.setSolicitudList(solicitudListNew);
            estado = em.merge(estado);
            for (Solicitud solicitudListNewSolicitud : solicitudListNew) {
                if (!solicitudListOld.contains(solicitudListNewSolicitud)) {
                    Estado oldEstadoOfSolicitudListNewSolicitud = solicitudListNewSolicitud.getEstado();
                    solicitudListNewSolicitud.setEstado(estado);
                    solicitudListNewSolicitud = em.merge(solicitudListNewSolicitud);
                    if (oldEstadoOfSolicitudListNewSolicitud != null && !oldEstadoOfSolicitudListNewSolicitud.equals(estado)) {
                        oldEstadoOfSolicitudListNewSolicitud.getSolicitudList().remove(solicitudListNewSolicitud);
                        oldEstadoOfSolicitudListNewSolicitud = em.merge(oldEstadoOfSolicitudListNewSolicitud);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getId();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Solicitud> solicitudListOrphanCheck = estado.getSolicitudList();
            for (Solicitud solicitudListOrphanCheckSolicitud : solicitudListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Solicitud " + solicitudListOrphanCheckSolicitud + " in its solicitudList field has a non-nullable estado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
