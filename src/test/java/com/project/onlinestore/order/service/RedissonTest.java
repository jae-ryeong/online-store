package com.project.onlinestore.order.service;

import com.project.onlinestore.Item.entity.Item;
import com.project.onlinestore.Item.entity.enums.Category;
import com.project.onlinestore.Item.repository.ItemRepository;
import com.project.onlinestore.order.Entity.Order;
import com.project.onlinestore.order.Entity.OrderItem;
import com.project.onlinestore.order.repository.OrderItemRepository;
import com.project.onlinestore.order.repository.OrderRepository;
import com.project.onlinestore.user.entity.Cart;
import com.project.onlinestore.user.entity.ItemCart;
import com.project.onlinestore.user.entity.User;
import com.project.onlinestore.user.entity.enums.RoleType;
import com.project.onlinestore.user.repository.CartRepository;
import com.project.onlinestore.user.repository.ItemCartRepository;
import com.project.onlinestore.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// 실제 Spring Context를 로드하고 Redisson빈을 사용
@SpringBootTest
//@Transactional
public class RedissonTest {

    @Autowired private OrderService orderService;

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemCartRepository itemCartRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private OrderItemRepository  orderItemRepository;
    @Autowired private RedissonClient redissonClient;

    @PersistenceContext
    private EntityManager entityManager;

    // 테스트용 ExecutorService
    private ExecutorService executorService;

    // 테스트 데이터
    private User userA;
    private User userB;
    private Item sharedItem; // 두 사용자가 동시에 구매할 상품
    private Order orderA;
    private Order orderB;
    private Cart cartA;
    private Cart cartB;

    @BeforeEach
    void setUp() {
        cartA = Cart.builder().build();
        cartB = Cart.builder().build();
        cartA = cartRepository.save(cartA);
        cartB = cartRepository.save(cartB);

        userA = User.builder().userName("userA").password("1234").cart(cartA).roleType(RoleType.CUSTOMER).build();
        userB = User.builder().userName("userB").password("1234").cart(cartB).roleType(RoleType.SELLER).build();
        userA = userRepository.save(userA); // DB 저장 후 엔티티 받기
        userB = userRepository.save(userB);

        // 두 사용자가 구매할 상품
        sharedItem = Item.builder().itemName("ConcurrentItem").price(10000).quantity(1).mainImageUrl("url").soldCount(5L).soldOut(false).category(Category.PET).user(userB).description("설명").build();
        sharedItem = itemRepository.save(sharedItem);

        // 각 사용자의 장바구니에 동일 상품 추가
        ItemCart itemCartA = ItemCart.builder().item(sharedItem).quantity(1).cart(cartA).cartCheck(true).build();
        ItemCart itemCartB = ItemCart.builder().item(sharedItem).quantity(1).cart(cartB).cartCheck(true).build();
        itemCartRepository.save(itemCartA);
        itemCartRepository.save(itemCartB);

        // 각 사용자의 주문 엔티티 생성 (결제 완료되었다 가정)
        orderA = Order.builder().user(userA).build();
        orderB = Order.builder().user(userB).build();
        orderA = orderRepository.save(orderA);
        orderB = orderRepository.save(orderB);

        // 스레드 풀 설정 (ex: 2개의 스레드로 동시 실행 시뮬레이션)
        executorService = Executors.newFixedThreadPool(2);

        // 테스트 시작 전에 Redisson 락 키가 남아있을 경우 정리
        String lockKey = "lock:item:" + sharedItem.getId();

    }

    @AfterEach
    void tearDown() {
        executorService.shutdown(); // 스레드 풀 종료
    }

    @Test
    @DisplayName("두 사용자가 동일 상품 동시 주문시, 동시성 문제 해결 성공")
    public void testSuccessOrder_Success() throws Exception{
        //given
        int initialStock = sharedItem.getQuantity(); // 초기 재고 5

        // 락 획득 시도가 동시에 이루어지도록 CountDownLatch 사용
        CountDownLatch latch = new CountDownLatch(1);
        // 두 스레드의 실행 완료를 기다리기 위한 CountDownLatch
        CountDownLatch completionLatch = new CountDownLatch(2);

        //when  두 사용자가 주문 동시 실행
        List<Throwable> exceptions = new ArrayList<>(); // 스레드에서 발생한 예외를 수집
        
        // 사용자 A 주문 처리 스레드
        executorService.submit(() -> {
            try{
                latch.await();
                orderService.successOrder(userA.getUserName(), orderA.getId());
            }catch (Exception e){
                exceptions.add(e); // 예외 발생 시 리스트에 추가
            }finally {
                System.out.println("A실행 후 재고: " + sharedItem.getQuantity());
                completionLatch.countDown(); // 실행 완료 알림
            }
        });
        
        // 사용자 B 주문 처리 스레드
        executorService.submit(() -> {
            try{
                latch.await(); // 모든 스레드가 준비될 때까지 대기
                orderService.successOrder("userB", orderB.getId());
            }catch (Exception e){
                exceptions.add(e);
            }finally {
                System.out.println("B실행 후 재고: " + sharedItem.getQuantity());
                completionLatch.countDown();
            }
        });
        
        // 모든 스레드가 동시에 시작하도록 신호
        latch.countDown();
        
        // 모든 스레드가 종료될 때까지 대기
        completionLatch.await(10, TimeUnit.SECONDS);    // 최대 10초 대기

        //then
        
        // 1. 최종 재고 수량 확인, 초기재고 1, 최종 재고 0 이여야 정상
        Item finalItem = itemRepository.findById(sharedItem.getId())
                .orElseThrow(() -> new AssertionError("Test item not found after processing"));
        assertEquals(initialStock - 1, finalItem.getQuantity(), "Concurrent orders should result in correct final stock");

        // 2. 예외 발생 여부 확인
        // Redisson 락을 먼저 획득한 스레드는 성공, 나중에 획득한 스레드는 재고 부족하여 예외
        // 혹은 락 획득 과정에서 실패하여 예외가 발생할 수 있다.
        long successCount = itemRepository.findById(sharedItem.getId()).get().getQuantity() == initialStock -1 ? 1: 0;
        long failureCount = exceptions.size(); // 예외 발생 횟수

        System.out.println("initialStock = " + initialStock);
        System.out.println("final Stock = " + finalItem.getQuantity());
        System.out.println("failureCount = " + failureCount);
        exceptions.forEach(e -> System.err.println("Caught exception: " + e.getMessage()));

        // 3. 성공한 주문 하나만 OrderItem이 생성
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertEquals(1, orderItems.size());

        OrderItem successfulOrderItem = orderItems.get(0);
        assertTrue(successfulOrderItem.getOrder().getId().equals(orderA.getId()) || successfulOrderItem.getOrder().getId().equals(orderB.getId()), "OrderItem should belong to either orderA or orderB");
        assertEquals(sharedItem.getId(), successfulOrderItem.getItem().getId(), "OrderItem should be for the shared item");
        assertEquals(1, successfulOrderItem.getCount(), "OrderItem count should be 1");

        // 4. ItemCart 삭제 확인
        List<ItemCart> remainingCarts = itemCartRepository.findAll();
        assertEquals(1, remainingCarts.size(), "Exactly one ItemCart should remain (the one from the failed order).");
        ItemCart remainingCart = remainingCarts.get(0);
        if (successfulOrderItem.getOrder().getId().equals(orderA.getId())) {
            // UserA 성공, userB 실패
            assertEquals(cartB.getId(), remainingCart.getId(), "Remaining ItemCart should belong to userB's cart");
        } else{
            // userB성공, userA 실패
            assertEquals(cartA.getId(), remainingCart.getId(), "Remaining ItemCart should belong to userA's cart");
        }
        assertEquals(sharedItem.getId(), remainingCart.getItem().getId(), "Remaining ItemCart should be for the shared item");
        assertEquals(1, remainingCart.getQuantity(), "Remaining ItemCart quantity should be 1");
    }
}
