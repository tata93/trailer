package com.httpconvertor;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class Config implements HttpMessageConverter<MultiValueMap<String, InputStreamResource>> {


    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return MultiValueMap.class.isAssignableFrom(clazz);
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return MultiValueMap.class.isAssignableFrom(clazz);
    }

    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.ALL);
    }

    public MultiValueMap<String, InputStreamResource> read(Class<? extends MultiValueMap<String, InputStreamResource>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        System.out.println("in");
        String[] boundaries = inputMessage.getHeaders().getContentType().toString().split("boundary=", 2);
        String s = boundaries[1];
        String[] boundary = s.split(";", 2);
        MultiValueMap<String, InputStreamResource> map = new LinkedMultiValueMap<String, InputStreamResource>();
        try {
            MultipartStream multipartStream = new MultipartStream(inputMessage.getBody(), boundary[0].getBytes(), 1048576, null);
            boolean nextPart = multipartStream.skipPreamble();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while (nextPart) {
                String header = multipartStream.readHeaders();
                // process headers
                // create some output stream
                multipartStream.readBodyData(output);
                nextPart = multipartStream.readBoundary();
                map.add("data", new InputStreamResource(new ByteArrayInputStream(output.toByteArray())));
            }
            if (multipartStream.skipPreamble()){
                System.out.println("to");
            }
            map.add("sign", new InputStreamResource(inputMessage.getBody()));
        } finally {

        }

        return map;
    }

    public void write(MultiValueMap<String, InputStreamResource> stringInputStreamResourceMultiValueMap, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }

}
