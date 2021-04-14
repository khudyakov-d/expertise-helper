package ru.nsu.ccfit.khudyakov.expertise_helper.features.users;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.khudyakov.expertise_helper.props.UserSecurityProps;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.KeyStore.PasswordProtection;
import static java.security.KeyStore.ProtectionParameter;
import static java.security.KeyStore.SecretKeyEntry;
import static java.security.KeyStore.getInstance;
import static java.util.Arrays.copyOf;

@Service
@Setter
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserSecurityProps userSecurityProps;


    private static final String ALGORITHM = "AES";

    private static final int KEY_SIZE = 128;

    private KeyStore keyStore;

    private Cipher cipher;

    @PostConstruct
    public void loadKeyStore() {
        try (FileInputStream keyFile = new FileInputStream(userSecurityProps.getKeyStoreName())) {
            keyStore = getInstance("jceks");
            keyStore.load(keyFile, userSecurityProps.getKeyStorePassword());
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public void deleteMailPassword(User user) {
        try {
            keyStore.deleteEntry(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public boolean checkMailPassword(User user) {
        try {
            return keyStore.containsAlias(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }


    public void saveMailPassword(User user, char[] password) {
        try {
            SecretKey key = getSecretKey();
            saveKey(user, key);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            user.setMailPassword(new String(cipher.doFinal(charsToBytes(password))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private void saveKey(User user, SecretKey key) throws KeyStoreException {
        SecretKeyEntry secret = new SecretKeyEntry(key);
        ProtectionParameter protectionPassword = new PasswordProtection(userSecurityProps.getKeyStorePassword());
        keyStore.setEntry(user.getEmail(), secret, protectionPassword);
    }

    private SecretKey getSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        return generator.generateKey();
    }

    public char[] getMailPassword(User user) {
        try {
            Key key = keyStore.getKey(
                    user.getEmail(),
                    userSecurityProps.getKeyStorePassword()
            );
            byte[] keyEncoded = key.getEncoded();
            SecretKey secretKey = new SecretKeySpec(keyEncoded, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return bytesToChars(cipher.doFinal(user.getMailPassword().getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public byte[] charsToBytes(char[] chars) {
        final ByteBuffer byteBuffer = UTF_8.encode(CharBuffer.wrap(chars));
        return copyOf(byteBuffer.array(), byteBuffer.limit());
    }

    public char[] bytesToChars(byte[] bytes) {
        final CharBuffer charBuffer = UTF_8.decode(ByteBuffer.wrap(bytes));
        return copyOf(charBuffer.array(), charBuffer.limit());
    }

}
