package utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LockManagerEX {
	
	private static LockManagerEX instance;
	
	//Key: lockable
	//Value: owner
	private ConcurrentMap<String, String> lockMap;

	public static synchronized LockManagerEX getInstance() {
		if(instance == null) {
			instance = new LockManagerEX();
		}
		return instance;
	}
	
	private LockManagerEX() {
		lockMap = new ConcurrentHashMap<String, String>();
	}
	
	public boolean acquireLock(String lockable, String owner) {
		if(!lockMap.containsKey(lockable)) {
			//no lock on lockable, grant lock
			lockMap.put(lockable, owner);
			return true;
		}

		return false;
	}
	
	public void releaseLock(String lockable, String owner) {
		lockMap.remove(lockable);
		notify();
	}
}
