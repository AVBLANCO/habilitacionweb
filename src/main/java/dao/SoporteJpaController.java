/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Usuario;
import dto.Solicitud;
import dto.Soporte;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class SoporteJpaController implements Serializable {

    public SoporteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Soporte soporte) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (soporte.getSolicitudList() == null) {
            soporte.setSolicitudList(new ArrayList<Solicitud>());
        }
        List<String> illegalOrphanMessages = null;
        Usuario usuarioOrphanCheck = soporte.getUsuario();
        if (usuarioOrphanCheck != null) {
            Soporte oldSoporteOfUsuario = usuarioOrphanCheck.getSoporte();
            if (oldSoporteOfUsuario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuario " + usuarioOrphanCheck + " already has an item of type Soporte whose usuario column cannot be null. Please make another selection for the usuario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = soporte.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                soporte.setUsuario(usuario);
            }
            List<Solicitud> attachedSolicitudList = new ArrayList<Solicitud>();
            for (Solicitud solicitudListSolicitudToAttach : soporte.getSolicitudList()) {
                solicitudListSolicitudToAttach = em.getReference(solicitudListSolicitudToAttach.getClass(), solicitudListSolicitudToAttach.getId());
                attachedSolicitudList.add(solicitudListSolicitudToAttach);
            }
            soporte.setSolicitudList(attachedSolicitudList);
            em.persist(soporte);
            if (usuario != null) {
                usuario.setSoporte(soporte);
                usuario = em.merge(usuario);
            }
            for (Solicitud solicitudListSolicitud : soporte.getSolicitudList()) {
                Soporte oldTecnicoOfSolicitudListSolicitud = solicitudListSolicitud.getTecnico();
                solicitudListSolicitud.setTecnico(soporte);
                solicitudListSolicitud = em.merge(solicitudListSolicitud);
                if (oldTecnicoOfSolicitudListSolicitud != null) {
                    oldTecnicoOfSolicitudListSolicitud.getSolicitudList().remove(solicitudListSolicitud);
                    oldTecnicoOfSolicitudListSolicitud = em.merge(oldTecnicoOfSolicitudListSolicitud);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSoporte(soporte.getSoporte()) != null) {
                throw new PreexistingEntityException("Soporte " + soporte + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Soporte soporte) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Soporte persistentSoporte = em.find(Soporte.class, soporte.getSoporte());
            Usuario usuarioOld = persistentSoporte.getUsuario();
            Usuario usuarioNew = soporte.getUsuario();
            List<Solicitud> solicitudListOld = persistentSoporte.getSolicitudList();
            List<Solicitud> solicitudListNew = soporte.getSolicitudList();
            List<String> illegalOrphanMessages = null;
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                Soporte oldSoporteOfUsuario = usuarioNew.getSoporte();
                if (oldSoporteOfUsuario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuario " + usuarioNew + " already has an item of type Soporte whose usuario column cannot be null. Please make another selection for the usuario field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                soporte.setUsuario(usuarioNew);
            }
            List<Solicitud> attachedSolicitudListNew = new ArrayList<Solicitud>();
            for (Solicitud solicitudListNewSolicitudToAttach : solicitudListNew) {
                solicitudListNewSolicitudToAttach = em.getReference(solicitudListNewSolicitudToAttach.getClass(), solicitudListNewSolicitudToAttach.getId());
                attachedSolicitudListNew.add(solicitudListNewSolicitudToAttach);
            }
            solicitudListNew = attachedSolicitudListNew;
            soporte.setSolicitudList(solicitudListNew);
            soporte = em.merge(soporte);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.setSoporte(null);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.setSoporte(soporte);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Solicitud solicitudListOldSolicitud : solicitudListOld) {
                if (!solicitudListNew.contains(solicitudListOldSolicitud)) {
                    solicitudListOldSolicitud.setTecnico(null);
                    solicitudListOldSolicitud = em.merge(solicitudListOldSolicitud);
                }
            }
            for (Solicitud solicitudListNewSolicitud : solicitudListNew) {
                if (!solicitudListOld.contains(solicitudListNewSolicitud)) {
                    Soporte oldTecnicoOfSolicitudListNewSolicitud = solicitudListNewSolicitud.getTecnico();
                    solicitudListNewSolicitud.setTecnico(soporte);
                    solicitudListNewSolicitud = em.merge(solicitudListNewSolicitud);
                    if (oldTecnicoOfSolicitudListNewSolicitud != null && !oldTecnicoOfSolicitudListNewSolicitud.equals(soporte)) {
                        oldTecnicoOfSolicitudListNewSolicitud.getSolicitudList().remove(solicitudListNewSolicitud);
                        oldTecnicoOfSolicitudListNewSolicitud = em.merge(oldTecnicoOfSolicitudListNewSolicitud);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = soporte.getSoporte();
                if (findSoporte(id) == null) {
                    throw new NonexistentEntityException("The soporte with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Soporte soporte;
            try {
                soporte = em.getReference(Soporte.class, id);
                soporte.getSoporte();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The soporte with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = soporte.getUsuario();
            if (usuario != null) {
                usuario.setSoporte(null);
                usuario = em.merge(usuario);
            }
            List<Solicitud> solicitudList = soporte.getSolicitudList();
            for (Solicitud solicitudListSolicitud : solicitudList) {
                solicitudListSolicitud.setTecnico(null);
                solicitudListSolicitud = em.merge(solicitudListSolicitud);
            }
            em.remove(soporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Soporte> findSoporteEntities() {
        return findSoporteEntities(true, -1, -1);
    }

    public List<Soporte> findSoporteEntities(int maxResults, int firstResult) {
        return findSoporteEntities(false, maxResults, firstResult);
    }

    private List<Soporte> findSoporteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Soporte.class));
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

    public Soporte findSoporte(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Soporte.class, id);
        } finally {
            em.close();
        }
    }

    public int getSoporteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Soporte> rt = cq.from(Soporte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
