package dam.reprografia.dao;

import dam.reprografia.modelo.TipoCliente;
import dam.reprografia.modelo.Trabajo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Mantiene dos colas FIFO para aplicar la prioridad indicada en el enunciado. */
public class DAOCola {
    private final Deque<Trabajo> trabajosProfesores = new ArrayDeque<>();
    private final Deque<Trabajo> trabajosAlumnos = new ArrayDeque<>();

    public void encolar(Trabajo trabajo) {
        Objects.requireNonNull(trabajo, "El trabajo no puede ser nulo");
        if (trabajo.getCliente().getTipo() == TipoCliente.PROFESOR) {
            trabajosProfesores.addLast(trabajo);
        } else {
            trabajosAlumnos.addLast(trabajo);
        }
    }

    public Optional<Trabajo> desencolarSiguiente() {
        // Se vacía primero la cola de profesores; los alumnos esperan si queda alguna.
        if (!trabajosProfesores.isEmpty()) {
            return Optional.of(trabajosProfesores.removeFirst());
        }
        if (!trabajosAlumnos.isEmpty()) {
            return Optional.of(trabajosAlumnos.removeFirst());
        }
        return Optional.empty();
    }

    public void reponerAlPrincipio(Trabajo trabajo) {
        Objects.requireNonNull(trabajo, "El trabajo no puede ser nulo");
        // Si se interrumpe una impresión, el trabajo conserva su turno.
        if (trabajo.getCliente().getTipo() == TipoCliente.PROFESOR) {
            trabajosProfesores.addFirst(trabajo);
        } else {
            trabajosAlumnos.addFirst(trabajo);
        }
    }

    public List<Trabajo> listarPendientes() {
        List<Trabajo> pendientes = new ArrayList<>(trabajosProfesores);
        pendientes.addAll(trabajosAlumnos);
        return List.copyOf(pendientes);
    }

    public int cantidadPendiente() {
        return trabajosProfesores.size() + trabajosAlumnos.size();
    }
}
