package net41;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ejercicio1 {

	public static void main(String args[]) {
		
		long ti = System.currentTimeMillis();
		
		URL u = null;
		try {
			u = new URL("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/google/6/panda-face_1f43c.png");
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("HEAD");
			long tam = con.getContentLengthLong();
			
			ExecutorService pool = Executors.newFixedThreadPool(3);
			
			CountDownLatch cdl = new CountDownLatch(3);
			
			long part = tam/3;
			
			System.out.println("Se va a descargar el archivo de la url:");
			System.out.println("  "+u.toString());
			
			System.out.println("Hilo 1, descargará de " + 0 + " a " + (part-1));
			HiloDescargador hd1 = new HiloDescargador(u, 0, part-1, cdl);
			
			System.out.println("Hilo 2, descargará de " + part + " a " + (2*part-1));
			HiloDescargador hd2 = new HiloDescargador(u, part, 2*part-1, cdl);
			
			System.out.println("Hilo 3, descargará de " + 2*part + " a " + (tam-1));
			HiloDescargador hd3 = new HiloDescargador(u, 2*part, tam-1, cdl);
			
			pool.execute(hd1);
			pool.execute(hd2);
			pool.execute(hd3);
			
			System.out.println("Descargando...");
			
			cdl.await();
			
			long t = System.currentTimeMillis() - ti;
			
			System.out.println("Descarga completada. Tiempo: " + t + " ms");
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
