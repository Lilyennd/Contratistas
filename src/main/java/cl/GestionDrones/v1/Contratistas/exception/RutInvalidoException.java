package cl.GestionDrones.v1.Contratistas.exception;

public class RutInvalidoException extends RuntimeException {
    
    private final String rut;

    public RutInvalidoException(String rut) {
        super(String.format("El RUT '%s' no es válido o no arrojó resultados en el sistema.", rut));
        this.rut = rut;
    }

    public RutInvalidoException(String rut, String mensajePersonalizado) {
        super(mensajePersonalizado);
        this.rut = rut;
    }

    public String getRut() {
        return rut;
    }
}
