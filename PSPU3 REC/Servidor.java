package examen;

import java.net.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor {
	private final static int MAX_BYTES = 1400;
	private final static String COD_TEXTO = "UTF-8";
	String CODIGO;
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Error, indica el puerto y un código.");
			System.exit(1);
		}else if (args.length < 2) {
			System.err.println("Error, indica el puerto y un código.");
			System.exit(1);
		}
		int n_puerto = Integer.parseInt(args[0]);
		String codigo = args[2].toString();

		try {
//lE PEDIMOS LOS DATOS Y EL CODIGO, LUEGO LE HACEMOS 4 BUCLES TRES DENTRO DE UNO MISMO Y DESPUES CADA UNO 
			//HACE LA VERIFICACION DEL USUARIO, DE LA CONTRASEÑA Y DE CODIGO Y CUANDO EL CODIGO SEA EL MISMO EMPIEZA OTRA VEZ EL GRANDE
			try (DatagramSocket ds_server = new DatagramSocket(n_puerto)) {
				Random r = new Random();
				//NumeroOculto n_oculto = new NumeroOculto(r.nextInt(101));
				
				//pillar el usuario y contraseña, verificarlos, y mandarle al usuario el codigo para que lo introduzca.

				while (codigo!="@respuesta#"+codigo+"@") {

					byte[] b_recividos = new byte[MAX_BYTES];
					DatagramPacket dp_recibido = new DatagramPacket(b_recividos, b_recividos.length);

					byte[] b = new byte[MAX_BYTES];
					DatagramPacket dp_enviado = new DatagramPacket(b, b.length);

					ds_server.receive(dp_recibido);

					InetAddress ia_ip_cliente = dp_recibido.getAddress();
					int npuerto_cliente = dp_recibido.getPort();

					String mensaje = new String(dp_recibido.getData(), 0, dp_recibido.getLength(), COD_TEXTO);

					String nombre;
					String n;
					String respuesta;

					Pattern patRespuesta = Pattern.compile("@(.+)#(.*)@");
					Matcher m = patRespuesta.matcher(mensaje);
					if(m.find()) {
						nombre = m.group(1);
						n = m.group(2);
						respuesta = m.group(3);
						
						if(respuesta.equals(codigo)) {
							System.out.println("Has introducido bien el código, te damos las felicidades desde el servidor!");
						
						}else {
							System.out.println("Ese codigo no es correcto, vuelve a intentarlo.");
							//se repite el bucle hasta encontrarle
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}