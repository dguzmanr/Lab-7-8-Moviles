/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import AccesoDatos.GlobalException;
import AccesoDatos.NoDataException;

/**
 *
 * @author david
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws GlobalException, NoDataException {
        // TODO code application logic here
        Control c = new Control();
        System.out.print(c.listarAlumnos());
    }
    
}
