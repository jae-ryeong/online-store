package com.project.onlinestore.user.repository;

import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() throws Exception {
        //given
        User user = User.builder()
                .userName("test")
                .password("1234")
                .roleType(RoleType.CUSTOMER)
                .build();

        userRepository.save(user);
        //when
        User user1 = userRepository.findByUserName("test").get();

        //then
        assertThat(user1).isEqualTo(user);
    }
    @Test
    public void findRoleByUserNameTest() throws Exception {
        //given
        User user = User.builder()
                .userName("test")
                .password("1234")
                .roleType(RoleType.CUSTOMER)
                .build();

        userRepository.save(user);

        //when
        RoleType roleByUserName = userRepository.findRoleByUserName(user.getUserName());

        //then
        assertThat(roleByUserName).isEqualTo(RoleType.CUSTOMER);
    }
}