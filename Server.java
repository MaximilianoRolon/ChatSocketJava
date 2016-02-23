import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.Utilities;

public class Server extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private JButton boton;
	private int coordenadas;
	
	//constructor
	public Server(){
		super("MSN Pro v0.1  SERVER");
		userText =new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
		boton=new JButton();
		boton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if(event.getSource()==boton){
							sendMessage(":)");
						}
					}
				}
				);
		add(boton,BorderLayout.EAST);
		add(userText,BorderLayout.SOUTH);
		chatWindow=new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300,150);
		setVisible(true);
	}
	
	//configurar y correr el servidor
	public void startRunning(){
		try{
			server = new ServerSocket(6789,100);
				while(true){
					try{
						waitForConnection();
						setupStreams();
						whileChatting();
			
					}catch(EOFException eofException){
						showMessage("\nLa conexion se terminó");
					}finally{
						closeCrap();
					}
				}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//esperando a que se conecten
	private void waitForConnection()throws IOException{
		chatWindow.setEditable(false);
		showMessage("\nEsperando a que alguien se conecte");
		connection= server.accept();
		showMessage("\nSe han conectado desde :"+connection.getInetAddress().getHostName());
	}
	
	//configurar streams
	private void setupStreams()throws IOException{
		output= new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showMessage("\nLos streams estan establecidos");
	}
	
	//while chating
	private void whileChatting()throws IOException{
		String message ="Estas conectado!!";
		sendMessage(message);
		ableToType(true);
		do{
		try{
			
			message= (String) input.readObject();
			showMessage("\n"+message);
			
			
		}catch(ClassNotFoundException classNotFoundException){
			showMessage("\nNo se pudo recibir mensaje");
		}
		}while(!message.equals("Cliente: SALIR"));
	}
	
	//cerrar streams
	private void closeCrap(){
		showMessage("\nFinalizando conexion");
		ableToType(false);
		try{
			input.close();
			output.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//enviar mensajes
	private void sendMessage(String message){
		try{
			output.writeObject("Server: " + message);
			output.flush();
			showMessage("\nServer: " + message);
			
		}catch (IOException ioException){
			chatWindow.append("\nError al enviar mensaje");
		}
	}
	
	//mostrar y actualizar mensajes
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(text);
					}
				}
				);
	}
	
	//permitir escribir texto
		private void ableToType(final boolean tof){
			SwingUtilities.invokeLater(
					new Runnable(){
						public void run(){
							userText.setEditable(tof);
						}
					}
					);
		}		
}
