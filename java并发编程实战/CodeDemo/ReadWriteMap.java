package test;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author limeng
 * 
 * 通过ReadWriteLock读写锁包装Map
 */
public class ReadWriteMap {
	
	private final ReadWriteLock lock=new ReentrantReadWriteLock();
	private final Lock readLock=lock.readLock();
	private final Lock writeLock=lock.writeLock();
	
	private Map map;

	public ReadWriteMap(Map map) {
		this.map = map;
	}

	public Object get() {
		readLock.lock();
		try{
			return map;
		}finally{
			readLock.unlock();
		}
	}

	public void put(Object key,Object value) {
		writeLock.lock();
		try{
			this.map.put(key, value);
		}finally{
			writeLock.unlock();
		}
	}
	
	
}
