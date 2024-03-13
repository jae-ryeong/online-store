package com.project.onlinestore.user.service;

import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.user.dto.response.AddressListResponseDto;
import com.project.onlinestore.user.dto.request.AddressRegistrationRequestDto;
import com.project.onlinestore.user.dto.response.AddressRegistrationResponseDto;
import com.project.onlinestore.user.entity.Address;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.repository.AddressRepository;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressRegistrationResponseDto addressRegistration(String userName, AddressRegistrationRequestDto dto) {
        User user = findUser(userName);

        Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$");

        if (!pattern.matcher(dto.tel()).find()) {    // 010-0000-0000 pattern 아니라면 예외 발생
            throw new ApplicationException(ErrorCode.DIFFERENT_FROM_THE_PATTERN, null);
        }

        addressRepository.save(Address.builder()
                .addresseeName(dto.addresseeName())
                .address(dto.address())
                .user(user)
                .detailAddress(dto.detailAddress())
                .postalCode(dto.postalCode())
                .tel(dto.tel())
                .build()
        );

        return new AddressRegistrationResponseDto(dto.addresseeName(), dto.address(), dto.detailAddress(), dto.postalCode(), dto.tel());
    }

    public void addressDelete(String userName, Long addressId) {
        User user = findUser(userName);
        Address address = findAddress(addressId);

        if (address.getUser() != user) {
            throw new ApplicationException(ErrorCode.INVALID_USER, "권한이 없는 유저입니다.");
        }

        addressRepository.deleteById(addressId);
    }

    public List<AddressListResponseDto> addressListView(String userName) {
        User user = findUser(userName);

        List<Address> userAddress = addressRepository.findAllByUserId(user.getId());

        return userAddress.stream().map(AddressListResponseDto::fromEntity).collect(Collectors.toList());
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));
    }

    private Address findAddress(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ADDRESS_NOT_FOUNT, "저장된 배송지를 찾을 수 없습니다."));
    }
}
