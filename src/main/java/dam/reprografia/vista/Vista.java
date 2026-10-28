package dam.reprografia.vista;

import dam.reprografia.dao.DAOHistorial.HistorialCliente;
import dam.reprografia.modelo.Alumno;
import dam.reprografia.modelo.Cliente;
import dam.reprografia.modelo.Docu;
import dam.reprografia.modelo.DocuHis;
import dam.reprografia.modelo.OpcionHistorial;
import dam.reprografia.modelo.OpcionMenu;
import dam.reprografia.modelo.Profesor;
import dam.reprografia.modelo.TipoCliente;
import dam.reprografia.modelo.Trabajo;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/** Se encarga de pedir datos y mostrar resultados por consola. */
public class Vista {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Scanner scanner;

    public Vista() {
        this(new Scanner(System.in));
    }

    public Vista(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== Reprografía del instituto ===");
        System.out.println("0. Recibir trabajo (simular correo)");
        System.out.println("1. Fotocopiar el siguiente trabajo");
        System.out.println("2. Consultar historial de fotocopias");
        System.out.println("3. Listar trabajos pendientes e historial");
        System.out.println("4. Salir");
        return leerEntero("Elige una opción: ");
    }

    public int mostrarMenuHistorial() {
        System.out.println("\n--- Consultar historial ---");
        System.out.println("1. Individual por DNI");
        System.out.println("2. Todos los usuarios y trabajos por DNI");
        System.out.println("3. Todos los usuarios y trabajos por nombre");
        System.out.println("4. Volver al menú principal");
        return leerEntero("Elige una opción: ");
    }

    public Trabajo solicitarTrabajo() {
        System.out.println("\n--- Nuevo trabajo recibido ---");
        String dni = leerTexto("DNI: ");
        String nombre = leerTexto("Nombre: ");
        TipoCliente tipo = solicitarTipoCliente();

        Cliente cliente;
        if (tipo == TipoCliente.ALUMNO) {
            cliente = new Alumno(dni, nombre, leerTexto("Curso: "));
        } else {
            cliente = new Profesor(dni, nombre, leerTexto("Departamento: "));
        }

        int paginas = leerEnteroPositivo("Número de páginas: ");
        String referencia = leerTexto("Descripción breve del trabajo: ");
        return new Trabajo(cliente, new Docu(paginas, referencia));
    }

    public String solicitarDni() {
        return leerTexto("DNI que desea consultar: ");
    }

    public void mostrarTrabajoRecibido(Trabajo trabajo) {
        System.out.println("Trabajo #" + trabajo.getId() + " añadido a la cola para "
                + trabajo.getCliente().getNombre() + ".");
    }

    public void mostrarTrabajoImpreso(Trabajo trabajo) {
        System.out.println("Trabajo #" + trabajo.getId() + " impreso: "
                + trabajo.getDocumento().getNumPaginas() + " páginas, cliente "
                + trabajo.getCliente().getNombre() + ".");
    }

    public void mostrarColaVacia() {
        System.out.println("No hay trabajos pendientes.");
    }

    public void mostrarHistorialPorDni(HistorialCliente historial) {
        System.out.println("\nHistorial de " + historial.getCliente() + ":");
        mostrarDocumentos(historial.getDocumentos());
    }

    public void mostrarHistorialCompleto(List<HistorialCliente> historiales) {
        if (historiales.isEmpty()) {
            System.out.println("Todavía no hay fotocopias realizadas.");
            return;
        }

        for (HistorialCliente historial : historiales) {
            mostrarHistorialPorDni(historial);
        }
    }

    public void mostrarListaCompleta(List<Trabajo> pendientes, List<HistorialCliente> historiales) {
        System.out.println("\n--- Trabajos pendientes (" + pendientes.size() + ") ---");
        if (pendientes.isEmpty()) {
            System.out.println("No hay trabajos pendientes.");
        } else {
            for (Trabajo trabajo : pendientes) {
                System.out.println("#" + trabajo.getId() + " | "
                        + trabajo.getCliente().getTipo() + " | "
                        + trabajo.getCliente().getNombre() + " (DNI "
                        + trabajo.getCliente().getDni() + ") | "
                        + trabajo.getDocumento().getNumPaginas() + " páginas | "
                        + trabajo.getDocumento().getReferencia());
            }
        }

        System.out.println("\n--- Trabajos realizados ---");
        mostrarHistorialCompleto(historiales);
    }

    public void mostrarDniSinHistorial(String dni) {
        System.out.println("No hay trabajos realizados para el DNI " + dni + ".");
    }

    public void mostrarErrorEntrada() {
        System.out.println("Entrada no válida. Inténtalo de nuevo.");
    }

    public void mostrarInterrupcion() {
        System.out.println("La impresión se ha interrumpido.");
    }

    public void mostrarDespedida() {
        System.out.println("Hasta luego.");
    }

    private TipoCliente solicitarTipoCliente() {
        while (true) {
            System.out.println("Tipo de cliente: 1. Alumno  2. Profesor");
            int opcion = leerEntero("Elige una opción: ");
            if (opcion == 1) {
                return TipoCliente.ALUMNO;
            }
            if (opcion == 2) {
                return TipoCliente.PROFESOR;
            }
            mostrarErrorEntrada();
        }
    }

    private void mostrarDocumentos(List<DocuHis> documentos) {
        for (DocuHis documento : documentos) {
            System.out.println("- " + documento.getNumPaginas() + " páginas | "
                    + documento.getReferencia() + " | "
                    + documento.getFechaImpresion().format(FORMATO_FECHA));
        }
    }

    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) {
                return valor;
            }
            mostrarErrorEntrada();
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException excepcion) {
                // Mantiene la pregunta activa hasta recibir un número válido.
                mostrarErrorEntrada();
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            mostrarErrorEntrada();
        }
    }
}
