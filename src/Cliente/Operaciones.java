//package Ejemplos;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author neogeek
 */
public interface Operaciones extends Remote{

    public void Insertar(String nombre,String genero,String editorial,String autor) throws RemoteException;

    public void Eliminar(int id) throws RemoteException;

    public void Actulizar(int id,String nombre,String genero,String editorial,String autor) throws RemoteException;

    public boolean  logeo(String usuario,String contrasenia) throws RemoteException;

    public  String []consulta()throws RemoteException;

    public void recibirMensaje(String cadena) throws RemoteException;

    public void CerrarSesion(String cadena) throws RemoteException;

}
