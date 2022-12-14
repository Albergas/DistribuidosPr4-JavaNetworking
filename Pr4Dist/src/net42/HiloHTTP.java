package net42;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;

public class HiloHTTP extends Thread{

	private File HOMEDIR;
	private Socket s;
	
	public HiloHTTP(File HOMEDIR, Socket s) {
		this.HOMEDIR = HOMEDIR;
		this.s = s;
	}
	
	public void run() {
		try (DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream())){
			
			String query = dis.readLine();
			
			File file = buscaFichero(query);
			
			if(file.exists()) {
				String cType;
				if (file.getPath().endsWith("css"))
					cType = "text/css";
				else
					cType = URLConnection.guessContentTypeFromName(file.getPath());
				sendMIMEHeading(dos, 200, cType, file.length());
				if(query.startsWith("GET ")) {
					FileInputStream fis = new FileInputStream(file);
					byte buf[] = new byte[512];
					int leidos = fis.read(buf);
					while(leidos != -1) {
						dos.write(buf, 0, leidos);
						leidos = fis.read(buf);
					}
				}
				dos.flush();
				
			}
			else {
				String msgError = makeHTMLErrorText(404, "Olv?date pa, ese fichero no est?");
				long tam = msgError.length();
				sendMIMEHeading(dos, 404, "text/plain; charset=UTF-8", tam);
				dos.writeBytes(msgError);
				dos.flush();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private File buscaFichero(String m) {
		 String fileName="";
		 if (m.startsWith("GET ")){
			 // A partir de una cadena de mensaje (m) correcta (comienza por GET)
			 fileName = m.substring(4, m.indexOf(" ", 5));
			 if (fileName.equals("/"))
				 fileName += "index.html";
		 }
		 
		 if (m.startsWith("HEAD ")){
			 // A partir de una cadena de mensaje (m) correcta (comienza por HEAD)
			 fileName = m.substring(6, m.indexOf(" ", 7));
			 if (fileName.equals("/"))
				 fileName += "index.html"; 
		 }
		 return new File(HOMEDIR, fileName);
		 
		 
	}

	
	private void sendMIMEHeading(OutputStream os, int code, String cType, long fSize) {
		PrintStream dos = new PrintStream(os);
		dos.print("HTTP/1.1 " + code + " ");
		if (code == 200) {
			dos.print("OK\r\n");
			dos.print("Date: " + new Date() + "\r\n");
			dos.print("Server: Cutre http Server ver. -6.0\r\n");
			dos.print("Connection: close\r\n");
			dos.print("Content-length: " + fSize + "\r\n");
			dos.print("Content-type: " + cType + "\r\n");
			dos.print("\r\n");
		} 
		else if (code == 404) {
			dos.print("File Not Found\r\n");
			dos.print("Date: " + new Date() + "\r\n");
			dos.print("Server: Cutre http Server ver. -6.0\r\n");
			dos.print("Connection: close\r\n");
			dos.print("Content-length: " + fSize + "\r\n");
			dos.print("Content-type: " + "text/html" + "\r\n");
			dos.print("\r\n");
		} 
		else if (code == 501) {
			dos.print("Not Implemented\r\n");
			dos.print("Date: " + new Date() + "\r\n");
			dos.print("Server: Cutre http Server ver. -6.0\r\n");
			dos.print("Connection: close\r\n");
			dos.print("Content-length: " + fSize + "\r\n");
			dos.print("Content-type: " + "text/html" + "\r\n");
			dos.print("\r\n");
		}
		dos.flush();
	}
	
	private String makeHTMLErrorText(int code, String txt) {
		StringBuffer msg = new StringBuffer("<HTML>\r\n");
		msg.append(" <HEAD>\r\n");
		msg.append(" <TITLE>" + txt + "</TITLE>\r\n");
		msg.append(" </HEAD>\r\n");
		msg.append(" <BODY>\r\n");
		msg.append(" <H1>HTTP Error " + code + ": " + txt + "</H1>\r\n");
		msg.append(" </BODY>\r\n");
		msg.append("</HTML>\r\n");
		return msg.toString();
	}
	
	private boolean esPrimo(int n) {
		for (int i = 2; i <= n/2; i++) {
			if (n%i==0)
				return false;
		}
		return true;
	}
}