package br.jus.tjba.mobile.sec.factory.rsa.exception;

/**
 * Created by rudolfoborges on 19/04/17.
 */
public class PrivateKeyNotFoundException extends RuntimeException {

    public PrivateKeyNotFoundException(){
        super("Private Key Not Found");
    }

}
