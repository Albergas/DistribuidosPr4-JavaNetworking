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

import net43.Ejercicio3;


public class Server {
	
	private static File HOMEDIR = new File("C:\\Users\\aleja\\Desktop\\Pr4Ej2"); 
	
	public static void main(String args[]) {

		System.out.println(Ejercicio3.esPrimo(2));
		System.out.println(Ejercicio3.esPrimo(259));
		System.out.println(Ejercicio3.esPrimo(4));
		System.out.println(Ejercicio3.esPrimo(3));
		System.out.println(Ejercicio3.esPrimo(9));
		
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