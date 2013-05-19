/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package Ejemplos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author neogeek
 */
public class TablaActualizado {
  public static GUICliente infCliente;
 static Operaciones objetoRemoto;
    Registry registro;
    String dirccionDelServidor="192.168.43.8";
    int puertoServidor=8080;
    void conectarseAlservidor() throws RemoteException, NotBoundException{
        registro = LocateRegistry.getRegistry(dirccionDelServidor,puertoServidor);
        objetoRemoto = (Operaciones) (registro.lookup("rmi"));
     
            
     
    }

      public static void main(String[] args) throws RemoteException, NotBoundException {
  
                 actualizarTabla();
               
    }
      
      public static void actualizarTabla() throws RemoteException, NotBoundException{
            DefaultTableModel modeloTabla;
     DefaultTableModel modeloTabla2;
    Object datos[]=new Object[5];
        infCliente = new GUICliente();
        infCliente.setVisible(true);
         modeloTabla = (DefaultTableModel) infCliente.tabla_actualiar.getModel();
         modeloTabla2 = (DefaultTableModel) infCliente.tabla_Eliminar.getModel();

          TablaActualizado cliente = new TablaActualizado();
        cliente.conectarseAlservidor();

        String librosObtenidos[]=objetoRemoto.consulta();
        String filas="";
        
        int i=0;



        while(i < librosObtenidos.length){

            datos[0]=librosObtenidos[i];
            i++;
            datos[1]=librosObtenidos[i];
            i++;
            datos[2]=librosObtenidos[i];
            i++;
           datos[3]=librosObtenidos[i];
            i++;
            datos[4]=librosObtenidos[i];
            i++;

            modeloTabla.addRow(datos);
            modeloTabla2.addRow(datos);
            
        }

      }

}
