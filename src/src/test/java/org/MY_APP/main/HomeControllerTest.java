/**
 *Αυτό το test είναι unit test του HomeController, όπου ελέγχεται η συμπεριφορά του controller
 * χωρίς Spring context. Χρησιμοποιεί MockMvc σε standaloneSetup και Mockito mocks για τα
 * dependencies (TmdbService, FavoriteRepository), ώστε να μη γίνουν πραγματικές κλήσεις
 * σε API ή βάση. Στο @BeforeEach ορίζεται ένας dummy ViewResolver για να μη χρειάζεται
 * πραγματικό template. Τα tests καλούν το /home endpoint και ελέγχουν ότι: χωρίς χρήστη
 * στο session επιστρέφεται το view "home" με HTTP 200, και με χρήστη στο session δεν
 * γίνεται redirect στο /login, αφού όλα τα calls του controller (trending movies, popular
 * actors, favorites) είναι stubbed και επιστρέφουν ασφαλή δεδομένα.
 */

package org.MY_APP.main;

import org.MY_APP.main.model.User;
import org.MY_APP.main.repository.FavoriteRepository;
import org.MY_APP.main.service.TmdbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TmdbService tmdbService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setup() {
        ViewResolver dummyViewResolver = (viewName, locale) -> new View() {
            @Override
            public String getContentType() { return "text/html"; }

            @Override
            public void render(
                    java.util.Map<String, ?> model,
                    jakarta.servlet.http.HttpServletRequest request,
                    jakarta.servlet.http.HttpServletResponse response
            ) throws Exception {
                response.setStatus(200);
                response.getWriter().write("OK");
            }
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(homeController)
                .setViewResolvers(dummyViewResolver)
                .build();
    }

    @Test
    void home_returnsOk_whenNoSessionUser() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void home_doesNotRedirectToLogin_whenSessionUserExists() throws Exception {
        User u = new User("a@test.com", "1234", "Alex");

        // stubs για ό,τι καλεί ο controller
        when(tmdbService.getTrendingMovies(1)).thenReturn(List.of());
        when(tmdbService.getPopularActors()).thenReturn(List.of());

        // το NPE που είδες: homeController καλεί favoriteRepository.findByUser(...)
        when(favoriteRepository.findByUser(u)).thenReturn(List.of());

        mockMvc.perform(get("/home").sessionAttr("loggedUser", u))
                // Το μόνο που μας νοιάζει εδώ: δεν σε πετάει στο /login
                .andExpect(status().isOk());
    }
}
