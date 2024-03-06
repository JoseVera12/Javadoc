/**
 * Clase que representa el cliente de un sistema de gestión de entradas para eventos.
 * Permite al usuario realizar consultas y compras de entradas, también visualizar informes de compra
 * 
 * @author Jose
 */

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.Color;


public class Cliente {

	private JFrame frame;
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private int tipoEntradaSeleccionada;
    private int totalCompra;

    /**
     * Método principal del cliente que ejecuta la aplicación.
     * 
     * @exception excepcion que se manda a la consola en caso de error durante la ejecución.
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Cliente window = new Cliente();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Cliente() {
		initialize();
		conectarAlServidor();
	}

	/**
	 * Metodo que inicializa la interfaz de la aplicación, contiene todo lo visual y todos los accionadores que se ejecutan a la hora de presionar un boton, etc..
	 * 
	 * @exception Todas las excepciones saltan por consola cuando da un error
	 * @exception JRException salta cuando se da un error con el Jasper. Puede ocurrir, por ejemplo, cuando hay problemas con el diseño del informe, los datos proporcionados no son válidos o hay errores durante la generación del informe.
	 * @exception IOException se utiliza para indicar que ha ocurrido un error relacionado con operaciones de entrada/salida, como lectura o escritura de datos en archivos, conexiones de red, entrada/salida estándar, entre otros
	 * 
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("ENTRADAS BETIS");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(158, 26, 192, 13);
		frame.getContentPane().add(lblNewLabel);

		JButton EntradasDisponibles = new JButton("Consulta de entradas");
		EntradasDisponibles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					writer.write("CONSULTAR_ENTRADAS");
					writer.newLine();
					writer.flush();

					String respuesta = reader.readLine();
					mostrarMensajeEmergente("Entradas Disponibles", respuesta);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		EntradasDisponibles.setBounds(10, 205, 185, 21);
		frame.getContentPane().add(EntradasDisponibles);

		JButton btnEntradaNormal = new JButton("Entrada General");
		btnEntradaNormal.setEnabled(false);
		btnEntradaNormal.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        realizarCompra(1, 1, "General", 30);
		        Map<String, Object> mapeo = new HashMap<>();
		        mapeo.put("Cantidad", Integer.toString(1));
		        mapeo.put("Tipo", "General");
		        mapeo.put("Total", Integer.toString(totalCompra)); 

		        try {
		            JasperReport jasperReport = JasperCompileManager.compileReport("jasper/Blank_A4.jrxml");

		            JasperPrint informePrint = JasperFillManager.fillReport(jasperReport, mapeo, new JREmptyDataSource());
		            JasperViewer.viewReport(informePrint);
		        } catch (JRException e1) {
		            e1.printStackTrace();
		        }

		    }
		});

		btnEntradaNormal.setBounds(126, 49, 169, 21);
		frame.getContentPane().add(btnEntradaNormal);

		JButton btnEntradaPremium = new JButton("Entrada Premium");
		btnEntradaPremium.setEnabled(false);
		btnEntradaPremium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarCompra(1, 1, "Premium", 60);
				Map<String, Object> mapeo = new HashMap<>();
				mapeo.put("Cantidad", Integer.toString(1));
		        mapeo.put("Tipo", "General");
		        mapeo.put("Total", Integer.toString(totalCompra));

				try {
					JasperReport jasperReport = JasperCompileManager.compileReport("jasper/Blank_A4.jrxml");


					JasperPrint informePrint = JasperFillManager.fillReport(jasperReport, mapeo, new JREmptyDataSource());
					JasperViewer.viewReport(informePrint);
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnEntradaPremium.setBounds(126, 88, 169, 21);
		frame.getContentPane().add(btnEntradaPremium);

		JButton btnEntradaVIP = new JButton("Entrada VIP");
		btnEntradaVIP.setEnabled(false);
		btnEntradaVIP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarCompra(1, 1, "Premium", 100);
				Map<String, Object> mapeo = new HashMap<>();
				mapeo.put("Cantidad", Integer.toString(1));
		        mapeo.put("Tipo", "VIP");
		        mapeo.put("Total", Integer.toString(totalCompra));

				try {
					JasperReport jasperReport = JasperCompileManager.compileReport("jasper/Blank_A4.jrxml");


					JasperPrint informePrint = JasperFillManager.fillReport(jasperReport, mapeo, new JREmptyDataSource());
					JasperViewer.viewReport(informePrint);
				} catch (JRException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnEntradaVIP.setBounds(126, 130, 169, 21);
		frame.getContentPane().add(btnEntradaVIP);

		JButton btnReservar = new JButton("Empezar a reservar");
		btnReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Temporizador(btnEntradaNormal, btnEntradaPremium, btnEntradaVIP, btnReservar);

			}
		});
		btnReservar.setBounds(263, 205, 163, 21);
		frame.getContentPane().add(btnReservar);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(".//Fotos//cliente.png"));
		lblNewLabel_1.setBounds(0, 0, 436, 263);
		frame.getContentPane().add(lblNewLabel_1);
	}

	/**
	 * 
	 * Es un metodo para informar que es accionado, es decir cuando se toca en algún botón o algo se imprime este método, este método es llamado desde otros.
	 * 
	 * @param titulo -> Muestra un mensaje escrito desde el codigo para dar una idea de lo que se esta informando. ej: "Entradas disponibles: " + el verdadero mensaje informativo
	 * @param mensaje -> Muestra un mensaje que es una variable, un ejemplo: quedan 9 entradas, ese 9 es el mensaje que se va aumentando o restando.
	 */
	private void mostrarMensajeEmergente(String titulo, String mensaje) {
		JOptionPane.showMessageDialog(frame, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
     * Realiza la conexión del cliente con el servidor.
     * 
     * @exception -> IOException Se lanza si ocurre un error de entrada/salida durante la conexión.
     */
	private void conectarAlServidor() {
		try {
			socket = new Socket("localhost", 1234);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Cliente sale del servidor");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para tener un limite de tiempo, puedes cambiarlo desde esta linea (10 * 1000)
	 * 
	 * @param jbutton -> Boton compra general
	 * @param jButton2 -> boton compra premium
	 * @param jButton3 -> boton compra VIP
	 * @param jButtonReserva -> boton empezar la reserva, una vez accionado el tiempo de compra
	 */
	private static void Temporizador(JButton jbutton, JButton jButton2, JButton jButton3, JButton jButtonReserva) {

		jbutton.setEnabled(true);
		jButton2.setEnabled(true);
		jButton3.setEnabled(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				jButtonReserva.setEnabled(false);
				jbutton.setEnabled(false);
				jButton2.setEnabled(false);
				jButton3.setEnabled(false);

			}
		}, 10 * 1000);
	}

	/**
     * Realiza la compra de una entrada y genera un informe visual.
     * 
     * @param tipo -> El tipo de entrada a comprar.
     * @param cantidad -> La cantidad de entradas a comprar.
     * @param tipoEntrada -> El tipo específico de entrada (General, Premium, VIP).
     * @param precio -> El precio unitario de la entrada.
     * @exception -> IOException Se lanza si ocurre un error de entrada/salida durante la conexión.
     */
	private void realizarCompra(int tipo, int cantidad, String tipoEntrada, int precio) {
	    tipoEntradaSeleccionada = tipo;
	    totalCompra = cantidad * precio;

	    try {
	        writer.write("COMPRAR_ENTRADA," + tipo + "," + cantidad);
	        writer.newLine();
	        writer.flush();

	        String respuesta = reader.readLine();
	        mostrarMensajeEmergente("Resultado de la compra", respuesta);

	        Map<String, Object> mapeo = new HashMap<>();
	        mapeo.put("Cantidad", Integer.toString(cantidad));
	        mapeo.put("Tipo", tipoEntrada);
	        mapeo.put("Total", Integer.toString(totalCompra));

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

}
