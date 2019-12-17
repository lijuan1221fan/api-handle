package com.visionvera.api.handler.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author EricShen
 */
public class RefUtil {

  /**
   * 方法名
   *
   * @return 调用的方法的方法名
   */
  public static String getMethodName() {
    return Thread.currentThread().getStackTrace()[2].getMethodName();
  }

  /**
   * 参数对象转换成map
   *
   * @param paramEntity 参数对象
   * @return map | null
   */
  public static Map<String, Object> getProperty(ParamBaseEntity paramEntity) {
    if (paramEntity == null) {
      return null;
    }
    Map<String, Object> resultMap = null;
    try {
      //获得属性
      Field[] fields = paramEntity.getClass().getDeclaredFields();
      Class clazz = paramEntity.getClass();
      resultMap = new HashMap<String, Object>(fields.length);
      for (Field field : fields) {
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
        Method getMethod = pd.getReadMethod();
        if (getMethod != null && getMethod.invoke(paramEntity) != null) {
          String name = field.getName();
          Class<?> type = field.getType();
          Object value = getMethod.invoke(paramEntity);
          resultMap.put(name, value);
        }
      }
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
      e.printStackTrace();
    }
    return resultMap;
  }

  /**
   * 执行静态方法
   *
   * @param objclass 类型
   * @param methodName 方法名称
   * @param value 参数
   * @return Object
   */
  public static <T> Object executeStatic(Class<?> objclass, String methodName, T value) {
    try {
      if (objclass == null || value == null) {
        return null;
      }
      Method func = objclass.getMethod(methodName, value.getClass());
      if (func != null) {
        return func.invoke(null, value);
      } else {
        return null;
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T execute(Class<?> clazz, Object obj, String methodName) {
    try {
      Method method = clazz.getMethod(methodName);
      return method == null ? null : (T) method.invoke(obj);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T executePrivate(Class<?> clazz, Object obj, String methodName) {
    try {
      Method methodPrivate = clazz.getDeclaredMethod(methodName);
      methodPrivate.isAccessible();
      //调private方法
      methodPrivate.setAccessible(true);
      return (T) methodPrivate.invoke(obj);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }

  }


  public static <T> T execute(Class<?> objclass, Object obj, String methodName, T value) {
    try {
      if (value == null) {
        return null;
      }
      Method func = objclass.getMethod(methodName, value.getClass());
      if (func != null) {
        return (T) func.invoke(obj, value);
      } else {
        return null;
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T executeDouble(Class<?> objclass, Object obj, String methodName,
      double value) {
    try {
      Method func = objclass.getMethod(methodName, double.class);
      return (T) func.invoke(obj, value);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T executeFloat(Class<?> objclass, Object obj, String methodName, float value) {
    try {
      Method func = objclass.getMethod(methodName, float.class);
      return (T) func.invoke(obj, value);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }


  public static <T> T executeLong(Class<?> objclass, Object obj, String methodName, long value) {
    try {
      Method func = objclass.getMethod(methodName, long.class);
      return (T) func.invoke(obj, value);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }
}
