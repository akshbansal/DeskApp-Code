package com.im.login;

import java.util.Timer;
import java.util.TimerTask;

import com.im.utils.Constants;

public class SayHello {

	public static void main(String[] args) {
		// And From your main() method or any other method
		 Timer timer = new Timer();
		 timer.schedule(new SayHelloTask(), 0, 1000);
	}
}

class SayHelloTask extends TimerTask {
	int counter = 0;
    public void run() {
    	counter++;
    	if(counter == 5) {
    		counter = 0;
    		if(Constants.showConsole) System.out.println("after five second, Hello World!");
    	} else {
    		System.out.print(counter);
    	}
    }
 }

 