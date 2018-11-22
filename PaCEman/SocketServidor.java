import java.net.*;
import java.io.*;

public class SocketServidor
{    
    public static void main (String [] args) {
    
        new SocketServidor();
    }
    public SocketServidor(){
        try{
        	while(true) {
                ServerSocket socket = new ServerSocket (35557);
                
                // Se acepata una conexión con un cliente. Esta llamada se queda
                // bloqueada hasta que se arranque el cliente.
                System.out.println ("Esperando cliente");
                Socket cliente = socket.accept();
                System.out.println ("Conectado con cliente de " + cliente.getInetAddress());
                
                // Se hace que el cierre del socket sea "gracioso". Esta llamada sólo
                // es necesaria si cerramos el socket inmediatamente después de
                // enviar los datos (como en este caso).
                // setSoLinger() a true hace que el cierre del socket espere a que
                // el cliente lea los datos, hasta un máximo de 10 segundos de espera.
                // Si no ponemos esto, el socket se cierra inmediatamente y si el 
                // cliente no ha tenido tiempo de leerlos, los datos se pierden.
                cliente.setSoLinger (true, 10);

        
                // Se prepara el flujo de entrada de datos, es decir, la clase encargada
                // de leer datos del socket.
                DataInputStream bufferEntrada =
                   new DataInputStream (cliente.getInputStream());

                // Se crea un dato a leer y se le dice que se rellene con el flujo de
                // entrada de datos.
                DatoSocket aux = new DatoSocket("");
                aux.readObject (bufferEntrada);
                System.out.println ("Servidor java: Recibido " + aux.toString());
                
                // Se cierra el socket con el cliente.
                // La llamada anterior a setSoLinger() hará
                // que estos cierres esperen a que el cliente retire los datos.
                cliente.close();
                
                // Se cierra el socket encargado de aceptar clientes. Ya no
                // queremos más.
                socket.close();
        	}
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}




//
//import java.net.*;
//import java.io.*;
//
//
// // Clase principal que instancia un socket servidor, acepta una conexión
// // de un cliente y le envía un entero y una cadena de caracteres.
// 
//public class SocketServidor
//{    
//		
//	
//    public static void main (String [] args)
//    {
//        // Se instancia la clase principal para que haga todo lo que tiene que
//        // hacer el ejemplo
//        new SocketServidor();
//    }
//    
//     
//      // Constructor por defecto. Hace todo lo que hace el ejemplo.
//     
//    public SocketServidor()
//    {
//        try
//        {
//            // Se crea un socket servidor atendiendo a un determinado puerto.
//            // Por ejemplo, el 35557.
//            ServerSocket socket = new ServerSocket (8080);
//            
//            // Se acepata una conexión con un cliente. Esta llamada se queda
//            // bloqueada hasta que se arranque el cliente.
//            System.out.println ("Esperando cliente");
//            Socket cliente = socket.accept();
//            System.out.println ("Conectado con cliente de " + cliente.getInetAddress());
//            
//            // Se hace que el cierre del socket sea "gracioso". Esta llamada sólo
//            // es necesaria si cerramos el socket inmediatamente después de
//            // enviar los datos (como en este caso).
//            // setSoLinger() a true hace que el cierre del socket espere a que
//            // el cliente lea los datos, hasta un máximo de 10 segundos de espera.
//            // Si no ponemos esto, el socket se cierra inmediatamente y si el 
//            // cliente no ha tenido tiempo de leerlos, los datos se pierden.
//            cliente.setSoLinger (true, 10);
//
//            // Se prepara un flujo de salida de datos simples con el socket.
//            DataOutputStream buffer = new DataOutputStream (cliente.getOutputStream());
//            
//            // Se envia un entero y una cadena de caracteres.
//            buffer.writeInt (22);
//            System.out.println ("Enviado 22");
//            buffer.writeUTF ("Hola");
//            System.out.println ("Enviado Hola");
//            
//            // Se prepara un flujo de salida para objetos y un objeto para enviar*/
//            DatoSocket dato = new DatoSocket();
//            ObjectOutputStream bufferObjetos = 
//                new ObjectOutputStream (cliente.getOutputStream());
//            
//            // Se envía el objeto 
//            bufferObjetos.writeObject(dato);
//            System.out.println ("Enviado " + dato.toString());
//            
//            // Se cierra el socket con el cliente.
//            // La llamada anterior a setSoLinger() hará
//            // que estos cierres esperen a que el cliente retire los datos.
//            cliente.close();
//            
//            // Se cierra el socket encargado de aceptar clientes. Ya no
//            // queremos más.
//            socket.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//}

