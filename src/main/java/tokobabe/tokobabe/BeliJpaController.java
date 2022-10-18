/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobabe.tokobabe;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import tokobabe.tokobabe.exceptions.IllegalOrphanException;
import tokobabe.tokobabe.exceptions.NonexistentEntityException;
import tokobabe.tokobabe.exceptions.PreexistingEntityException;

/**
 *
 * @author ROG
 */
public class BeliJpaController implements Serializable {

    public BeliJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tokobabe_tokobabe_jar_0.0.1-SNAPSHOTPU");

    public BeliJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Beli beli) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Barang idBaranagOrphanCheck = beli.getIdBaranag();
        if (idBaranagOrphanCheck != null) {
            Beli oldBeliOfIdBaranag = idBaranagOrphanCheck.getBeli();
            if (oldBeliOfIdBaranag != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Barang " + idBaranagOrphanCheck + " already has an item of type Beli whose idBaranag column cannot be null. Please make another selection for the idBaranag field.");
            }
        }
        Pembeli idPembeliOrphanCheck = beli.getIdPembeli();
        if (idPembeliOrphanCheck != null) {
            Beli oldBeliOfIdPembeli = idPembeliOrphanCheck.getBeli();
            if (oldBeliOfIdPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + idPembeliOrphanCheck + " already has an item of type Beli whose idPembeli column cannot be null. Please make another selection for the idPembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang idBaranag = beli.getIdBaranag();
            if (idBaranag != null) {
                idBaranag = em.getReference(idBaranag.getClass(), idBaranag.getIdBarang());
                beli.setIdBaranag(idBaranag);
            }
            Pembeli idPembeli = beli.getIdPembeli();
            if (idPembeli != null) {
                idPembeli = em.getReference(idPembeli.getClass(), idPembeli.getIdPembeli());
                beli.setIdPembeli(idPembeli);
            }
            em.persist(beli);
            if (idBaranag != null) {
                idBaranag.setBeli(beli);
                idBaranag = em.merge(idBaranag);
            }
            if (idPembeli != null) {
                idPembeli.setBeli(beli);
                idPembeli = em.merge(idPembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBeli(beli.getIdBeli()) != null) {
                throw new PreexistingEntityException("Beli " + beli + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Beli beli) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Beli persistentBeli = em.find(Beli.class, beli.getIdBeli());
            Barang idBaranagOld = persistentBeli.getIdBaranag();
            Barang idBaranagNew = beli.getIdBaranag();
            Pembeli idPembeliOld = persistentBeli.getIdPembeli();
            Pembeli idPembeliNew = beli.getIdPembeli();
            List<String> illegalOrphanMessages = null;
            if (idBaranagNew != null && !idBaranagNew.equals(idBaranagOld)) {
                Beli oldBeliOfIdBaranag = idBaranagNew.getBeli();
                if (oldBeliOfIdBaranag != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Barang " + idBaranagNew + " already has an item of type Beli whose idBaranag column cannot be null. Please make another selection for the idBaranag field.");
                }
            }
            if (idPembeliNew != null && !idPembeliNew.equals(idPembeliOld)) {
                Beli oldBeliOfIdPembeli = idPembeliNew.getBeli();
                if (oldBeliOfIdPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + idPembeliNew + " already has an item of type Beli whose idPembeli column cannot be null. Please make another selection for the idPembeli field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idBaranagNew != null) {
                idBaranagNew = em.getReference(idBaranagNew.getClass(), idBaranagNew.getIdBarang());
                beli.setIdBaranag(idBaranagNew);
            }
            if (idPembeliNew != null) {
                idPembeliNew = em.getReference(idPembeliNew.getClass(), idPembeliNew.getIdPembeli());
                beli.setIdPembeli(idPembeliNew);
            }
            beli = em.merge(beli);
            if (idBaranagOld != null && !idBaranagOld.equals(idBaranagNew)) {
                idBaranagOld.setBeli(null);
                idBaranagOld = em.merge(idBaranagOld);
            }
            if (idBaranagNew != null && !idBaranagNew.equals(idBaranagOld)) {
                idBaranagNew.setBeli(beli);
                idBaranagNew = em.merge(idBaranagNew);
            }
            if (idPembeliOld != null && !idPembeliOld.equals(idPembeliNew)) {
                idPembeliOld.setBeli(null);
                idPembeliOld = em.merge(idPembeliOld);
            }
            if (idPembeliNew != null && !idPembeliNew.equals(idPembeliOld)) {
                idPembeliNew.setBeli(beli);
                idPembeliNew = em.merge(idPembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = beli.getIdBeli();
                if (findBeli(id) == null) {
                    throw new NonexistentEntityException("The beli with id " + id + " no longer exists.");
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
            Beli beli;
            try {
                beli = em.getReference(Beli.class, id);
                beli.getIdBeli();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The beli with id " + id + " no longer exists.", enfe);
            }
            Barang idBaranag = beli.getIdBaranag();
            if (idBaranag != null) {
                idBaranag.setBeli(null);
                idBaranag = em.merge(idBaranag);
            }
            Pembeli idPembeli = beli.getIdPembeli();
            if (idPembeli != null) {
                idPembeli.setBeli(null);
                idPembeli = em.merge(idPembeli);
            }
            em.remove(beli);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Beli> findBeliEntities() {
        return findBeliEntities(true, -1, -1);
    }

    public List<Beli> findBeliEntities(int maxResults, int firstResult) {
        return findBeliEntities(false, maxResults, firstResult);
    }

    private List<Beli> findBeliEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Beli.class));
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

    public Beli findBeli(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Beli.class, id);
        } finally {
            em.close();
        }
    }

    public int getBeliCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Beli> rt = cq.from(Beli.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
