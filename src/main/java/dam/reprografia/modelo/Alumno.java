package dam.reprografia.modelo;

/** Cliente alumno; conserva el curso al que pertenece. */
public class Alumno extends Cliente {
    private final String curso;

    public Alumno(String dni, String nombre, String curso) {
        super(dni, nombre);
        this.curso = textoObligatorio(curso, "El curso");
    }

    public String getCurso() {
        return curso;
    }

    @Override
    public TipoCliente getTipo() {
        return TipoCliente.ALUMNO;
    }

    @Override
    public String toString() {
        return super.toString() + ", curso: " + curso;
    }
}
