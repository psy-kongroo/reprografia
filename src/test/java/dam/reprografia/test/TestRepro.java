package dam.reprografia.test;

import dam.reprografia.controlador.GesRepro;
import dam.reprografia.dao.DAOCola;
import dam.reprografia.dao.DAOHistorial;
import dam.reprografia.modelo.Alumno;
import dam.reprografia.modelo.Docu;
import dam.reprografia.modelo.DocuHis;
import dam.reprografia.modelo.OrdenHistorial;
import dam.reprografia.modelo.Profesor;
import dam.reprografia.modelo.Trabajo;

import java.time.LocalDateTime;
import java.util.List;

/** Comprobaciones sencillas de prioridad, orden y agrupación del historial. */
public class TestRepro {

    public static void main(String[] args) {
        DAOCola cola = new DAOCola();
        DAOHistorial historial = new DAOHistorial();
        GesRepro gestion = new GesRepro(cola, historial);

        Trabajo alumno = gestion.recibirTrabajo(
                new Alumno("100", "Ana", "1 DAM"),
                new Docu(2, "Trabajo de clase")
        );
        Trabajo primerProfesor = gestion.recibirTrabajo(
                new Profesor("300", "Luis", "Informática"),
                new Docu(1, "Examen")
        );
        Trabajo segundoProfesor = gestion.recibirTrabajo(
                new Profesor("200", "Eva", "Matemáticas"),
                new Docu(1, "Apuntes")
        );

        List<Trabajo> pendientes = gestion.listarPendientes();
        comprobar(pendientes.size() == 3, "Deben listarse los tres trabajos.");
        comprobar(pendientes.get(0).getId() == primerProfesor.getId(),
                "Los profesores deben tener prioridad.");
        comprobar(pendientes.get(1).getId() == segundoProfesor.getId(),
                "Los trabajos de profesores deben respetar el orden de llegada.");
        comprobar(pendientes.get(2).getId() == alumno.getId(),
                "Los trabajos de alumnos se imprimen después de los de profesores.");

        comprobar(cola.desencolarSiguiente().orElseThrow().getId() == primerProfesor.getId(),
                "La cola debe extraer primero el trabajo del primer profesor.");
        comprobar(cola.desencolarSiguiente().orElseThrow().getId() == segundoProfesor.getId(),
                "La cola debe mantener el orden FIFO de profesores.");

        Alumno ana = new Alumno("100", "Ana", "1 DAM");
        historial.registrarImpresion(ana, new DocuHis(2, "Trabajo 1", LocalDateTime.now()));
        historial.registrarImpresion(ana, new DocuHis(3, "Trabajo 2", LocalDateTime.now()));
        historial.registrarImpresion(
                new Profesor("50", "Zoe", "Informática"),
                new DocuHis(1, "Apuntes", LocalDateTime.now())
        );

        comprobar(gestion.buscarHistorial("100").orElseThrow().getDocumentos().size() == 2,
                "El historial debe agrupar todos los documentos por DNI.");
        comprobar(gestion.listarHistorial(OrdenHistorial.DNI).get(0)
                        .getCliente().getDni().equals("100"),
                "El historial debe poder ordenarse por DNI.");
        comprobar(gestion.listarHistorial(OrdenHistorial.NOMBRE).get(0)
                        .getCliente().getNombre().equals("Ana"),
                "El historial debe poder ordenarse por nombre.");

        System.out.println("Pruebas correctas.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
