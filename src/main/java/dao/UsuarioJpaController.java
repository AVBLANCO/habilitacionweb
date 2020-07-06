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
import dto.Soporte;
import dto.Solicitud;
import dto.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getSolicitudList() == null) {
            usuario.setSolicitudList(new ArrayList<Solicitud>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Soporte soporte = usuario.getSoporte();
            if (soporte != null) {
                soporte = em.getReference(soporte.getClass(), soporte.getSoporte());
                usuario.setSoporte(soporte);
            }
            List<Solicitud> attachedSolicitudList = new ArrayList<Solicitud>();
            for (Solicitud solicitudListSolicitudToAttach : usuario.getSolicitudList()) {
                solicitudListSolicitudToAttach = em.getReference(solicitudListSolicitudToAttach.getClass(), solicitudListSolicitudToAttach.getId());
                attachedSolicitudList.add(solicitudListSolicitudToAttach);
            }
            usuario.setSolicitudList(attachedSolicitudList);
            em.persist(usuario);
            if (soporte != null) {
                Usuario oldUsuarioOfSoporte = soporte.getUsuario();
                if (oldUsuarioOfSoporte != null) {
                    oldUsuarioOfSoporte.setSoporte(null);
                    oldUsuarioOfSoporte = em.merge(oldUsuarioOfSoporte);
                }
                soporte.setUsuario(usuario);
                soporte = em.merge(soporte);
            }
            for (Solicitud solicitudListSolicitud : usuario.getSolicitudList()) {
                Usuario oldUsuarioOfSolicitudListSolicitud = solicitudListSolicitud.getUsuario();
                solicitudListSolicitud.setUsuario(usuario);
                solicitudListSolicitud = em.merge(solicitudListSolicitud);
                if (oldUsuarioOfSolicitudListSolicitud != null) {
                    oldUsuarioOfSolicitudListSolicitud.getSolicitudList().remove(solicitudListSolicitud);
                    oldUsuarioOfSolicitudListSolicitud = em.merge(oldUsuarioOfSolicitudListSolicitud);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuario());
            Soporte soporteOld = persistentUsuario.getSoporte();
            Soporte soporteNew = usuario.getSoporte();
            List<Solicitud> solicitudListOld = persistentUsuario.getSolicitudList();
            List<Solicitud> solicitudListNew = usuario.getSolicitudList();
            List<String> illegalOrphanMessages = null;
            if (soporteOld != null && !soporteOld.equals(soporteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Soporte " + soporteOld + " since its usuario field is not nullable.");
            }
            for (Solicitud solicitudListOldSolicitud : solicitudListOld) {
                if (!solicitudListNew.contains(solicitudListOldSolicitud)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Solicitud " + solicitudListOldSolicitud + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (soporteNew != null) {
                soporteNew = em.getReference(soporteNew.getClass(), soporteNew.getSoporte());
                usuario.setSoporte(soporteNew);
            }
            List<Solicitud> attachedSolicitudListNew = new ArrayList<Solicitud>();
            for (Solicitud solicitudListNewSolicitudToAttach : solicitudListNew) {
                solicitudListNewSolicitudToAttach = em.getReference(solicitudListNewSolicitudToAttach.getClass(), solicitudListNewSolicitudToAttach.getId());
                attachedSolicitudListNew.add(solicitudListNewSolicitudToAttach);
            }
            solicitudListNew = attachedSolicitudListNew;
            usuario.setSolicitudList(solicitudListNew);
            usuario = em.merge(usuario);
            if (soporteNew != null && !soporteNew.equals(soporteOld)) {
                Usuario oldUsuarioOfSoporte = soporteNew.getUsuario();
                if (oldUsuarioOfSoporte != null) {
                    oldUsuarioOfSoporte.setSoporte(null);
                    oldUsuarioOfSoporte = em.merge(oldUsuarioOfSoporte);
                }
                soporteNew.setUsuario(usuario);
                soporteNew = em.merge(soporteNew);
            }
            for (Solicitud solicitudListNewSolicitud : solicitudListNew) {
                if (!solicitudListOld.contains(solicitudListNewSolicitud)) {
                    Usuario oldUsuarioOfSolicitudListNewSolicitud = solicitudListNewSolicitud.getUsuario();
                    solicitudListNewSolicitud.setUsuario(usuario);
                    solicitudListNewSolicitud = em.merge(solicitudListNewSolicitud);
                    if (oldUsuarioOfSolicitudListNewSolicitud != null && !oldUsuarioOfSolicitudListNewSolicitud.equals(usuario)) {
                        oldUsuarioOfSolicitudListNewSolicitud.getSolicitudList().remove(solicitudListNewSolicitud);
                        oldUsuarioOfSolicitudListNewSolicitud = em.merge(oldUsuarioOfSolicitudListNewSolicitud);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Soporte soporteOrphanCheck = usuario.getSoporte();
            if (soporteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Soporte " + soporteOrphanCheck + " in its soporte field has a non-nullable usuario field.");
            }
            List<Solicitud> solicitudListOrphanCheck = usuario.getSolicitudList();
            for (Solicitud solicitudListOrphanCheckSolicitud : solicitudListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Solicitud " + solicitudListOrphanCheckSolicitud + " in its solicitudList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
