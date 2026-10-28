package dam.reprografia.controlador;

import dam.reprografia.dao.DAOCola;
import dam.reprografia.dao.DAOHistorial;
import dam.reprografia.modelo.Cliente;
import dam.reprografia.modelo.Docu;
import dam.reprografia.modelo.DocuHis;
import dam.reprografia.modelo.OrdenHistorial;
import dam.reprografia.modelo.Trabajo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Coordina la cola de impresión y el registro de trabajos completados. */
public class GesRepro {
    // 100 ms por página equivalen a la velocidad simulada de 10 páginas por segundo.
    private static final long MILISEGUNDOS_POR_PAGINA = 100;

    private final DAOCola daoCola;
    private final DAOHistorial daoHistorial;

    public GesRepro(DAOCola daoCola, DAOHistorial daoHistorial) {
        this.daoCola = Objects.requireNonNull(daoCola);
        this.daoHistorial = Objects.requireNonNull(daoHistorial);
    }

    public Trabajo recibirTrabajo(Cliente cliente, Docu documento) {
        Trabajo trabajo = new Trabajo(cliente, documento);
        recibirTrabajo(trabajo);
        return trabajo;
    }

    public void recibirTrabajo(Trabajo trabajo) {
        daoCola.encolar(trabajo);
    }

    public Optional<Trabajo> fotocopiarSiguiente() throws InterruptedException {
        Optional<Trabajo> siguiente = daoCola.desencolarSiguiente();
        if (siguiente.isEmpty()) {
            return Optional.empty();
        }

        Trabajo trabajo = siguiente.get();
        try {
            // Simula el tiempo de impresión en función del número de páginas.
            Thread.sleep(trabajo.getDocumento().getNumPaginas() * MILISEGUNDOS_POR_PAGINA);
        } catch (InterruptedException excepcion) {
            daoCola.reponerAlPrincipio(trabajo);
            throw excepcion;
        }
        Docu documento = trabajo.getDocumento();
        daoHistorial.registrarImpresion(
                trabajo.getCliente(),
                new DocuHis(documento.getNumPaginas(), documento.getReferencia(), LocalDateTime.now())
        );
        return siguiente;
    }

    public List<Trabajo> listarPendientes() {
        return daoCola.listarPendientes();
    }

    public Optional<DAOHistorial.HistorialCliente> buscarHistorial(String dni) {
        return daoHistorial.buscarPorDni(dni);
    }

    public List<DAOHistorial.HistorialCliente> listarHistorial(OrdenHistorial orden) {
        return daoHistorial.listarTodos(orden);
    }

    public int cantidadPendiente() {
        return daoCola.cantidadPendiente();
    }
}
