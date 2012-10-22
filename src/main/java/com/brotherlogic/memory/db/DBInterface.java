package com.brotherlogic.memory.db;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import com.brotherlogic.memory.core.Memory;

public abstract class DBInterface
{
   public abstract void storeMemory(Memory mem) throws IOException;

   public abstract Memory retrieveMemory(long timestamp, String className) throws IOException;

   public abstract void clear() throws IOException;

   private String getProperty(Method meth)
   {
      String mName = meth.getName();
      return mName.substring("get".length(), "get".length() + 1).toLowerCase()
            + mName.substring("get".length() + 1);
   }

   protected void setProperty(Memory obj, String propName, Object value) throws IOException
   {
      try
      {
         Method setMethod = obj.getClass().getMethod(
               "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1), new Class[]
               { value.getClass() });
         setMethod.invoke(obj, new Object[]
         { value });
      }
      catch (NoSuchMethodException e)
      {
         throw new IOException(e);
      }
      catch (InvocationTargetException e)
      {
         throw new IOException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IOException(e);
      }
   }

   protected Object getObject(String property, Object obj) throws IOException
   {
      try
      {
         Method meth = obj.getClass()
               .getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1),
                     new Class[0]);
         Object retValue = meth.invoke(obj, new Object[0]);
         return retValue;
      }
      catch (NoSuchMethodException e)
      {
         throw new IOException(e);
      }
      catch (InvocationTargetException e)
      {
         throw new IOException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IOException(e);
      }
   }

   /**
    * Maps from the property name (accessible via get methods) to the
    * corresponding responsible class
    * 
    * @param mem
    *           The memory to derive
    * @return A {@link Map} from property name to the responsible class
    */
   public Map<String, Class> deriveProperties(Memory mem)
   {
      Map<String, Class> strs = new TreeMap<String, Class>();

      // Add all the interface methods
      for (Class inter : mem.getClass().getInterfaces())
         for (Method meth : inter.getMethods())
            if (meth.getName().startsWith("get"))
               strs.put(getProperty(meth), inter);

      // Add all the class methods (excluding object methods)
      for (Method meth : mem.getClass().getMethods())
         if (meth.getName().startsWith("get") && !strs.containsKey(getProperty(meth)))
            if (meth.getDeclaringClass() != Object.class)
               strs.put(getProperty(meth), meth.getDeclaringClass());

      return strs;
   }
}
