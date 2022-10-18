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
public class BarangJpaController implements Serializable {

    public BarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tokobabe_tokobabe_jar_0.0.1-SNAPSHOTPU");

    public BarangJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barang barang) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pembeli pembeliOrphanCheck = barang.getPembeli();
        if (pembeliOrphanCheck != null) {
            Barang oldBarangOfPembeli = pembeliOrphanCheck.getBarang();
            if (oldBarangOfPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + pembeliOrphanCheck + " already has an item of type Barang whose pembeli column cannot be null. Please make another selection for the pembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli pembeli = barang.getPembeli();
            if (pembeli != null) {
                pembeli = em.getReference(pembeli.getClass(), pembeli.getIdPembeli());
                barang.setPembeli(pembeli);
            }
            Beli beli = barang.getBeli();
            if (beli != null) {
                beli = em.getReference(beli.getClass(), beli.getIdBeli());
                barang.setBeli(beli);
            }
            em.persist(barang);
            if (pembeli != null) {
                pembeli.setBarang(barang);
                pembeli = em.merge(pembeli);
            }
            if (beli != null) {
                Barang oldIdBaranagOfBeli = beli.getIdBaranag();
                if (oldIdBaranagOfBeli != null) {
                    oldIdBaranagOfBeli.setBeli(null);
                    oldIdBaranagOfBeli = em.merge(oldIdBaranagOfBeli);
                }
                beli.setIdBaranag(barang);
                beli = em.merge(beli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBarang(barang.getIdBarang()) != null) {
                throw new PreexistingEntityException("Barang " + barang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barang barang) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang persistentBarang = em.find(Barang.class, barang.getIdBarang());
            Pembeli pembeliOld = persistentBarang.getPembeli();
            Pembeli pembeliNew = barang.getPembeli();
            Beli beliOld = persistentBarang.getBeli();
            Beli beliNew = barang.getBeli();
            List<String> illegalOrphanMessages = null;
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                Barang oldBarangOfPembeli = pembeliNew.getBarang();
                if (oldBarangOfPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + pembeliNew + " already has an item of type Barang whose pembeli column cannot be null. Please make another selection for the pembeli field.");
                }
            }
            if (beliOld != null && !beliOld.equals(beliNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Beli " + beliOld + " since its idBaranag field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pembeliNew != null) {
                pembeliNew = em.getReference(pembeliNew.getClass(), pembeliNew.getIdPembeli());
                barang.setPembeli(pembeliNew);
            }
            if (beliNew != null) {
                beliNew = em.getReference(beliNew.getClass(), beliNew.getIdBeli());
                barang.setBeli(beliNew);
            }
            barang = em.merge(barang);
            if (pembeliOld != null && !pembeliOld.equals(pembeliNew)) {
                pembeliOld.setBarang(null);
                pembeliOld = em.merge(pembeliOld);
            }
            if (pembeliNew != null && !pembeliNew.equals(pembeliOld)) {
                pembeliNew.setBarang(barang);
                pembeliNew = em.merge(pembeliNew);
            }
            if (beliNew != null && !beliNew.equals(beliOld)) {
                Barang oldIdBaranagOfBeli = beliNew.getIdBaranag();
                if (oldIdBaranagOfBeli != null) {
                    oldIdBaranagOfBeli.setBeli(null);
                    oldIdBaranagOfBeli = em.merge(oldIdBaranagOfBeli);
                }
                beliNew.setIdBaranag(barang);
                beliNew = em.merge(beliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = barang.getIdBarang();
                if (findBarang(id) == null) {
                    throw new NonexistentEntityException("The barang with id " + id + " no longer exists.");
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
            Barang barang;
            try {
                barang = em.getReference(Barang.class, id);
                barang.getIdBarang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barang with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Beli beliOrphanCheck = barang.getBeli();
            if (beliOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Barang (" + barang + ") cannot be destroyed since the Beli " + beliOrphanCheck + " in its beli field has a non-nullable idBaranag field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pembeli pembeli = barang.getPembeli();
            if (pembeli != null) {
                pembeli.setBarang(null);
                pembeli = em.merge(pembeli);
            }
            em.remove(barang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barang> findBarangEntities() {
        return findBarangEntities(true, -1, -1);
    }

    public List<Barang> findBarangEntities(int maxResults, int firstResult) {
        return findBarangEntities(false, maxResults, firstResult);
    }

    private List<Barang> findBarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barang.class));
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

    public Barang findBarang(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barang.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barang> rt = cq.from(Barang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
