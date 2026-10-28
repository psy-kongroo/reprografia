package dam.reprografia.modelo;

import java.util.Objects;
import java.util.Locale;

/** Datos comunes a alumnos y profesores. */
public abstract class Cliente {
    private final String dni;
    private final String nombre;

    protected Cliente(String dni, String nombre) {
        this.dni = textoObligatorio(dni, "El DNI");
        this.nombre = textoObligatorio(nombre, "El nombre");
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract TipoCliente getTipo();

    protected static String textoObligatorio(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " no puede ser nulo");
        String limpio = valor.trim();
        // Evita guardar datos obligatorios vacíos o con espacios sobrantes.
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío");
        }
        return limpio;
    }

    @Override
    public String toString() {
        return nombre + " (DNI: " + dni + ", "
                + getTipo().name().toLowerCase(Locale.ROOT) + ")";
    }
}
