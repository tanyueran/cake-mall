package com.github.tanyueran.component;

import com.github.tanyueran.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@Component
public class KeyComp {

    @Value("${self.key.private}")
    private String privateKey;

    @Value("${self.key.public}")
    private String publicKey;

    @Bean
    public PrivateKey privateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RsaUtil.getPrivateKey(privateKey);
    }

    @Bean
    public PublicKey publicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RsaUtil.getPublicKey(publicKey);
    }
}
