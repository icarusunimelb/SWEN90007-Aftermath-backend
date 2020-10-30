package utils;

import exceptions.CanNotAcquireLockException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LockManager {
	
	private static LockManager instance;
	public enum LOCKTYPE {READ, WRITE}
	
	//Key: lockable
	//Value: owner
	private ConcurrentMap<String, String> readLockMap;
	private ConcurrentMap<String, String> writeLockMap;

	public static synchronized LockManager getInstance() {
		if(instance == null) {
			instance = new LockManager();
		}
		return instance;
	}
	
	private LockManager() {
		readLockMap = new ConcurrentHashMap<String, String>();
		writeLockMap = new ConcurrentHashMap<String, String>();
	}

	public synchronized void acquireLock(String lockable, String owner, LOCKTYPE lockType) throws CanNotAcquireLockException{
		if ((lockType == LOCKTYPE.READ && writeLockMap.containsKey(lockable)) ||
				(lockType == LOCKTYPE.WRITE && (writeLockMap.containsKey(lockable) || readLockMap.containsKey(lockable)))) {

		}

		if (lockType == LOCKTYPE.READ && !writeLockMap.containsKey(lockable)) {
			readLockMap.put(lockable, owner);
		} else if (lockType == LOCKTYPE.WRITE && !writeLockMap.containsKey(lockable) && !readLockMap.containsKey(lockable)) {
			writeLockMap.put(lockable, owner);
		} else {
			throw new CanNotAcquireLockException("Can not acquire lock!");
		}
	}
	
	public synchronized void releaseLock(String lockable, String owner, LOCKTYPE lockType) {
		if (lockType == LOCKTYPE.READ) {
			readLockMap.remove(lockable);
		} else {
			writeLockMap.remove(lockable);
		}
		notify();
	}

	public synchronized void releaseAll(String owner) {
		readLockMap.entrySet().removeIf(e -> e.getValue().equals(owner) );
		writeLockMap.entrySet().removeIf(e -> e.getValue().equals(owner));
	}
}
