/**
 * Αυτό είναι unit test για το UserService με Mockito, χωρίς Spring και χωρίς πραγματική βάση.
 * Στο @BeforeEach φτιάχνει ένα mock UserRepository (άρα δεν υπάρχει DB), δημιουργεί το
 * UserService και του “δίνει” το mock repository. Κάθε test ορίζει τη συμπεριφορά του
 * repository με when(...).thenReturn(...), καλεί τις μεθόδους του service και ελέγχει
 * το αποτέλεσμα με assertions, ενώ παράλληλα επιβεβαιώνει ότι έγιναν οι σωστές κλήσεις
 * στο repository (verify, never). Τα tests καλύπτουν: createUser (true όταν δεν υπάρχει
 * email και κάνει save, false όταν υπάρχει και δεν κάνει save), validateLogin (false αν
 * δεν βρεθεί χρήστης, true/false ανάλογα με password), getLeaderboard (επιστρέφει ό,τι
 * δίνει το repository), και τέλος με ArgumentCaptor ελέγχει ότι ο χρήστης που περνάει
 * στο save έχει τα σωστά πεδία (email/password/fullName).
 */


package org.MY_APP.main.service;

import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        // mock repository (δεν υπάρχει DB εδώ)
        userRepository = Mockito.mock(UserRepository.class);

        // service instance
        userService = new UserService(userRepository);

        // inject το mock repository στο private field (χωρίς Spring)
        // (έτσι δεν χρειάζεται @SpringBootTest)
        try {
            var f = UserService.class.getDeclaredField("userRepository");
            f.setAccessible(true);
            f.set(userService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // -------------------------
    // createUser tests
    // -------------------------

    @Test
    void createUser_returnsTrue_whenEmailDoesNotExist() {
        // given
        when(userRepository.findByEmail("a@test.com")).thenReturn(null);

        // when
        boolean ok = userService.createUser("a@test.com", "1234", "Alex");

        // then
        assertTrue(ok);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_returnsFalse_whenEmailAlreadyExists() {
        // given
        when(userRepository.findByEmail("a@test.com"))
                .thenReturn(new User("a@test.com", "pass", "Someone"));

        // when
        boolean ok = userService.createUser("a@test.com", "1234", "Alex");

        // then
        assertFalse(ok);
        verify(userRepository, never()).save(any());
    }

    // -------------------------
    // validateLogin tests
    // -------------------------

    @Test
    void validateLogin_returnsFalse_whenUserNotFound() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(null);

        assertFalse(userService.validateLogin("missing@test.com", "1234"));
    }

    @Test
    void validateLogin_returnsTrue_whenPasswordMatches() {
        when(userRepository.findByEmail("a@test.com"))
                .thenReturn(new User("a@test.com", "1234", "Alex"));

        assertTrue(userService.validateLogin("a@test.com", "1234"));
    }

    @Test
    void validateLogin_returnsFalse_whenPasswordWrong() {
        when(userRepository.findByEmail("a@test.com"))
                .thenReturn(new User("a@test.com", "1234", "Alex"));

        assertFalse(userService.validateLogin("a@test.com", "wrong"));
    }

    // -------------------------
    // leaderboard test
    // -------------------------

    @Test
    void getLeaderboard_returnsTop10FromRepository() {
        List<User> fake = List.of(
                new User("u1@test.com", "p", "U1"),
                new User("u2@test.com", "p", "U2")
        );

        when(userRepository.findTop10ByOrderByTotalQuizScoreDesc()).thenReturn(fake);

        List<User> result = userService.getLeaderboard();

        assertEquals(2, result.size());
        assertEquals("u1@test.com", result.get(0).getEmail());
        verify(userRepository).findTop10ByOrderByTotalQuizScoreDesc();
    }


    @Test
    void createUser_savesCorrectUser() {
        when(userRepository.findByEmail("a@test.com")).thenReturn(null);

        boolean ok = userService.createUser("a@test.com", "1234", "Alex");

        assertTrue(ok);

        var captor = org.mockito.ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("a@test.com", saved.getEmail());
        assertEquals("1234", saved.getPassword());
        assertEquals("Alex", saved.getFullName());
    }
}
