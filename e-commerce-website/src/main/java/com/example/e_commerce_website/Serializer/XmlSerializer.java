package com.example.e_commerce_website.Serializer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.ByteArrayOutputStream;

public class XmlSerializer<T> implements Serializer<T> {

    private final Class<T> type;

    public XmlSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            JAXBContext context = JAXBContext.newInstance(type);
            Marshaller marshaller = context.createMarshaller();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(data, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Error serializing to XML", e);
        }
    }
}
