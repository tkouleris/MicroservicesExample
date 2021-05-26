package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.entity.Rating;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Service
public class CacheService<T> {

    public void save(String data, String key) throws IOException {
        FileWriter myWriter = new FileWriter("/MyWork/Projects/Microservices/MicroservicesExample/cache/"+key+".json");
        myWriter.write(data);
        myWriter.close();
    }

    public <T> T getKey(String key, Class<T> classpath) throws FileNotFoundException, JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        File file = new File("/MyWork/Projects/Microservices/MicroservicesExample/cache/"+key+".json");
        Scanner myReader = new Scanner(file);
//        RatingsResponse ratingsResponse = new RatingsResponse();
//        Object obj = new <Object>;
//        Rating[] ratings = new Rating[0];
//        Object obj = Class.forName(classpath).newInstance();
        while (myReader.hasNextLine()) {
            String response = myReader.nextLine();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            obj = objectMapper.readValue(response, classpath);
        }

        return (T)obj;
    }
}