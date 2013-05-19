

import BaseDatos.Actualizar;
import BaseDatos.Consultar;
import BaseDatos.Eliminar;
import BaseDatos.Insertar;
import Fecha.ObtFecha;
import Fecha.ObtHora;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;


public class ClaseServidor extends UnicastRemoteObject implements Operaciones{
DefaultTableModel modeloTabla;
DefaultTableModel modeloTablaHistorial;
    Object datos[]=new Object[3];
    Object datos2[]=new Object[3];
    int contador;
    GUIServidor gui;
    int puerto;
    String ip;
    Registry registro;


    public ClaseServidor() throws UnknownHostException, RemoteException
    {
        
        gui=new GUIServidor();
        gui.setVisible(true);
        modeloTabla=(DefaultTableModel) gui.tabla_Ejecutando.getModel();
        modeloTablaHistorial=(DefaultTableModel) gui.tabla_Historial.getModel();
        ip = (InetAddress.getLocalHost().toString());
        
puerto = 8080;
        System.out.println("conexion establecida diccion ip"+ip+"y puerto"+puerto);
        registro = LocateRegistry.createRegistry(puerto);
        registro.rebind("rmi", this);
        gui.btn_limpiar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
             limpiarDatos();
            }
        });
        
        actualizarTablaHistorial();
    }

    public static void main(String[] args) throws UnknownHostException, RemoteException {
        ClaseServidor servidor = new ClaseServidor();
       

    }

    public void Insertar(String nombre,String genero,String editorial,String autor) throws RemoteException {
            new Insertar("insert into libros (nombre,genero,aditorial,autor) values ('"+nombre+"','"+genero+"','"+editorial+"','"+autor+"')");
    
    }

    public void Eliminar(int id) throws RemoteException {
            new Eliminar("delete from libros where id="+id);
        
    }

    public void  Actulizar(int id,String nombre,String genero,String editorial,String autor) throws RemoteException {
            new Actualizar("update libros set nombre='"+nombre+"',genero='"+genero+"',aditorial='"+editorial+"',autor='"+autor+"' where id="+id);
       
    }

    public boolean  logeo(String usuario, String contrasenia) throws RemoteException {
             boolean respuesta=false;

            String contraseniaobt="";
            ResultSet resultados = new Consultar().Consultar("select contrasenia from alumnos where nombre='" + usuario + "'");
        try {
            while (resultados.next()) {
                
                contraseniaobt = resultados.getString(1);
                if (contrasenia.equals(contraseniaobt)) {
                   respuesta =true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return respuesta;
    }



    public void recibirMensaje(String cadena) throws RemoteException {


           datos[0]=cadena;
            datos[1]=new ObtFecha().ObtFecha();
            datos[2]=new ObtHora().ObtHora();
            modeloTabla.addRow(datos);
            System.out.println("Se ateindio a : "+cadena);
            new Insertar("insert into hosts (nombre,fecha,horaEntrada,horaSalida) values ('"+cadena+"','"+new ObtFecha().ObtFecha()+"','"+new ObtHora().ObtHora()+"','00:00:00')");
            contador++;
            
               actualizarTablaHistorial();
            
    }

    public String []consulta() throws RemoteException {
        //throw new UnsupportedOperationException("Not supported yet.");
       String datos = "";

        int numeroFilas =0;
        int i=0;

        ResultSet resultadosFilas =new Consultar().Consultar("select count(*) from libros");
        try {
            while (resultadosFilas.next()) {
                numeroFilas = resultadosFilas.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        String Arrreglocadenas[]=new String[numeroFilas*5];

        ResultSet resultados =new Consultar().Consultar("select *from libros");

        try {
            while (resultados.next()) {

              Arrreglocadenas[i] = resultados.getInt(1)+"";
              i++;
              Arrreglocadenas[i] =resultados.getString(2);
              i++;
              Arrreglocadenas[i]=resultados.getString(3);
              i++;
              Arrreglocadenas[i]=resultados.getString(4);
              i++;
              Arrreglocadenas[i]=resultados.getString(5);
              i++;
            }
        } catch (SQLException ex) {
            System.out.println("Error en la consulta"+ex);
        }

        
        
        return Arrreglocadenas;
    }

    
    public void CerrarSesion(String cadena) throws RemoteException {
     new Insertar("insert into hosts (horaEntrada) values (time(now())) where nombre='"+cadena+"'");
    }
   
   public void actualizarTablaHistorial(){
        int cantidadRegistros = modeloTablaHistorial.getRowCount();
        for (int i = 0; i < cantidadRegistros; i++) {
            modeloTablaHistorial.removeRow(0);
        }
            
            ResultSet resultadosHistirial = new Consultar().Consultar("select *from hosts");
        try {
            while(resultadosHistirial.next()){
                     datos2[0]=resultadosHistirial.getString(1);
                     datos2[1]=resultadosHistirial.getDate(2);
                     datos2[2]=resultadosHistirial.getTime(3);
                     modeloTablaHistorial.addRow(datos2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
   } 
    
    
    public  void limpiarDatos(){
                int cantidadRegistros = modeloTabla.getRowCount();
        for (int i = 0; i < cantidadRegistros; i++) {
            modeloTabla.removeRow(0);
        }
    }

}
