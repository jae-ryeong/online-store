package com.project.onlinestore.service;

import com.project.onlinestore.Item.dto.request.RegistrationRequestDto;
import com.project.onlinestore.Item.dto.response.RegistrationResponseDto;
import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.Item.service.ItemService;
import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @DisplayName("정상적으로 상품이 등록")
    @Test
    public void registrationTest_OK() throws Exception{
        //given
        User user = sellerUser();
        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //when
        RegistrationResponseDto registration = itemService.registration(user.getUserName(), itemDto());

        //then
        verify(userRepository).findByUserName(any());
        assertThat(registration.itemName()).isEqualTo(itemDto().itemName());
        assertThat(registration.userId()).isEqualTo(user.getId());
    }

    @DisplayName("customerType 상품 등록 시도, 에러 발생")
    @Test
    public void registrationTest_roleType_Error() throws Exception{
        //given
        User user = customerUser();
        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> itemService.registration(user.getUserName(), itemDto())).isInstanceOf(ApplicationException.class);
    }

    @DisplayName("item 전체 목록 조회")
    @Test
    public void allItemSearchTest() throws Exception{
        //given
        Pageable pageable = Mockito.mock(Pageable.class);

        //when
        when(itemRepository.findAll(pageable)).thenReturn(Page.empty());

        //then
        assertThatCode(() -> itemService.findAllItem(pageable)).doesNotThrowAnyException();
    }

    private User sellerUser() {
        return User.builder().userName("seller")
                .password("1234")
                .storeName("회사")
                .roleType(RoleType.SELLER)
                .build();
    }

    private User customerUser() {
        return User.builder().userName("seller")
                .password("1234")
                .roleType(RoleType.CUSTOMER)
                .build();
    }

    private Item createItem() {
        return Item.builder()
                .itemName("item"). quantity(100).price(10000).build();
    }

    private RegistrationRequestDto itemDto() {
        return new RegistrationRequestDto("item", 100, 10000, Category.PET);
    }
}