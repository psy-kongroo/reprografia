package dam.reprografia.controlador;

import dam.reprografia.dao.DAOHistorial.HistorialCliente;
import dam.reprografia.modelo.OpcionHistorial;
import dam.reprografia.modelo.OpcionMenu;
import dam.reprografia.modelo.OrdenHistorial;
import dam.reprografia.modelo.Trabajo;
import dam.reprografia.vista.Vista;

import java.util.Objects;
import java.util.Optional;

/** Traduce las opciones de la vista en acciones de gestión. */
public class CtrlRepro {
    private final GesRepro gestion;
    private final Vista vista;

    public CtrlRepro(GesRepro gestion, Vista vista) {
        this.gestion = Objects.requireNonNull(gestion);
        this.vista = Objects.requireNonNull(vista);
    }

    public void iniciar() {
        boolean continuar = true;
        // El menú principal se repite hasta salir o interrumpir la impresión.
        while (continuar) {
            OpcionMenu opcion = OpcionMenu.desdeNumero(vista.mostrarMenuPrincipal());
            if (opcion == null) {
                vista.mostrarErrorEntrada();
                continue;
            }

            switch (opcion) {
                case RECIBIR_TRABAJO:
                    Trabajo trabajo = vista.solicitarTrabajo();
                    gestion.recibirTrabajo(trabajo);
                    vista.mostrarTrabajoRecibido(trabajo);
                    break;
                case FOTOCOPIAR:
                    continuar = fotocopiarSiguiente();
                    break;
                case CONSULTAR_HISTORIAL:
                    consultarHistorial();
                    break;
                case LISTAR_TODO:
                    vista.mostrarListaCompleta(
                            gestion.listarPendientes(),
                            gestion.listarHistorial(OrdenHistorial.DNI)
                    );
                    break;
                case SALIR:
                    continuar = false;
                    vista.mostrarDespedida();
                    break;
                default:
                    vista.mostrarErrorEntrada();
            }
        }
    }

    private boolean fotocopiarSiguiente() {
        try {
            Optional<Trabajo> trabajo = gestion.fotocopiarSiguiente();
            if (trabajo.isPresent()) {
                vista.mostrarTrabajoImpreso(trabajo.get());
            } else {
                vista.mostrarColaVacia();
            }
            return true;
        } catch (InterruptedException excepcion) {
            // Restaura la señal para que otros componentes también puedan detectarla.
            Thread.currentThread().interrupt();
            vista.mostrarInterrupcion();
            return false;
        }
    }

    private void consultarHistorial() {
        boolean volver = false;
        while (!volver) {
            OpcionHistorial opcion = OpcionHistorial.desdeNumero(vista.mostrarMenuHistorial());
            if (opcion == null) {
                vista.mostrarErrorEntrada();
                continue;
            }

            switch (opcion) {
                case POR_DNI:
                    String dni = vista.solicitarDni();
                    Optional<HistorialCliente> historial = gestion.buscarHistorial(dni);
                    if (historial.isPresent()) {
                        vista.mostrarHistorialPorDni(historial.get());
                    } else {
                        vista.mostrarDniSinHistorial(dni);
                    }
                    break;
                case TODOS_POR_DNI:
                    vista.mostrarHistorialCompleto(gestion.listarHistorial(OrdenHistorial.DNI));
                    break;
                case TODOS_POR_NOMBRE:
                    vista.mostrarHistorialCompleto(gestion.listarHistorial(OrdenHistorial.NOMBRE));
                    break;
                case VOLVER:
                    volver = true;
                    break;
                default:
                    vista.mostrarErrorEntrada();
            }
        }
    }
}
