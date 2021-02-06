package com.bank.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * <p>
 * Map Null Key Serializer
 * </p>
 * 
 * @author Ketan.Soneji
 *
 */
public class MapNullKeySerializer extends JsonSerializer<Object>
{
   @Override
   public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException
   {
      jsonGenerator.writeFieldName("");
   }
}
