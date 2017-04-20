package br.jus.tjba.mobile.sec.service;

import com.google.gson.Gson;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import br.jus.tjba.mobile.sec.factory.rsa.PrivateKeyValue;
import br.jus.tjba.mobile.sec.factory.rsa.PrivateKeyValueFactory;
import br.jus.tjba.mobile.sec.model.Session;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@Service
public class EncodeResponseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodeResponseService.class);

    private static final String ALGORITHM = "RSA";

    @Autowired
    private PrivateKeyValueFactory privateKeyValueFactory;

    public String encode(final Session session){
        try {
            final PrivateKeyValue privateKeyValue = privateKeyValueFactory.getPrivateKey();

            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyValue.getValue());
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            final PrivateKey privateKey = keyFactory.generatePrivate(spec);

            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            final byte[] encodedResponse = cipher.doFinal(buildJSon(session));

            return new String(Base64.encode(encodedResponse));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public byte[] buildJSon(final Session session) throws UnsupportedEncodingException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        final Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("triploDesKey", session.getKey());
        response.put("createdAt", LocalDateTime.now().format(formatter));

        final LocalDateTime expiresTime = LocalDateTime.now().plusMinutes(15l);
        response.put("expires", expiresTime.format(formatter));

        return new Gson().toJson(response).getBytes("utf-8");
    }

}
