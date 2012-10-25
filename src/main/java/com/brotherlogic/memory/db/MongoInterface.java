package com.brotherlogic.memory.db;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.ObjectId;

import com.brotherlogic.memory.core.Memory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

public class MongoInterface extends DBInterface
{
   private DB mongo;

   private final String DB_NAME = "memorybox";
   private final String REF_NAME = "ref_id";

   @Override
   public void clear() throws IOException
   {
      connect();
      mongo.dropDatabase();
   }

   private void connect() throws IOException
   {
      if (mongo == null)
      {
         Mongo m = new Mongo();
         mongo = m.getDB(DB_NAME);
      }
   }

   private void setProperties(Class cls, Memory obj, Map<String, Class> properties, ObjectId refId)
         throws IOException
   {
      String collection = cls.getName();

      BasicDBObject query = new BasicDBObject();
      query.put(REF_NAME, refId);
      DBObject retObj = mongo.getCollection(collection).findOne(query);

      // Match up these properties
      for (Entry<String, Class> propEntry : properties.entrySet())
         if (propEntry.getValue().equals(cls))
            setProperty(obj, propEntry.getKey(), retObj.get(propEntry.getKey()));
   }

   @Override
   public Memory retrieveMemory(long timestamp, String className) throws IOException
   {
      try
      {
         // Create the given object
         Class objType = Class.forName(className);
         Constructor objCons = objType.getConstructor(new Class[0]);
         Memory memory = (Memory) objCons.newInstance(new Object[0]);

         // Get the ID number
         DBObject query = new BasicDBObject();
         query.put("timestamp", timestamp);
         DBObject res = mongo.getCollection(Memory.class.getName()).findOne(query);

         ObjectId id = (ObjectId) res.get("_id");
         memory.setTimestamp(timestamp);
         Map<String, Class> properties = deriveProperties(memory);
         enrichMemory(memory, id, properties);
         return memory;
      }
      catch (ClassNotFoundException e)
      {
         throw new IOException(e);
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
      catch (InstantiationException e)
      {
         throw new IOException(e);
      }
   }

   public void enrichMemory(Memory obj, ObjectId id, Map<String, Class> properties)
         throws IOException
   {
      // Ignore the core memory properties!
      Set<Class> classes = new HashSet<Class>(properties.values());
      classes.remove(Memory.class);
      for (Class cls : classes)
         setProperties(cls, obj, properties, id);

   }

   @Override
   public void storeMemory(Memory mem) throws IOException
   {
      String className = mem.getClass().getCanonicalName();
      Map<String, Class> propertyMap = deriveProperties(mem);

      // Store the memory in the memory table
      ObjectId id = store(Memory.class, mem, propertyMap, null);

      // Work through the other objects - ignore the Memory core stuff
      Set<Class> allClasses = new HashSet<Class>(propertyMap.values());
      allClasses.remove(Memory.class);
      for (Class storeClass : allClasses)
         store(storeClass, mem, propertyMap, id);
   }

   private ObjectId store(Class storeType, Object toStore, Map<String, Class> potProperties,
         ObjectId refId) throws IOException
   {
      connect();

      String collection = storeType.getCanonicalName();
      BasicDBObject obj = new BasicDBObject();

      for (Entry<String, Class> prop : potProperties.entrySet())
         if (prop.getValue().equals(storeType))
            obj.put(prop.getKey(), getObject(prop.getKey(), toStore));

      // Add the reference id if this isn't the base object
      if (refId != null)
         obj.put("ref_id", refId);

      WriteResult res = mongo.getCollection(collection).insert(obj);
      return obj.getObjectId("_id");
   }
}
