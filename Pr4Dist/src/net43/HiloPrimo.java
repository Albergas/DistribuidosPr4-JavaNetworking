package net43;

import java.util.concurrent.Callable;

public class HiloPrimo implements Callable<Boolean> {

	private int n1, n2, n;

	public HiloPrimo(int n1, int n2, int n) {

		this.n1 = n1;
		this.n2 = n2;
		this.n = n;
	}
	
	public Boolean call(){

		for(int i=n1; i < n2; i++){
			if(n%i==0)
				return false;
		}
		return true;
	}	
}