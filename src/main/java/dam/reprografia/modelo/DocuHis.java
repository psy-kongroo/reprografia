package dam.reprografia.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

/** Documento impreso con la fecha en que se completó. */
public class DocuHis extends Docu {
    private final LocalDateTime fechaImpresion;

    public DocuHis(int numPaginas, String referencia, LocalDateTime fechaImpresion) {
        super(numPaginas, referencia);
        this.fechaImpresion = Objects.requireNonNull(fechaImpresion, "La fecha no puede ser nula");
    }

    public LocalDateTime getFechaImpresion() {
        return fechaImpresion;
    }
}
