package utils;

import datamapper.*;
import domain.DomainObject;
import exceptions.NoSuchMapperTypeException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class KeyGenerator {
    private static volatile KeyGenerator instance = null;

    public static KeyGenerator getSingletonInstance() {
        if (instance == null) {
            synchronized (KeyGenerator.class) {
                if (instance == null) {
                    instance = new KeyGenerator();
                }
            }
        }
        return instance;
    }

    public String getKey(DomainObject object){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        Random random = new Random();
        String key = object.getClass().getName()+simpleDateFormat.format(date)+random.nextInt(100000000);

        return key;
    }
}
