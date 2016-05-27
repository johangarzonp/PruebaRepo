package com.cotech.taxislibres.activities;

import org.jasypt.util.text.BasicTextEncryptor;

public class EncriptadorTexto {
	BasicTextEncryptor textEncryptor;

    public EncriptadorTexto(String ip) {
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ip);
    }
    
    public String encriptar(String textoAEncriptar) {      
        return textEncryptor.encrypt(textoAEncriptar);
    }
    
    public String desencriptar(String textoADesencriptar) {
        return textEncryptor.decrypt(textoADesencriptar);
    }
}
