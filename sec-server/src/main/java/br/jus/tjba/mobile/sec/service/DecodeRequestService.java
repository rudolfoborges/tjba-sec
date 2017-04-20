package br.jus.tjba.mobile.sec.service;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;

import br.jus.tjba.mobile.sec.factory.rsa.PrivateKeyValue;
import br.jus.tjba.mobile.sec.factory.rsa.PrivateKeyValueFactory;

/**
 * Created by rudolfoborges on 19/04/17.
 */
public class DecodeRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecodeRequestService.class);

    private static final String ALGORITHM = "RSA";

    @Autowired
    private PrivateKeyValueFactory privateKeyValueFactory;

    public Map<String, Object> decode(final String request){
        try {
            final PrivateKeyValue privateKeyValue = privateKeyValueFactory.getPrivateKey();

            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyValue.getValue());
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

            final PrivateKey privateKey = keyFactory.generatePrivate(spec);

            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            final byte[] decodedRequest = cipher.doFinal(Base64.decode(request));

            return new GsonJsonParser().parseMap(new String(decodedRequest));

        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

}
