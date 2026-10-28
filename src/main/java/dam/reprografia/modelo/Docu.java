package dam.reprografia.modelo;

import java.util.Objects;

/** Datos simulados de un documento, sin almacenar su contenido real. */
public class Docu {
    private final int numPaginas;
    private final String referencia;

    public Docu(int numPaginas, String referencia) {
        // Un trabajo sin páginas o referencia no se puede poner en la cola.
        if (numPaginas <= 0) {
            throw new IllegalArgumentException("El número de páginas debe ser mayor que cero");
        }
        this.numPaginas = numPaginas;
        String referenciaLimpia = Objects.requireNonNull(
                referencia, "La referencia no puede ser nula"
        ).trim();
        if (referenciaLimpia.isEmpty()) {
            throw new IllegalArgumentException("La referencia no puede estar vacía");
        }
        this.referencia = referenciaLimpia;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public String getReferencia() {
        return referencia;
    }
}
