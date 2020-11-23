package net.mckitsu.lib.util;

import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AES extends net.mckitsu.lib.util.Encrypt {
    public AES(byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        this(new SecretKeySpec(key, "AES"));
    }

    public AES(Key key) throws InvalidKeyException, NoSuchAlgorithmException {
        super(key, Type.AES);
        if(!key.getAlgorithm().equalsIgnoreCase(Type.AES.getInstance()))
            throw new NoSuchAlgorithmException();
    }
}
