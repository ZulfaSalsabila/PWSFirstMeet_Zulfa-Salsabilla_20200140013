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
@Table(name = "transaksi")
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByIdTransaksi", query = "SELECT t FROM Transaksi t WHERE t.idTransaksi = :idTransaksi"),
    @NamedQuery(name = "Transaksi.findByTanggalTransaksi", query = "SELECT t FROM Transaksi t WHERE t.tanggalTransaksi = :tanggalTransaksi"),
    @NamedQuery(name = "Transaksi.findByTotalTransaksi", query = "SELECT t FROM Transaksi t WHERE t.totalTransaksi = :totalTransaksi"),
    @NamedQuery(name = "Transaksi.findByMetodeTransaksi", query = "SELECT t FROM Transaksi t WHERE t.metodeTransaksi = :metodeTransaksi")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_transaksi")
    private Integer idTransaksi;
    @Basic(optional = false)
    @Column(name = "tanggal_transaksi")
    private String tanggalTransaksi;
    @Basic(optional = false)
    @Column(name = "total_transaksi")
    private int totalTransaksi;
    @Basic(optional = false)
    @Column(name = "metode_transaksi")
    private String metodeTransaksi;
    @JoinColumn(name = "id_pembeli", referencedColumnName = "id_pembeli")
    @OneToOne(optional = false)
    private Pembeli idPembeli;

    public Transaksi() {
    }

    public Transaksi(Integer idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Transaksi(Integer idTransaksi, String tanggalTransaksi, int totalTransaksi, String metodeTransaksi) {
        this.idTransaksi = idTransaksi;
        this.tanggalTransaksi = tanggalTransaksi;
        this.totalTransaksi = totalTransaksi;
        this.metodeTransaksi = metodeTransaksi;
    }

    public Integer getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(Integer idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(String tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(int totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    public String getMetodeTransaksi() {
        return metodeTransaksi;
    }

    public void setMetodeTransaksi(String metodeTransaksi) {
        this.metodeTransaksi = metodeTransaksi;
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
        hash += (idTransaksi != null ? idTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.idTransaksi == null && other.idTransaksi != null) || (this.idTransaksi != null && !this.idTransaksi.equals(other.idTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokobabe.tokobabe.Transaksi[ idTransaksi=" + idTransaksi + " ]";
    }
    
}
