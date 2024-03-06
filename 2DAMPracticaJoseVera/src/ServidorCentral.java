/**
 * Clase que representa el servidor central de un sistema de gestión de entradas para eventos.
 * Gestiona la disponibilidad de entradas, realiza actualizaciones y proporciona información a los clientes.
 * 
 * @author Jose
 */
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ServidorCentral {

    private JFrame frame;

    private static int entradasTipo1 = 100;
    private static int entradasTipo2 = 60;
    private static int entradasTipo3 = 30;
    private static int contadorSuma = 0;
    
    private static int numIntentos = 0;

    private JLabel contadorTipo1;
    private JLabel contadorTipo2;
    private JLabel contadorTipo3;
    private JLabel contadorSuma_Label;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;

    
    /**
     * Método main del servidor central que ejecuta la aplicación
     * 
     * @exception Exception -> Se lanza en caso de error durante la ejecución.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServidorCentral window = new ServidorCentral();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ServidorCentral servidor = new ServidorCentral();
        servidor.iniciarServidor();
    }

    public ServidorCentral() {
        initialize();
    }

    /**
     * Metodo que contiene todos lo visual, al ejecutar todo lo que hay dentro de este metodo se imprime, botones, textfields...etc
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Servidor central");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setBounds(178, 27, 114, 13);
        frame.getContentPane().add(lblNewLabel);

        contadorTipo1 = new JLabel(Integer.toString(entradasTipo1));
        contadorTipo1.setForeground(Color.WHITE);
        contadorTipo1.setBounds(158, 63, 45, 13);
        frame.getContentPane().add(contadorTipo1);

        contadorTipo2 = new JLabel(Integer.toString(entradasTipo2));
        contadorTipo2.setForeground(Color.WHITE);
        contadorTipo2.setBounds(158, 104, 45, 13);
        frame.getContentPane().add(contadorTipo2);

        contadorTipo3 = new JLabel(Integer.toString(entradasTipo3));
        contadorTipo3.setForeground(Color.WHITE);
        contadorTipo3.setBounds(158, 146, 45, 13);
        frame.getContentPane().add(contadorTipo3);

        contadorSuma_Label = new JLabel(Integer.toString(contadorSuma));
        contadorSuma_Label.setForeground(Color.WHITE);
        contadorSuma_Label.setBounds(28, 212, 45, 13);
        frame.getContentPane().add(contadorSuma_Label);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		actualizarLabels();
        		numIntentos=0;
        	}
        });
        btnActualizar.setBounds(307, 208, 119, 21);
        frame.getContentPane().add(btnActualizar);
        
        
		
		lblNewLabel_2 = new JLabel("ENTRADA GENERAL:");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(28, 63, 145, 13);
		frame.getContentPane().add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("ENTRADAPREMIUM:");
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(28, 104, 120, 13);
		frame.getContentPane().add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("ENTRADAS VIP: ");
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setBounds(28, 146, 120, 13);
		frame.getContentPane().add(lblNewLabel_4);
		
		lblNewLabel_1 = new JLabel("");
        lblNewLabel_1 .setIcon(new ImageIcon(".//Fotos//cliente.png"));
        lblNewLabel_1 .setBounds(0, 0, 436, 263);
		frame.getContentPane().add(lblNewLabel_1);
        
    }

    /**
     * Método que atualiza el contador de entradas Disponibles desde la  visya del server
     */
    private synchronized void actualizarLabels() {
        contadorTipo1.setText(Integer.toString(entradasTipo1));
        contadorTipo2.setText(Integer.toString(entradasTipo2));
        contadorTipo3.setText(Integer.toString(entradasTipo3));
        contadorSuma_Label.setText(Integer.toString(contadorSuma));
    }

    
    /**
     * Inicia el servidor central y espera conexiones de clientes.
     * 
     * @exception IOException Se lanza si ocurre un error de entrada/salida durante la ejecución del servidor.
     */
    private void iniciarServidor() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Servidor Central iniciado. Esperando conexiones...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clienteSocket.getInetAddress());

                Thread clienteThread = new Thread(() -> manejarConexion(clienteSocket));
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que nos resta las entradas cuando compramos
     * 
     * @param tipo --> tipo de entrada que se va a actualizar al comprar una(General, premium o VIP)
     * @param cantidad --> Cantidad que se resta a la hora de comprar
     */
    private synchronized void actualizarEntradas(int tipo, int cantidad) {
        switch (tipo) {
            case 1:
                entradasTipo1 -= cantidad;
                break;
            case 2:
                entradasTipo2 -= cantidad;
                break;
            case 3:
                entradasTipo3 -= cantidad;
                break;
        }
    }

    /**
     * Método para actualizar la facturación
     * 
     * @param cantidad --> total de dinero facturado
     */
    private synchronized void actualizarFacturacion(int cantidad) {
        contadorSuma += cantidad;
    }

    /**
     * Metodo que maneja la conexion, los clientes que se unen y demas
     * 
     * @param clienteSocket -> es el cliente que se conecta al servidor
     * @exception SocketException -> esta "excepcion" salta cuando un cliente se desconecta, para ello ponemos un syso de que se ha desconectado el cliente en vez de mostrar el error y asi es mas bonito
     * @exception -> IOException Se lanza si ocurre un error de entrada/salida durante la conexión.
     */
    private void manejarConexion(Socket clienteSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clienteSocket.getOutputStream()));

            String mensajeCliente;
            while ((mensajeCliente = reader.readLine()) != null) {
                System.out.println("Mensaje recibido del cliente: " + mensajeCliente);

                if (mensajeCliente.startsWith("COMPRAR_ENTRADA")) {
                    String[] partes = mensajeCliente.split(",");
                    int tipo = Integer.parseInt(partes[1]);
                    int cantidad = Integer.parseInt(partes[2]);

                    System.out.println("Tipo de entrada: " + tipo + ", Cantidad: " + cantidad);
                    
                    if (verificarDisponibilidadEntradas(tipo, cantidad) && numIntentos<=3) {
                        System.out.println("Entradas disponibles. Realizando actualizaciones...");
                        
                        actualizarEntradas(tipo, cantidad);
                        actualizarFacturacion(cantidad * obtenerPrecioEntrada(tipo));

                        writer.write("COMPRA_EXITOSA");
                        writer.newLine();
                        writer.flush();

                        actualizarLabels();

                        System.out.println("Actualización completa. Entradas restantes - Tipo " + tipo + ": " + obtenerEntradasRestantes(tipo));
                    } else if(numIntentos>3){
                    	 JOptionPane.showMessageDialog(null, "No se pueden comprar más de tres entradas.", "Error", JOptionPane.ERROR_MESSAGE);
                    }else {
                        writer.write("ERROR: Entradas insuficientes");
                        writer.newLine();
                        writer.flush();
                        System.out.println("Error: Entradas insuficientes");
                    }
                    numIntentos++;
                } else if (mensajeCliente.equals("CONSULTAR_ENTRADAS")) {
                    enviarInfoEntradas(writer);
                }
            }
        } catch (SocketException e) {
            System.out.println("Cliente desconectado");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo para obtener las entradas que quedan
     * 
     * @param tipo -> Tipo de entrada sobre la que se va a consultar
     * @return 0 -> si no queda ninguna sale 0
     */
    private int obtenerEntradasRestantes(int tipo) {
        switch (tipo) {
            case 1:
                return entradasTipo1;
            case 2:
                return entradasTipo2;
            case 3:
                return entradasTipo3;
            default:
                return 0;
        }
    }
    
    /**
     * Metodo casi igual que el anterior solo que este verifica si qquedan
     * @param tipo
     * @param cantidad
     * @return si no quedan entradas salta el false
     */
    private boolean verificarDisponibilidadEntradas(int tipo, int cantidad) {
        switch (tipo) {
            case 1:
                return entradasTipo1 >= cantidad;
            case 2:
                return entradasTipo2 >= cantidad;
            case 3:
                return entradasTipo3 >= cantidad;
            default:
                return false;
        }
    }

    /**
     * Saber el precio de cada entrada
     * 
     * @param tipo --> tipo de entrada y su precio
     * @return 0
     */
    private int obtenerPrecioEntrada(int tipo) {
        switch (tipo) {
            case 1:
                return 30;
            case 2:
                return 50;
            case 3:
                return 100;
            default:
                return 0;
        }
    }

    /**
     * 
     * @param writer, mostrar en la interfaz del cliente cuantas entradas disponibles quedan mediante el BufferedWriter
     */
    private void enviarInfoEntradas(BufferedWriter writer) {
        try {
            writer.write("Entradas tipo 1 disponibles: " + entradasTipo1);
            writer.newLine();
            writer.write("Entradas tipo 2 disponibles: " + entradasTipo2);
            writer.newLine();
            writer.write("Entradas tipo 3 disponibles: " + entradasTipo3);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
