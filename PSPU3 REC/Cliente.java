package examen;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Cliente {
	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";
	private  String USUARIO;
	private  String CONTRASEÑA;

	public static void main(String[] args) throws UnknownHostException {
		if (args.length < 3) {
			System.err.println("Error, indica el puerto y un código.");
			System.exit(1);
		}else{
			System.err.println("Error, indica el puerto, un código, un usuario y una contraseña");
		}

		String n_host = args[0];
		int n_puerto = Integer.parseInt(args[1]);
		String user = args[2].toString();
		String contraseña = args[3].toString();
		InetAddress ia_ip_servidor = InetAddress.getByName(n_host);

		
		//Le pedimos los datos y los guardamos, los metemos en variables y por sockets los vamos mandando al servidor
		
		try {
			DatagramSocket ds_cliente = new DatagramSocket();
			ds_cliente.connect(ia_ip_servidor, n_puerto);
			
			Random r = new Random();
			byte[] b = new byte[MAX_BYTES];
			DatagramPacket dp_enviado;
			boolean fin = false;
			while(!fin) {
				Scanner sc = new Scanner(System.in);
				
				System.out.println("Escribe el usuario");
				String v_user = sc.nextLine();
				if (v_user==user) {
					//mandar "@user#"+user+"@"
					System.out.println("Escribe la contraseña");
					String v_contraseña = sc.nextLine();
					if(v_contraseña==contraseña) {
						//mandar "@password#"+contraseña+"@"
						
						//Si están bien puestas la contraseña y el usuario te deja mandar el codigo
						String valor = sc.nextLine();
						String propuesta = "@" + user + "#" + valor + "@";
						b = propuesta.getBytes();
						
						dp_enviado = new DatagramPacket(b,b.length, ia_ip_servidor, n_puerto);
						
						byte[] b2 = new byte[MAX_BYTES];
						
						DatagramPacket dp_recibido = new DatagramPacket(b2, b2.length);
						
						ds_cliente.receive(dp_recibido);
						
						String mensaje = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);
						
						String texto;
						String n_string;
						String v_codigo;
						
						Pattern patRespuesta = Pattern.compile("@(.+)#(.*)@");
						Matcher m = patRespuesta.matcher(mensaje);
						
						if(m.find()) {
							texto = m.group(1);
							n_string = m.group(2);
							v_codigo = m.group(3);
							
							if(v_codigo.equals(valor)) {
								System.out.println(v_codigo + " -Acierto.");
							
							}else {
								System.out.println(v_codigo + " -Ese codigo no es correcto, vuelve a intentarlo.");
								//se repite el bucle que estaira en el codigo (no se por que antes a codigo le he puesto valor)
							}
						}
					}
									
					
				}else {
					System.out.println("Vuelve a conectarte e introduce bien los datos");
					//repetir en bucle
				}
				
			}
			

		} catch (SocketException ex) {
			System.out.println("Error de sockets");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Excepción de E/S");
			ex.printStackTrace();
		}
	}
}