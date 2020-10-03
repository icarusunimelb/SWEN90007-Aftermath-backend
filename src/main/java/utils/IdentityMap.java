package utils;

import java.util.HashMap;
import java.util.Map;

public class IdentityMap<E> {
    private Map<String, E> map = new HashMap<String, E>();
    private static Map<Class, IdentityMap> singletons = new HashMap<Class, IdentityMap>();

    public static <E> IdentityMap<E> getInstance(E e){
        IdentityMap<E> result = singletons.get(e.getClass());
        if (result == null){
            result = new IdentityMap<E>();
            singletons.put(e.getClass(), result);
        }
        return result;
    }

    public void put(String id, E obj) {
        map.put(id, obj);
    }

    public E get(String id) {
        return map.get(id);
    }
}
