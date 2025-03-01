package com.project.onlinestore.user.service;

import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import com.project.onlinestore.jwt.util.JwtTokenUtils;
import com.project.onlinestore.user.dto.request.CustomerRequestDto;
import com.project.onlinestore.user.dto.request.SellerRequestDto;
import com.project.onlinestore.user.dto.response.LoginResponseDto;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final JwtTokenUtils jwtTokenUtils;
    private final BCryptPasswordEncoder encoder;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret-key}")
    private String key;

    @Transactional
    public void customerJoin(CustomerRequestDto dto) {
        userRepository.findByUserName(dto.userName()).ifPresent(user -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s는 이미 존재하는 아이디입니다.", dto.userName()));
        });

        Cart cart = cartRepository.save(Cart.builder().build()); // 회원 가입시 자동적으로 cart(장바구니)도 만들어져야 한다.

        userRepository.save(User.builder()
                .userName(dto.userName())
                .password(encoder.encode(dto.password()))
                .roleType(RoleType.CUSTOMER)
                .cart(cart)
                .build()
        );
    }

    @Transactional
    public void sellerJoin(SellerRequestDto dto) {
        userRepository.findByUserName(dto.userName()).ifPresent(User -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s는 이미 존재하는 아이디입니다.", dto.userName()));
        });

        Cart cart = cartRepository.save(Cart.builder().build());

        userRepository.save(User.builder()
                .userName(dto.userName())
                .password(encoder.encode(dto.password()))
                .roleType(RoleType.SELLER)
                .storeName(dto.storeName())
                .cart(cart)
                .build()
        );
    }

    public LoginResponseDto login(String userName, String password) {
        User user = userRepository.findByUserName(userName).orElseThrow(() ->
                new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, userName + "를 찾을 수 없습니다."));

        // 비밀번호 체크
        if (!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, password + ", 패스워드가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenUtils.generateToken(userName);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userName);

        return LoginResponseDto.builder()
                .userName(user.getUserName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponseDto checkRefreshAndReIssueAccess(String refreshToken) {
        // refreshToken 검증
        jwtTokenUtils.validationToken(refreshToken, key);

        String userName = jwtTokenUtils.getUserName(refreshToken, key);

        String redisRefreshToken = jwtTokenUtils.getRedisRefreshToken(userName);
        if(!redisRefreshToken.equals(refreshToken)){    // refreshToken 값 비교 후 다르면 에러 발생
            throw new ApplicationException(ErrorCode.NOT_FOUNT_REFRESH_TOKEN, null);
        }

        // 토큰 재발행
        String accessToken = jwtTokenUtils.generateToken(userName);

        return new LoginResponseDto(userName, accessToken, refreshToken);
    }

    public void logout(String accessToken) {
        jwtTokenUtils.validationToken(accessToken, key);
        String userName = jwtTokenUtils.getUserName(accessToken, key);

        // Redis에 refresh Token이 존재한다면
        if (jwtTokenUtils.getRedisRefreshToken(userName) != null) {
            // Refresh Token 삭제
            jwtTokenUtils.deleteRefreshToken(userName);
        } else {
            throw new ApplicationException(ErrorCode.INVALID_USER, "이미 로그아웃한 유저입니다.");
        }

        // Access Token의 유효시간을 가지고 와서 BlackList에 저장
        Long expiration = jwtTokenUtils.getExpiration(accessToken);

        jwtTokenUtils.accessTokenBlackList(accessToken, expiration);
    }

    public boolean idCheck(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public RoleType roleTypeCheck(String userName){
        return userRepository.findRoleByUserName(userName);
    }

    public String storeNameReturn(String userName) {
        return userRepository.returnStoreName(userName);
    }

    public boolean jwtValidate(String accessToken) {
        return jwtTokenUtils.validationToken(accessToken, key);
    }
}
