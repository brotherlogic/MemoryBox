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

/**
 * DB Interface down to mongo
 * 
 * @author simon
 * 
 */
public class MongoInterface extends DBInterface
{
   /** The current mode that the database is running in */
   private final DBFactory.Mode currMode;

   /** The underlying database */
   private DB mongo;

   @Override
   public DownloadQueue getDownloadQueue()
   {
      return null;
   }

   /** The main database */
   private static final String DB_NAME = "memorybox";

   /** The test database */
   private static final String DB_NAME_TEST = "memorybox_test";

   /** The field name for setting references */
   private static final String REF_NAME = "ref_id";

   /**
    * Constructor
    * 
    * @param mode
    *           The mode to connect in (prod/test)
    */
   public MongoInterface(final DBFactory.Mode mode)
   {
      currMode = mode;
   }

   /**
    * Clear the database
    * 
    * @throws IOException
    *            If we can't reach the database
    */
   @Override
   public final void clear() throws IOException
   {
      connect();
      mongo.dropDatabase();
   }

   /**
    * Connect to the database
    * 
    * @throws IOException
    *            if we can't connect
    */
   private void connect() throws IOException
   {
      if (mongo == null)
      {
         Mongo m = new Mongo();

         if (currMode == DBFactory.Mode.PRODUCTION)
            mongo = m.getDB(DB_NAME);
         else if (currMode == DBFactory.Mode.TESTING)
            mongo = m.getDB(DB_NAME_TEST);
      }
   }

   /**
    * Sets the properties on a given class
    * 
    * @param cls
    *           The class name to set for
    * @param obj
    *           The object to set the properties on
    * @param properties
    *           A {@link Map} from the property name to the class that
    *           implements it
    * @param refId
    *           The underlying memory reference
    * @throws IOException
    *            If we can't reach the database
    */
   private void setProperties(final Class<?> cls, final Memory obj,
         final Map<String, Class<?>> properties, final ObjectId refId) throws IOException
   {
      String collection = cls.getName();

      BasicDBObject query = new BasicDBObject();
      query.put(REF_NAME, refId);
      DBObject retObj = mongo.getCollection(collection).findOne(query);

      // Match up these properties
      for (Entry<String, Class<?>> propEntry : properties.entrySet())
         if (propEntry.getValue().equals(cls))
            setProperty(obj, propEntry.getKey(), retObj.get(propEntry.getKey()));
   }

   @Override
   public final Memory retrieveMemory(final long timestamp, final String className)
         throws IOException
   {
      try
      {
         // Create the given object
         Class<?> objType = Class.forName(className);
         Constructor<?> objCons = objType.getConstructor(new Class[0]);
         Memory memory = (Memory) objCons.newInstance(new Object[0]);

         // Get the ID number
         DBObject query = new BasicDBObject();
         query.put("timestamp", timestamp);
         DBObject res = mongo.getCollection(Memory.class.getName()).findOne(query);

         ObjectId id = (ObjectId) res.get("_id");
         memory.setTimestamp(timestamp);
         Map<String, Class<?>> properties = deriveProperties(memory);
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

   /**
    * ENrich a memory with other lower level information
    * 
    * @param obj
    *           The Memory to enrich
    * @param id
    *           The id of the reference
    * @param properties
    *           The properties with which to enrich
    * @throws IOException
    *            If we can't reach the database
    */
   public final void enrichMemory(final Memory obj, final ObjectId id,
         final Map<String, Class<?>> properties) throws IOException
   {
      // Ignore the core memory properties!
      Set<Class<?>> classes = new HashSet<Class<?>>(properties.values());
      classes.remove(Memory.class);
      for (Class<?> cls : classes)
         setProperties(cls, obj, properties, id);

   }

   @Override
   public final void storeMemory(final Memory mem) throws IOException
   {
      Map<String, Class<?>> propertyMap = deriveProperties(mem);

      // Store the memory in the memory table
      ObjectId id = store(Memory.class, mem, propertyMap, null);

      // Work through the other objects - ignore the Memory core stuff
      Set<Class<?>> allClasses = new HashSet<Class<?>>(propertyMap.values());
      allClasses.remove(Memory.class);
      for (Class<?> storeClass : allClasses)
         store(storeClass, mem, propertyMap, id);
   }

   /**
    * Store an object in the database
    * 
    * @param storeType
    *           The type to store
    * @param toStore
    *           The object to store
    * @param potProperties
    *           The properties of the object
    * @param refId
    *           The reference ID
    * @return Gets the underlying ID of the object
    * @throws IOException
    *            If we can't reach the databsae
    */
   private ObjectId store(final Class<?> storeType, final Object toStore,
         final Map<String, Class<?>> potProperties, final ObjectId refId) throws IOException
   {
      connect();

      String collection = storeType.getCanonicalName();
      BasicDBObject obj = new BasicDBObject();

      for (Entry<String, Class<?>> prop : potProperties.entrySet())
         if (prop.getValue().equals(storeType))
            obj.put(prop.getKey(), getObject(prop.getKey(), toStore));

      // Add the reference id if this isn't the base object
      if (refId != null)
         obj.put("ref_id", refId);

      mongo.getCollection(collection).insert(obj);
      return obj.getObjectId("_id");
   }
}
