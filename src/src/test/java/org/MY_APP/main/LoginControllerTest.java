/**
 * Αυτό είναι unit test του LoginController με MockMvc σε standaloneSetup και Mockito,
 * χωρίς Spring context/DB. Κάνει mock το UserService και το “injectάρει” στον
 * controller (@InjectMocks), ώστε να ελέγξει μόνο τη λογική του endpoint /login.
 * Κάθε test στέλνει POST /login με parameters (email, password), κάνει stub τη συμπεριφορά
 * του userService.findByEmail(...), και μετά ελέγχει ότι ο controller κάνει το σωστό
 * redirect: όταν ο χρήστης υπάρχει και ο κωδικός ταιριάζει γίνεται redirect στο /home,
 * ενώ όταν ο χρήστης δεν βρεθεί ή ο κωδικός είναι λάθος γίνεται redirect στο /login?error=true
 */

package org.MY_APP.main;

import org.MY_APP.main.model.User;
import org.MY_APP.main.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }



    @Test
    void postLogin_redirectsToHome_whenCredentialsOk() throws Exception {
        User u = new User("a@test.com", "1234", "Alex");
        when(userService.findByEmail("a@test.com")).thenReturn(u);

        mockMvc.perform(post("/login")
                        .param("email", "a@test.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void postLogin_redirectsToLoginError_whenUserNotFound() throws Exception {
        when(userService.findByEmail("missing@test.com")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("email", "missing@test.com")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    void postLogin_redirectsToLoginError_whenPasswordWrong() throws Exception {
        User u = new User("a@test.com", "1234", "Alex");
        when(userService.findByEmail("a@test.com")).thenReturn(u);

        mockMvc.perform(post("/login")
                        .param("email", "a@test.com")
                        .param("password", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}
