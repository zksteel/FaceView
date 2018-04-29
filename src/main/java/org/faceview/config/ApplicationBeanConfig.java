package org.faceview.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class ApplicationBeanConfig {
    public static final String CLOUD_STORAGE_BUCKET = "test-7eb29.appspot.com";
    private static final String CONFIG_FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\test-2c45afcbf519.json";

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    //Google Cloud Storage Configuration
    @Bean
    public Storage storage(){
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials
                            .fromStream(new FileInputStream(CONFIG_FILE_PATH)))
                    .build().getService();

            return storage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
