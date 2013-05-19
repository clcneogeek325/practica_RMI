/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package Ejemplos;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author neogeek
 */
public class Cliente {

    static Operaciones objetoRemoto;
    Registry registro;
    String dirccionDelServidor="192.168.43.8";
    int puertoServidor=8080;

    void conectarseAlservidor() throws RemoteException, NotBoundException{
        registro = LocateRegistry.getRegistry(dirccionDelServidor,puertoServidor);
        objetoRemoto = (Operaciones) (registro.lookup("rmi"));

    }

    public static void main(String[] args) throws RemoteException , NotBoundException,UnknownHostException{
        Cliente cliente = new Cliente();
        cliente.conectarseAlservidor();
        objetoRemoto.recibirMensaje("Eminem");
        if (objetoRemoto.logeo("admin", "gnome")) {
            System.out.println("la cont correcta");
        }else{
            System.out.println("no");
        }
    }

}
