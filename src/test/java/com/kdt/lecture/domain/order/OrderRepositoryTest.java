package com.kdt.lecture.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class OrderRepositoryTest {

    String uuid = UUID.randomUUID().toString();

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        Member member = new Member();
        member.setName("김대휘");
        member.setAge(20);
        member.setNickName("maroo");
        member.setAddress("수원시 영통구");
        member.setDescription("");

        Order order = new Order();
        order.setUuid(uuid);
        order.setMemo("부재 시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPENED);

        order.setMember(member);

        orderRepository.save(order); // INSERT
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    @Transactional
    void JPA_query() {
        Order order = orderRepository.findById(uuid).get(); // SELECT * FROM orders WHERE id = ?
        List<Order> all = orderRepository.findAll(); // SELECT * FROM orders
        orderRepository.existsById(uuid);
    }

    @Test
    @Transactional
    void METHOD_QUERY() {
        orderRepository.findAllByOrderStatus(OrderStatus.OPENED);
        orderRepository.findAllByOrderStatusOrderByOrderDatetime(OrderStatus.OPENED);
    }


    @Test
    void NAMED_QUERY() {
        Optional<Order> order = orderRepository.findByMemo("부재 시");

        Order entity = order.get();
        log.info("{}", entity.getMemo());
    }


}
