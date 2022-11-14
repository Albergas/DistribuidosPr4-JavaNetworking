package net42;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	
	private static File HOMEDIR = new File("C:\\Users\\aleja\\OneDrive\\Escritorio\\Pr4Ej2"); 
	
	public static void main(String args[]) {

		try(ServerSocket ss = new ServerSocket(8080)){
			
			ExecutorService pool = Executors.newCachedThreadPool();
			
			while(true) {
				try {
					Socket s = ss.accept();
					
					HiloHTTP h = new HiloHTTP(HOMEDIR, s);
					pool.execute(h);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
