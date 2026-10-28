package dam.reprografia.modelo;

/** Opciones disponibles en el submenú de consulta del historial. */
public enum OpcionHistorial {
    POR_DNI(1),
    TODOS_POR_DNI(2),
    TODOS_POR_NOMBRE(3),
    VOLVER(4);

    private final int numero;

    OpcionHistorial(int numero) {
        this.numero = numero;
    }

    /** Traduce la entrada numérica a una opción del submenú. */
    public static OpcionHistorial desdeNumero(int numero) {
        for (OpcionHistorial opcion : values()) {
            if (opcion.numero == numero) {
                return opcion;
            }
        }
        return null;
    }
}
