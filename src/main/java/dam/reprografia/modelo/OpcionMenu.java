package dam.reprografia.modelo;

/** Opciones del menú principal y el número que introduce el usuario. */
public enum OpcionMenu {
    RECIBIR_TRABAJO(0),
    FOTOCOPIAR(1),
    CONSULTAR_HISTORIAL(2),
    LISTAR_TODO(3),
    SALIR(4);

    private final int numero;

    OpcionMenu(int numero) {
        this.numero = numero;
    }

    /** Devuelve la opción asociada al número o null si no existe. */
    public static OpcionMenu desdeNumero(int numero) {
        for (OpcionMenu opcion : values()) {
            if (opcion.numero == numero) {
                return opcion;
            }
        }
        return null;
    }
}
