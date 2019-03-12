package com.zhua.game.base;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseService {

	/**线程池*/
	private static final ExecutorService exe = Executors.newScheduledThreadPool(20);
	
	/**
	 * 生成业务id
	 */
	public String generatID(){
		return UUID.randomUUID().toString();
	}
	
	public String generateCode15(){
		Random r = new Random();
		return String.valueOf(System.nanoTime()) + r.nextInt(100) ;
	}
	
	public void registAsyncTask(Runnable command){
		exe.execute(command);
	}
	
	private void sleep(int time){
		try {
			Thread.sleep(time * 1000);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
