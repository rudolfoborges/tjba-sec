package br.jus.tjba.mobile.sec.factory.rsa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@Component
public class PrivateKeyValueFactory {

    @Autowired
    private PrivateKeyValue privateKeyValueDev;

    @Autowired
    private PrivateKeyValue privateKeyValueProd;

    @Autowired
    private Environment environment;

    public PrivateKeyValue getPrivateKey(){

        if(isActiveProfile("dev")){
            return privateKeyValueDev;
        }

        return privateKeyValueProd;
    }

    private boolean isActiveProfile(final String profile){
        final long count = Stream
                .of(environment.getActiveProfiles())
                .filter(p -> p.equals(profile))
                .count();

        return count > 0;
    }


}
