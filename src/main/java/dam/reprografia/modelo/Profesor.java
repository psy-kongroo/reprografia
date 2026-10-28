package dam.reprografia.modelo;

/** Cliente profesor; conserva su departamento. */
public class Profesor extends Cliente {
    private final String departamento;

    public Profesor(String dni, String nombre, String departamento) {
        super(dni, nombre);
        this.departamento = textoObligatorio(departamento, "El departamento");
    }

    public String getDepartamento() {
        return departamento;
    }

    @Override
    public TipoCliente getTipo() {
        return TipoCliente.PROFESOR;
    }

    @Override
    public String toString() {
        return super.toString() + ", departamento: " + departamento;
    }
}
