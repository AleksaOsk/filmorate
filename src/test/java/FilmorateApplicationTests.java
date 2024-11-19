import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.requests.UserRequests;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserInterface;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor
@ContextConfiguration(classes = {UserRepository.class, UserRowMapper.class, UserInterface.class, UserRequests.class, UserMapper.class})
class FilmorateApplicationTests {

    private User user;

    @MockBean
    @Qualifier("userService")
    private UserInterface userService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private UserRequests userRequests;

    @BeforeEach
    public void beforeEach() {
        user = new User();

        user.setName("user1");
        user.setLogin("user_1");
        user.setEmail("user1@gmail.com");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        userRepository.addNewUser(user);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userRepository.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}