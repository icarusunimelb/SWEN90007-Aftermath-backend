package utils;

import datamapper.DataMapperFactory;
import domain.DomainObject;
import exceptions.NoSuchMapperTypeException;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private static ThreadLocal current = new ThreadLocal();

    private List<DomainObject> newObjects = new ArrayList<DomainObject>();
    private List<DomainObject> dirtyObjects = new ArrayList<DomainObject>();
    private List<DomainObject> deletedObjects = new ArrayList<DomainObject>();

    public static void newCurrent(){
        setCurrent(new UnitOfWork());
    }
    public static void setCurrent(UnitOfWork unitOfWork){
        current.set(unitOfWork);
    }
    public static UnitOfWork getCurrent(){
        return (UnitOfWork) current.get();
    }

    public void registerNew(DomainObject obj) {
        if(checkMap(obj)) {
            newObjects.add(obj);
        }
    }

    public void registerDirty(DomainObject obj) {
        if (checkMap(obj)){
            dirtyObjects.add(obj);
        }
    }

    public void registerDeleted(DomainObject obj){
        if (newObjects.remove(obj)) return;
        dirtyObjects.remove(obj);
        if (!deletedObjects.contains(obj)){
            deletedObjects.add(obj);
        }
    }

    public boolean checkMap(DomainObject obj) {
        return !newObjects.contains(obj) && !dirtyObjects.contains(obj) &&
                !deletedObjects.contains(obj);
    }

    public void commit() {
        try {
            for (DomainObject object : newObjects) {
                DataMapperFactory.getMapper(object.getClass().getName()).insert(object);
            }
            for (DomainObject object : dirtyObjects){
                DataMapperFactory.getMapper(object.getClass().getName()).update(object);
            }
            for (DomainObject object : deletedObjects){
                DataMapperFactory.getMapper(object.getClass().getName()).delete(object);
            }
        }catch (NoSuchMapperTypeException e){
            System.out.println(e.getMessage());
        }
    }
}