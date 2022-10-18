/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobabe.tokobabe;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author ROG
 */
@Entity
@Table(name = "beli")
@NamedQueries({
    @NamedQuery(name = "Beli.findAll", query = "SELECT b FROM Beli b"),
    @NamedQuery(name = "Beli.findByIdBeli", query = "SELECT b FROM Beli b WHERE b.idBeli = :idBeli"),
    @NamedQuery(name = "Beli.findByQty", query = "SELECT b FROM Beli b WHERE b.qty = :qty"),
    @NamedQuery(name = "Beli.findByTotalHarga", query = "SELECT b FROM Beli b WHERE b.totalHarga = :totalHarga")})
public class Beli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_beli")
    private Integer idBeli;
    @Basic(optional = false)
    @Column(name = "QTY")
    private int qty;
    @Basic(optional = false)
    @Column(name = "total_harga")
    private String totalHarga;
    @JoinColumn(name = "id_baranag", referencedColumnName = "id_barang")
    @OneToOne(optional = false)
    private Barang idBaranag;
    @JoinColumn(name = "id_pembeli", referencedColumnName = "id_pembeli")
    @OneToOne(optional = false)
    private Pembeli idPembeli;

    public Beli() {
    }

    public Beli(Integer idBeli) {
        this.idBeli = idBeli;
    }

    public Beli(Integer idBeli, int qty, String totalHarga) {
        this.idBeli = idBeli;
        this.qty = qty;
        this.totalHarga = totalHarga;
    }

    public Integer getIdBeli() {
        return idBeli;
    }

    public void setIdBeli(Integer idBeli) {
        this.idBeli = idBeli;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public Barang getIdBaranag() {
        return idBaranag;
    }

    public void setIdBaranag(Barang idBaranag) {
        this.idBaranag = idBaranag;
    }

    public Pembeli getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(Pembeli idPembeli) {
        this.idPembeli = idPembeli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBeli != null ? idBeli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Beli)) {
            return false;
        }
        Beli other = (Beli) object;
        if ((this.idBeli == null && other.idBeli != null) || (this.idBeli != null && !this.idBeli.equals(other.idBeli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokobabe.tokobabe.Beli[ idBeli=" + idBeli + " ]";
    }
    
}
