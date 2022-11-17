package net41;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class HiloDescargador extends Thread{

	private URL url;
	private long byteInicial;
	private long byteFinal;
	private CountDownLatch cdl;
	
	public HiloDescargador(URL url, long bi, long bf, CountDownLatch cdl) {
		this.url = url;
		this.byteInicial = bi;
		this.byteFinal = bf;
		this.cdl = cdl;
	}
	
	public void run() {
		
		try( RandomAccessFile raf = new RandomAccessFile("arch.txt", "rw")) {
			
			raf.seek(byteInicial);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("Range", "bytes="+byteInicial+"-"+byteFinal);
			DataInputStream i = new DataInputStream(con.getInputStream());
			
			byte content[] = new byte[((int) byteFinal - (int) byteInicial) + 1];
			
			i.read(content, 0, ((int) byteFinal - (int) byteInicial) + 1);
			
			raf.write(content);
			
			cdl.countDown();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
