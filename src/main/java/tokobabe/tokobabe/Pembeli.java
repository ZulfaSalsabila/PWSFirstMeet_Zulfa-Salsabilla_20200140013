/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tokobabe.tokobabe;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author ROG
 */
@Entity
@Table(name = "pembeli")
@NamedQueries({
    @NamedQuery(name = "Pembeli.findAll", query = "SELECT p FROM Pembeli p"),
    @NamedQuery(name = "Pembeli.findByAlamatPembeli", query = "SELECT p FROM Pembeli p WHERE p.alamatPembeli = :alamatPembeli"),
    @NamedQuery(name = "Pembeli.findByNoTelfon", query = "SELECT p FROM Pembeli p WHERE p.noTelfon = :noTelfon"),
    @NamedQuery(name = "Pembeli.findByIdPembeli", query = "SELECT p FROM Pembeli p WHERE p.idPembeli = :idPembeli"),
    @NamedQuery(name = "Pembeli.findByNamaPembeli", query = "SELECT p FROM Pembeli p WHERE p.namaPembeli = :namaPembeli")})
public class Pembeli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "alamat_pembeli")
    private String alamatPembeli;
    @Basic(optional = false)
    @Column(name = "no_telfon")
    private String noTelfon;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pembeli")
    private String idPembeli;
    @Basic(optional = false)
    @Column(name = "nama_pembeli")
    private String namaPembeli;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pembeli")
    private Barang barang;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPembeli")
    private Transaksi transaksi;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPembeli")
    private Beli beli;

    public Pembeli() {
    }

    public Pembeli(String idPembeli) {
        this.idPembeli = idPembeli;
    }

    public Pembeli(String idPembeli, String alamatPembeli, String noTelfon, String namaPembeli) {
        this.idPembeli = idPembeli;
        this.alamatPembeli = alamatPembeli;
        this.noTelfon = noTelfon;
        this.namaPembeli = namaPembeli;
    }

    public String getAlamatPembeli() {
        return alamatPembeli;
    }

    public void setAlamatPembeli(String alamatPembeli) {
        this.alamatPembeli = alamatPembeli;
    }

    public String getNoTelfon() {
        return noTelfon;
    }

    public void setNoTelfon(String noTelfon) {
        this.noTelfon = noTelfon;
    }

    public String getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(String idPembeli) {
        this.idPembeli = idPembeli;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public Beli getBeli() {
        return beli;
    }

    public void setBeli(Beli beli) {
        this.beli = beli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPembeli != null ? idPembeli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pembeli)) {
            return false;
        }
        Pembeli other = (Pembeli) object;
        if ((this.idPembeli == null && other.idPembeli != null) || (this.idPembeli != null && !this.idPembeli.equals(other.idPembeli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tokobabe.tokobabe.Pembeli[ idPembeli=" + idPembeli + " ]";
    }
    
}
