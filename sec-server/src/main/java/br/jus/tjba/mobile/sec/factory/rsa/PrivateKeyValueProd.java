package br.jus.tjba.mobile.sec.factory.rsa;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import br.jus.tjba.mobile.sec.factory.rsa.exception.PrivateKeyNotFoundException;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@Component
public class PrivateKeyValueProd implements PrivateKeyValue {

    @Value("{private.key}")
    private String privateKeyPath;

    @Override
    public byte[] getValue() {
        try {
            final InputStream in = new FileInputStream(privateKeyPath);
            return IOUtils.toByteArray(in);

        } catch (IOException e) {
            throw new PrivateKeyNotFoundException();
        }

    }

}
