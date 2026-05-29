package cl.GestionDrones.v1.Contratistas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas_contratistas")
public class Contratista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "rut", nullable = false, unique = true, length = 15)
    private String rut; 

    @Column(name = "nombreEmpresa", nullable = false, length = 150)
    private String nombreEmpresa; 

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono; 

    @Column(name = "contacto_email", nullable = false, length = 100)
    private String contactoEmail;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; 

  
    public Contratista() {
    }


    public Contratista(long id, long idEmpresaProveedora, String rut, String nombreEmpresa, String telefono, String contactoEmail, String estado) {
        this.id = id;
        this.rut = rut;
        this.nombreEmpresa = nombreEmpresa;
        this.telefono = telefono;
        this.contactoEmail = contactoEmail;
        this.estado = estado;
    }

   
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getnombreEmpresa() {
        return nombreEmpresa;
    }

    public void setnombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}