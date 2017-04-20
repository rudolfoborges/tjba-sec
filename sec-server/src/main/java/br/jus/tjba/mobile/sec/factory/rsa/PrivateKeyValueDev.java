package br.jus.tjba.mobile.sec.factory.rsa;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.jus.tjba.mobile.sec.factory.rsa.exception.PrivateKeyNotFoundException;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@Component
public class PrivateKeyValueDev implements PrivateKeyValue {

    public byte[] getValue(){
        try {
            final Resource resource = new ClassPathResource("private-key-dev.der");
            final InputStream in = resource.getInputStream();

            return IOUtils.toByteArray(in);
        } catch (IOException e){
            throw new PrivateKeyNotFoundException();
        }
    }

}
