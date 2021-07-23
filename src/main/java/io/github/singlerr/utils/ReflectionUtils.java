package io.github.singlerr.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {
    public static<T> T instantiateObject(Class<T> cls) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<T> con = cls.getConstructor();
        return con.newInstance();
    }
}
