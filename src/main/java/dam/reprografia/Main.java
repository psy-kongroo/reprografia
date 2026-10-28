package dam.reprografia;

import dam.reprografia.controlador.CtrlRepro;
import dam.reprografia.controlador.GesRepro;
import dam.reprografia.dao.DAOCola;
import dam.reprografia.dao.DAOHistorial;
import dam.reprografia.vista.Vista;

public class Main {

    public static void main(String[] args) {
        // Conecta las capas de datos, gestión y presentación.
        GesRepro gestion = new GesRepro(new DAOCola(), new DAOHistorial());
        CtrlRepro controlador = new CtrlRepro(gestion, new Vista());
        controlador.iniciar();
    }
}
