package utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LockManager {
	
	private static LockManager instance;
	
	//Key: lockable
	//Value: owner
	private ConcurrentMap<String, String> lockMap;

	public static synchronized LockManager getInstance() {
		if(instance == null) {
			instance = new LockManager();
		}
		return instance;
	}
	
	private LockManager() {
		lockMap = new ConcurrentHashMap<String, String>();
	}

	public synchronized void acquireLock(String lockable, String owner) {
		while(lockMap.containsKey(lockable)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		lockMap.put(lockable, owner);
	}
	
	public void releaseLock(String lockable, String owner) {
		lockMap.remove(lockable);
		notify();
	}
}
