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
public class PembeliJpaController implements Serializable {

    public PembeliJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("tokobabe_tokobabe_jar_0.0.1-SNAPSHOTPU");

    public PembeliJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pembeli pembeli) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang barang = pembeli.getBarang();
            if (barang != null) {
                barang = em.getReference(barang.getClass(), barang.getIdBarang());
                pembeli.setBarang(barang);
            }
            Transaksi transaksi = pembeli.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getIdTransaksi());
                pembeli.setTransaksi(transaksi);
            }
            Beli beli = pembeli.getBeli();
            if (beli != null) {
                beli = em.getReference(beli.getClass(), beli.getIdBeli());
                pembeli.setBeli(beli);
            }
            em.persist(pembeli);
            if (barang != null) {
                Pembeli oldPembeliOfBarang = barang.getPembeli();
                if (oldPembeliOfBarang != null) {
                    oldPembeliOfBarang.setBarang(null);
                    oldPembeliOfBarang = em.merge(oldPembeliOfBarang);
                }
                barang.setPembeli(pembeli);
                barang = em.merge(barang);
            }
            if (transaksi != null) {
                Pembeli oldIdPembeliOfTransaksi = transaksi.getIdPembeli();
                if (oldIdPembeliOfTransaksi != null) {
                    oldIdPembeliOfTransaksi.setTransaksi(null);
                    oldIdPembeliOfTransaksi = em.merge(oldIdPembeliOfTransaksi);
                }
                transaksi.setIdPembeli(pembeli);
                transaksi = em.merge(transaksi);
            }
            if (beli != null) {
                Pembeli oldIdPembeliOfBeli = beli.getIdPembeli();
                if (oldIdPembeliOfBeli != null) {
                    oldIdPembeliOfBeli.setBeli(null);
                    oldIdPembeliOfBeli = em.merge(oldIdPembeliOfBeli);
                }
                beli.setIdPembeli(pembeli);
                beli = em.merge(beli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPembeli(pembeli.getIdPembeli()) != null) {
                throw new PreexistingEntityException("Pembeli " + pembeli + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pembeli pembeli) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli persistentPembeli = em.find(Pembeli.class, pembeli.getIdPembeli());
            Barang barangOld = persistentPembeli.getBarang();
            Barang barangNew = pembeli.getBarang();
            Transaksi transaksiOld = persistentPembeli.getTransaksi();
            Transaksi transaksiNew = pembeli.getTransaksi();
            Beli beliOld = persistentPembeli.getBeli();
            Beli beliNew = pembeli.getBeli();
            List<String> illegalOrphanMessages = null;
            if (barangOld != null && !barangOld.equals(barangNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Barang " + barangOld + " since its pembeli field is not nullable.");
            }
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Transaksi " + transaksiOld + " since its idPembeli field is not nullable.");
            }
            if (beliOld != null && !beliOld.equals(beliNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Beli " + beliOld + " since its idPembeli field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (barangNew != null) {
                barangNew = em.getReference(barangNew.getClass(), barangNew.getIdBarang());
                pembeli.setBarang(barangNew);
            }
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getIdTransaksi());
                pembeli.setTransaksi(transaksiNew);
            }
            if (beliNew != null) {
                beliNew = em.getReference(beliNew.getClass(), beliNew.getIdBeli());
                pembeli.setBeli(beliNew);
            }
            pembeli = em.merge(pembeli);
            if (barangNew != null && !barangNew.equals(barangOld)) {
                Pembeli oldPembeliOfBarang = barangNew.getPembeli();
                if (oldPembeliOfBarang != null) {
                    oldPembeliOfBarang.setBarang(null);
                    oldPembeliOfBarang = em.merge(oldPembeliOfBarang);
                }
                barangNew.setPembeli(pembeli);
                barangNew = em.merge(barangNew);
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                Pembeli oldIdPembeliOfTransaksi = transaksiNew.getIdPembeli();
                if (oldIdPembeliOfTransaksi != null) {
                    oldIdPembeliOfTransaksi.setTransaksi(null);
                    oldIdPembeliOfTransaksi = em.merge(oldIdPembeliOfTransaksi);
                }
                transaksiNew.setIdPembeli(pembeli);
                transaksiNew = em.merge(transaksiNew);
            }
            if (beliNew != null && !beliNew.equals(beliOld)) {
                Pembeli oldIdPembeliOfBeli = beliNew.getIdPembeli();
                if (oldIdPembeliOfBeli != null) {
                    oldIdPembeliOfBeli.setBeli(null);
                    oldIdPembeliOfBeli = em.merge(oldIdPembeliOfBeli);
                }
                beliNew.setIdPembeli(pembeli);
                beliNew = em.merge(beliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pembeli.getIdPembeli();
                if (findPembeli(id) == null) {
                    throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.");
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
            Pembeli pembeli;
            try {
                pembeli = em.getReference(Pembeli.class, id);
                pembeli.getIdPembeli();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Barang barangOrphanCheck = pembeli.getBarang();
            if (barangOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Barang " + barangOrphanCheck + " in its barang field has a non-nullable pembeli field.");
            }
            Transaksi transaksiOrphanCheck = pembeli.getTransaksi();
            if (transaksiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Transaksi " + transaksiOrphanCheck + " in its transaksi field has a non-nullable idPembeli field.");
            }
            Beli beliOrphanCheck = pembeli.getBeli();
            if (beliOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Beli " + beliOrphanCheck + " in its beli field has a non-nullable idPembeli field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pembeli);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pembeli> findPembeliEntities() {
        return findPembeliEntities(true, -1, -1);
    }

    public List<Pembeli> findPembeliEntities(int maxResults, int firstResult) {
        return findPembeliEntities(false, maxResults, firstResult);
    }

    private List<Pembeli> findPembeliEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pembeli.class));
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

    public Pembeli findPembeli(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pembeli.class, id);
        } finally {
            em.close();
        }
    }

    public int getPembeliCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pembeli> rt = cq.from(Pembeli.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
