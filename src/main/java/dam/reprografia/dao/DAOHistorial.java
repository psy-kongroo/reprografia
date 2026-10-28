package dam.reprografia.dao;

import dam.reprografia.modelo.Cliente;
import dam.reprografia.modelo.DocuHis;
import dam.reprografia.modelo.OrdenHistorial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Agrupa las impresiones completadas por DNI y permite consultarlas ordenadas. */
public class DAOHistorial {
    // El DNI identifica el historial de cada cliente.
    private final Map<String, HistorialCliente> historialPorDni = new HashMap<>();

    public void registrarImpresion(Cliente cliente, DocuHis documento) {
        Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        Objects.requireNonNull(documento, "El documento no puede ser nulo");

        // Crea el historial solo para el primer trabajo impreso de ese cliente.
        HistorialCliente historial = historialPorDni.computeIfAbsent(
                cliente.getDni(),
                dni -> new HistorialCliente(cliente)
        );
        historial.agregar(documento);
    }

    public Optional<HistorialCliente> buscarPorDni(String dni) {
        return Optional.ofNullable(historialPorDni.get(dni));
    }

    public List<HistorialCliente> listarTodos(OrdenHistorial orden) {
        Objects.requireNonNull(orden, "El orden no puede ser nulo");
        Comparator<HistorialCliente> comparador;
        if (orden == OrdenHistorial.NOMBRE) {
            comparador = Comparator.comparing(
                    (HistorialCliente entrada) -> entrada.getCliente().getNombre(),
                    String.CASE_INSENSITIVE_ORDER
            ).thenComparing(entrada -> entrada.getCliente().getDni());
        } else {
            comparador = Comparator.comparing(entrada -> entrada.getCliente().getDni());
        }

        // Se ordena una copia para no alterar el orden interno del mapa.
        List<HistorialCliente> resultado = new ArrayList<>(historialPorDni.values());
        resultado.sort(comparador);
        return List.copyOf(resultado);
    }

    /** Cliente y documentos impresos asociados a su DNI. */
    public static final class HistorialCliente {
        private final Cliente cliente;
        private final List<DocuHis> documentos = new ArrayList<>();

        private HistorialCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        private void agregar(DocuHis documento) {
            documentos.add(documento);
        }

        public Cliente getCliente() {
            return cliente;
        }

        public List<DocuHis> getDocumentos() {
            return List.copyOf(documentos);
        }
    }
}
