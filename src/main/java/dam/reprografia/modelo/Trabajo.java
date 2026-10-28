package dam.reprografia.modelo;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/** Asocia un cliente y un documento con un identificador único en la ejecución. */
public class Trabajo {
    private static final AtomicLong SECUENCIA = new AtomicLong(0);

    private final long id;
    private final Cliente cliente;
    private final Docu documento;

    public Trabajo(Cliente cliente, Docu documento) {
        // AtomicLong genera identificadores seguros incluso con accesos concurrentes.
        this.id = SECUENCIA.incrementAndGet();
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        this.documento = Objects.requireNonNull(documento, "El documento no puede ser nulo");
    }

    public long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Docu getDocumento() {
        return documento;
    }
}
