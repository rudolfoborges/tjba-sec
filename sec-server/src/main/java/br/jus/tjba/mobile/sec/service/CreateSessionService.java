package br.jus.tjba.mobile.sec.service;


import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import br.jus.tjba.mobile.sec.model.Session;
import br.jus.tjba.mobile.sec.repository.SessionRepository;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@Service
public class CreateSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSessionService.class);

    @Autowired
    private SessionRepository sessionRepository;

    public Session createNewSession(final String ip){
        final String uuid = UUID.randomUUID().toString();
        final String key = create3DesKey();

        Assert.notNull(key, "A chave não pode ser nula");

        final Session session = Session
                .builder()
                .id(uuid)
                .key(key)
                .createdAt(new Date())
                .ip(ip)
                .build();

        sessionRepository.save(session);

        return session;
    }

    private String create3DesKey(){
        try {
            final String seed = String.valueOf(new Date().getTime());

            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] digestOfPassword = md.digest(seed.getBytes("utf-8"));

            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey keySpec = new SecretKeySpec(keyBytes, "DESede");

            return new String(Base64.encode(keySpec.getEncoded()));

        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

}
