package test;

import org.junit.Test;


/**
 * @author T-lim1
 * 死锁示例代码
 * 当发送死锁时
 * 	Thread-0:run
	Thread-1:run
	Thread-1:lock b
	Thread-0:lock a
	且jvm进程无法终止
 */
public class DeadLockTest {
	
	Object a=new Object();
	Object b=new Object();
	
	@Test
	public void test1(){
		Thread A=new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":run");
				ThreadALock();
			}
		});
		Thread B=new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":run");
				ThreadBLock();
			}
		});
		A.start();
		B.start();
		try {
			A.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void ThreadALock(){
		synchronized(a){
			System.out.println(Thread.currentThread().getName()+":lock a");
			synchronized(b){
				System.out.println(Thread.currentThread().getName()+":lock b");
			}
		}
	}
	public void ThreadBLock(){
		synchronized(b){
			System.out.println(Thread.currentThread().getName()+":lock b");
			synchronized(a){
				System.out.println(Thread.currentThread().getName()+":lock a");
			}
		}
	}
}
