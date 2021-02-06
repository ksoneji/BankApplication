package com.bank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * <p>
 * Jackson ObjectMapper configuration class which also provides various ways to read / write JSON-formatter text. It creates ObjectMapper as
 * a singleton declared as a static global using static methods for reading / writing json.
 * </p>
 * 
 * @author Ketan.Soneji
 *
 */
public final class Json
{
   private static final Json DEFAULT_SERIALIZER;
   private static final Json NON_NULL_SERIALIZER;

   private final ObjectMapper mapper;
   private final ObjectWriter writer;
   private final ObjectWriter prettyWriter;

   static
   {
      ObjectMapper mapper = new ObjectMapper();

      mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
      mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
      mapper.findAndRegisterModules();

      // Don't throw an exception when json has extra fields you are not serializing on. This is useful when you want to use a pojo
      // for deserialization and only care about a portion of the json
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      // Indicate to mean that constructor should be considered a property-based Creator
      mapper.configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, true);

      // Indicates that the mapper must de-serialize enums in a case-insensitive manner
      mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

      // Write times as a String instead of a Long so its human readable.
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      // Register JavaTimeModule for enabling serialization of java.time object
      mapper.registerModule(new JavaTimeModule());

      // Register ParameterNamesModule to support introspection of method/constructor parameter names, without having to add explicit
      // property name annotation.
      mapper.registerModule(new ParameterNamesModule());

      // Register JavaTimeModule for enabling serialization of java.util.Optional object. Configuration setting that determines whether
      // `Optional.empty()` is considered "same as null" for serialization purposes; that is, to be filtered same as nulls are.
      mapper.registerModule(new Jdk8Module());

      // Set the default serializer; includes null values during serialization
      DEFAULT_SERIALIZER = new Json(mapper);

      /**
       * -----------------------------------------------------------------------<br>
       *  Setup a non-null serializer<br>
       * -----------------------------------------------------------------------<br>
       */
      // Specify serializer to use for serializingall non-null JSON property names
      mapper.getSerializerProvider().setNullKeySerializer(new MapNullKeySerializer());

      // Create new mapper for the non null serialization from the above created mapper with all configuration
      ObjectMapper nonNullMapper = mapper.copy();
      Jdk8Module jdk8Module = new Jdk8Module();
      jdk8Module.configureAbsentsAsNulls(true);
      nonNullMapper.registerModule(jdk8Module);
      
      // Set the non-null serializer; excludes (ignores) null values during serialization
      nonNullMapper.setSerializationInclusion(Include.NON_NULL);
      NON_NULL_SERIALIZER = new Json(nonNullMapper);
   }

   // Only let this be called statically. Hide the constructor
   private Json(ObjectMapper mapper)
   {
      this.mapper = mapper;
      this.writer = mapper.writer();
      this.prettyWriter = mapper.writerWithDefaultPrettyPrinter();
   }

   /**
    * <p>
    * Returns the JSON serializer (current singleton object) instance.
    * </p>
    * 
    * @return The JSON serializer
    */
   public static Json serializer()
   {
      return DEFAULT_SERIALIZER;
   }

   /**
    * <p>
    * Returns the JSON non null serializer (current singleton object) instance, which is required if need to serialize object to include
    * only non null fields
    * </p>
    * 
    * @return The JSON non null serializer
    */
   public static Json nonNullSerializer()
   {
      return NON_NULL_SERIALIZER;
   }

   /**
    * <p>
    * Returns the object mapper instance.
    * </p>
    * 
    * @return The JSON object mapper
    */
   public ObjectMapper mapper()
   {
      return mapper;
   }

   /**
    * <p>
    * Builder object for JSON generation that can be used for per-serialization configuration of serialization parameters, such as JSON View
    * and root type to use.
    * </p>
    * 
    * @return The Jackson Object Writer
    */
   public ObjectWriter writer()
   {
      return writer;
   }

   /**
    * <p>
    * Builder object for JSON generation that can be used for per-serialization configuration of serialization parameters, such as JSON View
    * and root type to use.
    * </p>
    * 
    * @return The Jackson Object Writer to pretty print JSON
    */
   public ObjectWriter prettyWriter()
   {
      return prettyWriter;
   }

   /**
    * <p>
    * Parse or deserialize JSON content stored in a Java byte array to a destination Java type.
    * </p>
    * 
    * @param bytes
    *           The byte array
    * @param typeRef
    *           The destination object type
    * @param <T>
    *           The descriptive type parameter (classname)
    * @return JSON content de-serialized and converted into the destination object type
    */
   public <T> T fromJson(byte[] bytes, TypeReference<T> typeRef)
   {
      try
      {
         return mapper.readValue(bytes, typeRef);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content from a JSON string object to a destination Java type.
    * </p>
    * 
    * @param json
    *           The JSON string
    * @param typeRef
    *           The destination java object type
    * @param <T>
    *           The descriptive type parameter (classname)
    * @return JSON content de-serialized and converted into the destination object type
    */
   public <T> T fromJson(String json, TypeReference<T> typeRef)
   {
      try
      {
         return mapper.readValue(json, typeRef);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content represent by a JsonNode to a destination Java type.
    * </p>
    * 
    * @param node
    *           The JSON node
    * @param typeRef
    *           The destination object type
    * @param <T>
    *           The descriptive type parameter (classname)
    * @return JSON content de-serialized and converted into the destination object type
    */
   public <T> T fromNode(JsonNode node, TypeReference<T> typeRef)
   {
      try
      {
         return mapper.readValue(node.toString(), typeRef);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content represented by a Java object to a destination Java type.
    * </p>
    * 
    * @param obj
    *           The Java object
    * @param typeRef
    *           The destination object type
    * @param <T>
    *           The descriptive type parameter (classname)
    * @return JSON content de-serialized and converted into the destination object type
    */
   public <T> T fromObject(Object obj, TypeReference<T> typeRef)
   {
      try
      {
         return mapper.readValue(toString(obj), typeRef);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content from an InputStream to a destination Java type.
    * </p>
    * 
    * @param is
    *           The input stream
    * @param typeRef
    *           The destination object type
    * @param <T>
    *           The descriptive type parameter (classname)
    * @return JSON content de-serialized and converted into the destination object type
    */
   public <T> T fromInputStream(InputStream is, TypeReference<T> typeRef)
   {
      try
      {
         return mapper.readValue(is, typeRef);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content from a Reader input to a destination Java type.
    * </p>
    * 
    * @param file
    *           The input file
    * @param valueType
    *           The destination object type
    * @param <T>
    *           The de-serialized object
    * @return JSON content de-serialized and converted into the destination object type
    */

   public <T> T fromFile(File file, Class<T> valueType)
   {
      try
      {
         return mapper.readValue(file, valueType);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content from an reader to a destination Java type.
    * </p>
    * 
    * @param reader
    *           The File reader
    * @param typeRef
    *           The destination object type
    * @param <T>
    *           The descriptive type parameter (classname)
    * @return JSON content de-serialized and converted into the destination object type
    */
   public <T> T fromReader(Reader reader, TypeReference<T> typeRef)
   {
      try
      {
         return mapper.readValue(reader, typeRef);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Serialize JSON content represented by a Java object to a JSON string.
    * </p>
    * 
    * @param obj
    *           The java object
    * @return The JSON string
    */
   public String toString(Object obj)
   {
      try
      {
         return writer.writeValueAsString(obj);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Serialize the JSON schema represented by Java object into a pretty-printed JSON string.
    * </p>
    * 
    * @param obj
    *           The Java object
    * @return A pretty-printed JSON string
    */
   public String toPrettyString(Object obj)
   {
      try
      {
         return prettyWriter.writeValueAsString(obj);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Serialize the JSON schema represented by Java object into a byte array.
    * </p>
    * 
    * @param obj
    *           The Java object
    * @return A byte array containing a JSON schema
    */
   public byte[] toByteArray(Object obj)
   {
      try
      {
         return prettyWriter.writeValueAsBytes(obj);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Serialize the JSON schema represented by a JSON string into a Java Map object.
    * </p>
    * 
    * @param json
    *           The Json string
    * @return A Java Map representing the JSON schema
    */
   public Map<String, Object> mapFromJson(String json)
   {
      try
      {
         return mapper.readValue(json, new TypeReference<Map<String, Object>>()
         {
         });
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Serialize the JSON schema represented by a byte array into a Java Map object.
    * </p>
    * 
    * @param bytes
    *           The byte array
    * @return A Java map representing the JSON schema
    */
   public Map<String, Object> mapFromJson(byte[] bytes)
   {
      try
      {
         return mapper.readValue(bytes, new TypeReference<Map<String, Object>>()
         {
         });
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize the JSON schema represented by a InputStream into a Java Map object.
    * </p>
    * 
    * @param stream
    *           The input stream
    * @return A Java map representing the JSON schema
    */
   public Map<String, Object> mapFromStream(InputStream stream)
   {
      try
      {
         return mapper.readValue(readFromInputStream(stream), new TypeReference<Map<String, Object>>()
         {
         });
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize JSON content stored in a string object to a JsonNode.
    * </p>
    * 
    * @param json
    *           The Json string
    * @return A JsonNode object representing the json tree model
    */
   public JsonNode nodeFromJson(String json)
   {
      try
      {
         return mapper.readTree(json);
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Parse or de-serialize the JSON schema represented by a Java Object into JsonNode.
    * </p>
    * 
    * @param obj
    *           The Java object
    * @return A JsonNode object representing the json tree model
    */
   public JsonNode nodeFromObject(Object obj)
   {
      try
      {
         return mapper.readTree(toString(obj));
      }
      catch (IOException e)
      {
         throw new JsonException(e);
      }
   }

   /**
    * <p>
    * Reads and returns a string from the input stream.
    * </p>
    * 
    * @param inputStream
    *           The input stream
    * @return The Json string
    * @throws IOException
    *            This exception is thrown if an error occurs while reading from the stream
    */
   private static String readFromInputStream(InputStream inputStream)
      throws IOException
   {
      StringBuilder resultStringBuilder = new StringBuilder();
      try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
      {
         String line;
         while ((line = br.readLine()) != null)
         {
            resultStringBuilder.append(line).append("\n");
         }
      }
      return resultStringBuilder.toString();
   }

   /**
    * <p>
    * The custom runtime exception - JsonException
    * </p>
    * 
    * @author Darpan.Thanki
    *
    */
   public static final class JsonException extends RuntimeException
   {
      /**
       * 
       */
      private static final long serialVersionUID = -9169593690230422478L;

      private JsonException(Exception ex)
      {
         super(ex);
      }
   }
}
