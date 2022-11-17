package net43;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net42.HiloHTTP;

public class Ejercicio3 {

	public static boolean esPrimo(int n) {
		
		if(n==1 || (n%2==0 && n!=2)) {
			return false;
		}
		
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(cores);
		
		int operaciones = n/2-1;
		int sec = operaciones / cores;
		int usados = 0;
		
		List<Future<Boolean>> listaHPs = new ArrayList<Future<Boolean>>();
		
		while(usados < cores) {
			listaHPs.add(pool.submit(new HiloPrimo(sec*usados+2, sec*usados+2 + sec, n)));
			usados++;
		}
		
		for(Future<Boolean> f : listaHPs) {
			try {
				if (!f.get())
					return false;
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
		
	}
}
