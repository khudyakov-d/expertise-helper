package ru.nsu.ccfit.khudyakov.expertise_helper.features.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.ccfit.khudyakov.expertise_helper.props.UserSecurityProps;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceTest {

    @Mock
    private UserSecurityProps props;

    @InjectMocks
    private UserSecurityService userSecurityService;

    @Test
    void saveAndLoadKey_withNotNullPassword_Success() {
        Mockito.when(props.getKeyStorePassword()).thenReturn("123321".toCharArray());
        Mockito.when(props.getKeyStoreName()).thenReturn("keystore_test.jceks");

        userSecurityService.loadKeyStore();
        User user = new User();
        user.setEmail("test@test.ru");

        userSecurityService.saveMailPassword(user, "test".toCharArray());
        char[] mailPassword = userSecurityService.getMailPassword(user);
        assertArrayEquals(mailPassword, "test".toCharArray());

        userSecurityService.deleteMailPassword(user);
        assertFalse(userSecurityService.checkMailPassword(user));
    }

}