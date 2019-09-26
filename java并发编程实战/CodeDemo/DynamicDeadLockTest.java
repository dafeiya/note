package test;

import org.junit.Test;

/**
 * @author T-lim1
 * 动态锁顺序死锁,发生死锁的对象通过函数传参传入
 * A,B账户互相转账场景
 * 
 * 可以通过控制锁的获取顺序来避免该问题
 */
public class DynamicDeadLockTest {

	private final Object tieLock=new Object(); //用于控制账户hash一致使的同步
	
	private final Account accA=new Account("AccountA",100.00);
	private final Account accB=new Account("AccountB",100.00);
	
	public void transAccountUnsafe(Account from, Account to,double amount) throws Exception{
		System.out.println(Thread.currentThread().getName()+":转账前账户情况：\n	from:"+from+"\n	to:"+to);
		synchronized(from){
			synchronized(to){
				if(from.amount-amount<0){throw new Exception("账户余额不足");}
				from.setAmount(from.getAmount()-amount);
				to.setAmount(to.getAmount()+amount);
				System.out.println(Thread.currentThread().getName()+":转账后账户情况：\n	from:"+from+"\n	to:"+to);
			}
		}
	}
	
	//通过控制锁的获取顺序来避免死锁
	public void transAccountSafe(Account from, Account to,double amount) throws Exception{
		System.out.println(Thread.currentThread().getName()+":转账前账户情况：\n	from:"+from+"\n	to:"+to);
		int fromHash=System.identityHashCode(from);
		int toHash=System.identityHashCode(to);
		if(fromHash<toHash){
			synchronized(from){
				synchronized(to){
					if(from.amount-amount<0){throw new Exception("账户余额不足");}
					from.setAmount(from.getAmount()-amount);
					to.setAmount(to.getAmount()+amount);
					System.out.println(Thread.currentThread().getName()+":转账后账户情况：\n	from:"+from+"\n	to:"+to);
				}
			}
		}else if(fromHash>toHash){
			synchronized(to){
				synchronized(from){
					if(from.amount-amount<0){throw new Exception("账户余额不足");}
					from.setAmount(from.getAmount()-amount);
					to.setAmount(to.getAmount()+amount);
					System.out.println(Thread.currentThread().getName()+":转账后账户情况：\n	from:"+from+"\n	to:"+to);
				}
			}
		}
		else{
			synchronized(tieLock){//当出现hash相同时，通过一个锁对象进行同步
				synchronized(from){
					synchronized(to){
						if(from.amount-amount<0){throw new Exception("账户余额不足");}
						from.setAmount(from.getAmount()-amount);
						to.setAmount(to.getAmount()+amount);
						System.out.println(Thread.currentThread().getName()+":转账后账户情况：\n	from:"+from+"\n	to:"+to);
					}
				}
			}
		}
		
	}
	
	@Test
	public void test1(){
		Thread transA2B=new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":run");
				try {
					for(int i=0;i<50;i++){
//						transAccountUnsafe(accA,accB,1);//可能发生死锁
						transAccountSafe(accA,accB,1);//无死锁
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Thread transB2A=new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":run");
				try {
					for(int i=0;i<50;i++){
//						transAccountUnsafe(accB,accA,2);	//可能发生死锁
						transAccountSafe(accB,accA,2);//无死锁
					}
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		});
		transA2B.setName("t_transA2B");
		transB2A.setName("t_transB2A");
		transA2B.start();
		transB2A.start();
		try {
			transA2B.join();
			transB2A.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test2(){
		System.out.println(System.identityHashCode(accA));
		System.out.println(System.identityHashCode(accB));
		System.out.println(accA.hashCode());
		System.out.println(accB.hashCode());

	}
	class Account {
		private String accountName;
		private double amount;
		public Account(String accountName, double amount) {
			super();
			this.accountName = accountName;
			this.amount = amount;
		}
		public String getAccountName() {
			return accountName;
		}
		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		@Override
		public String toString() {
			return "Account [accountName=" + accountName + ", amount=" + amount + "]";
		}
		
	}
}
