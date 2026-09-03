package com.moongcheap_backend.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.moongcheap_backend.common.crypto.EncryptionService;
import com.moongcheap_backend.demand.application.demand.OrderDemandService;
import com.moongcheap_backend.demand.domain.demand.Demand;
import com.moongcheap_backend.groupbuy.application.GroupBuyPublicService;
import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.member.application.OrderMemberInfoService;
import com.moongcheap_backend.member.domain.Seller;
import com.moongcheap_backend.order.domain.OrderStatus;
import com.moongcheap_backend.order.domain.Orders;
import com.moongcheap_backend.order.infrastructure.OrdersRepository;
import com.moongcheap_backend.order.presentation.OrderController.OrderListTab;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse;
import com.moongcheap_backend.order.presentation.dto.OrderListResponse;
import com.moongcheap_backend.order.presentation.dto.OrderShippingAddressRequest;
import com.moongcheap_backend.payments.application.PaymentPublicService;
import com.moongcheap_backend.payments.domain.BrandPayMethod;
import com.moongcheap_backend.payments.presentation.dto.OrderPaymentInfo;
import com.moongcheap_backend.product.domain.product.Product;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    private static final Long MEMBER_ID = 1L;
    private static final String ORDER_NO = "ORD-TEST";

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderMemberInfoService orderMemberInfoService;
    @Mock
    private PaymentPublicService orderPaymentInfoService;
    @Mock
    private EncryptionService encryptionService;
    @Mock
    private GroupBuyPublicService groupBuyPublicService;
    @Mock
    private OrderDemandService orderDemandService;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<List<Orders>> ordersCaptor;

    @Nested
    @DisplayName("자동 주문 정상 테스트")
    class AutoCreateOrderTest {

        @Test
        void 수요_20건을_주문으로_변환해_한번에_저장한다() {
            GroupBuy groupBuy = org.mockito.Mockito.mock(GroupBuy.class);
            Seller seller = org.mockito.Mockito.mock(Seller.class);
            Product product = org.mockito.Mockito.mock(Product.class);
            List<Demand> demands = IntStream.rangeClosed(1, 20)
                .mapToObj(index -> {
                    Demand demand = org.mockito.Mockito.mock(Demand.class);
                    when(demand.getMemberId()).thenReturn((long) index);
                    when(demand.getQuantity()).thenReturn(index);
                    return demand;
                })
                .toList();

            when(groupBuyPublicService.getOrderSource(10L)).thenReturn(groupBuy);
            when(groupBuy.getSeller()).thenReturn(seller);
            when(groupBuy.getProduct()).thenReturn(product);
            when(groupBuy.getTitle()).thenReturn("제주 감귤 공동구매");
            when(seller.isSellable()).thenReturn(true);
            when(seller.getId()).thenReturn(20L);
            when(seller.getBusinessName()).thenReturn("문치프 농장");
            when(product.isAwarded()).thenReturn(true);
            when(product.getSellerId()).thenReturn(20L);
            when(product.getThumbnailUrl()).thenReturn("https://example.com/image.jpg");
            when(product.getDemandBoardId()).thenReturn(30L);
            when(product.getId()).thenReturn(40L);
            when(product.getUnitPrice()).thenReturn(10_000);
            when(product.getShippingFee()).thenReturn(3_000);
            when(orderDemandService.getPaymentPendingForOrder(30L)).thenReturn(demands);

            orderService.autoCreateOrder(10L);

            verify(ordersRepository).saveAll(ordersCaptor.capture());
            List<Orders> savedOrders = ordersCaptor.getValue();
            assertThat(savedOrders).hasSize(20);
            assertThat(savedOrders)
                .extracting(Orders::getMemberId)
                .containsExactlyElementsOf(IntStream.rangeClosed(1, 20)
                    .mapToObj(Long::valueOf)
                    .toList());
            assertThat(savedOrders)
                .extracting(Orders::getSum)
                .containsExactlyElementsOf(IntStream.rangeClosed(1, 20).boxed().toList());
            assertThat(savedOrders)
                .extracting(Orders::getTotalAmount)
                .containsExactlyElementsOf(IntStream.rangeClosed(1, 20)
                    .map(quantity -> 10_000 * quantity + 3_000)
                    .boxed()
                    .toList());
            assertThat(savedOrders)
                .extracting(Orders::getOrderNo)
                .allMatch(orderNo -> orderNo.startsWith("ORD-"))
                .doesNotHaveDuplicates();
        }

        @Test
        void 결제수단이_있는_수요와_없는_수요의_주문을_생성한다() {
            GroupBuy groupBuy = org.mockito.Mockito.mock(GroupBuy.class);
            Seller seller = org.mockito.Mockito.mock(Seller.class);
            Product product = org.mockito.Mockito.mock(Product.class);
            Demand demandWithPayMethod = org.mockito.Mockito.mock(Demand.class);
            Demand demandWithoutPayMethod = org.mockito.Mockito.mock(Demand.class);
            BrandPayMethod payMethod = org.mockito.Mockito.mock(BrandPayMethod.class);

            when(groupBuyPublicService.getOrderSource(10L)).thenReturn(groupBuy);
            when(groupBuy.getSeller()).thenReturn(seller);
            when(groupBuy.getProduct()).thenReturn(product);
            when(groupBuy.getTitle()).thenReturn("제주 감귤 공동구매");
            when(seller.isSellable()).thenReturn(true);
            when(seller.getId()).thenReturn(20L);
            when(seller.getBusinessName()).thenReturn("문치프 농장");
            when(product.isAwarded()).thenReturn(true);
            when(product.getSellerId()).thenReturn(20L);
            when(product.getThumbnailUrl()).thenReturn("https://example.com/image.jpg");
            when(product.getDemandBoardId()).thenReturn(30L);
            when(product.getId()).thenReturn(40L);
            when(product.getUnitPrice()).thenReturn(10_000);
            when(product.getShippingFee()).thenReturn(3_000);
            when(orderDemandService.getPaymentPendingForOrder(30L))
                .thenReturn(List.of(demandWithPayMethod, demandWithoutPayMethod));
            when(demandWithPayMethod.getMemberId()).thenReturn(1L);
            when(demandWithPayMethod.getPayMethodId()).thenReturn(100L);
            when(demandWithPayMethod.getQuantity()).thenReturn(2);
            when(demandWithoutPayMethod.getMemberId()).thenReturn(2L);
            when(demandWithoutPayMethod.getPayMethodId()).thenReturn(null);
            when(demandWithoutPayMethod.getQuantity()).thenReturn(1);
            when(entityManager.getReference(BrandPayMethod.class, 100L)).thenReturn(payMethod);

            Void result = orderService.autoCreateOrder(10L);

            assertThat(result).isNull();
            verify(ordersRepository).saveAll(ordersCaptor.capture());
            List<Orders> savedOrders = ordersCaptor.getValue();
            assertThat(savedOrders).hasSize(2);
            assertThat(savedOrders.get(0).getOrderNo()).startsWith("ORD-");
            assertThat(savedOrders.get(0).getMemberId()).isEqualTo(1L);
            assertThat(savedOrders.get(0).getBrandPayMethod()).isSameAs(payMethod);
            assertThat(savedOrders.get(0).getTotalAmount()).isEqualTo(23_000);
            assertThat(savedOrders.get(1).getMemberId()).isEqualTo(2L);
            assertThat(savedOrders.get(1).getBrandPayMethod()).isNull();
            assertThat(savedOrders.get(1).getTotalAmount()).isEqualTo(13_000);
        }
    }

    @Nested
    @DisplayName("주문 목록 조회 정상 테스트")
    class ViewOrderListTest {

        private final Pageable pageable = PageRequest.of(0, 20);
        private Orders order;

        @BeforeEach
        void setUp() {
            order = org.mockito.Mockito.mock(Orders.class);
            when(order.getCreatedAt()).thenReturn(LocalDateTime.of(2026, 9, 1, 12, 0));
            when(order.getOrderNo()).thenReturn(ORDER_NO);
            when(order.getBusinessName()).thenReturn("문치프 농장");
            when(order.getOrderStatus()).thenReturn(OrderStatus.PAYMENT_COMPLETED);
            when(order.getImageUrl()).thenReturn("image.jpg");
            when(order.getProductName()).thenReturn("제주 감귤");
            when(order.getSum()).thenReturn(2);
            when(order.getTotalAmount()).thenReturn(23_000);
        }

        @Test
        void 전체_탭은_회원의_모든_주문을_조회한다() {
            when(ordersRepository.findAllByMemberId(MEMBER_ID, pageable))
                .thenReturn(new PageImpl<>(List.of(order)));

            Page<OrderListResponse> result =
                orderService.viewOrderList(MEMBER_ID, OrderListTab.ALL, pageable);

            assertOrderListResponse(result.getContent().getFirst());
            verify(orderMemberInfoService).validateActiveMember(MEMBER_ID);
        }

        @Test
        void 진행중_탭은_진행중인_네가지_상태를_조회한다() {
            Set<OrderStatus> statuses = Set.of(
                OrderStatus.PAYMENT_PENDING,
                OrderStatus.PAYMENT_COMPLETED,
                OrderStatus.PREPARING_SHIPMENT,
                OrderStatus.SHIPPED
            );
            when(ordersRepository.findAllByMemberIdAndOrderStatusIn(MEMBER_ID, statuses, pageable))
                .thenReturn(new PageImpl<>(List.of(order)));

            Page<OrderListResponse> result =
                orderService.viewOrderList(MEMBER_ID, OrderListTab.IN_PROGRESS, pageable);

            assertOrderListResponse(result.getContent().getFirst());
        }

        @Test
        void 배송완료_탭은_배송완료_상태를_조회한다() {
            Set<OrderStatus> statuses = Set.of(OrderStatus.DELIVERED);
            when(ordersRepository.findAllByMemberIdAndOrderStatusIn(MEMBER_ID, statuses, pageable))
                .thenReturn(new PageImpl<>(List.of(order)));

            Page<OrderListResponse> result =
                orderService.viewOrderList(MEMBER_ID, OrderListTab.DELIVERED, pageable);

            assertOrderListResponse(result.getContent().getFirst());
        }

        @Test
        void 구매확정_탭은_구매확정_상태를_조회한다() {
            Set<OrderStatus> statuses = Set.of(OrderStatus.COMPLEDED);
            when(ordersRepository.findAllByMemberIdAndOrderStatusIn(MEMBER_ID, statuses, pageable))
                .thenReturn(new PageImpl<>(List.of(order)));

            Page<OrderListResponse> result =
                orderService.viewOrderList(MEMBER_ID, OrderListTab.COMPLETED, pageable);

            assertOrderListResponse(result.getContent().getFirst());
        }

        private void assertOrderListResponse(OrderListResponse response) {
            assertThat(response.orderDate()).isEqualTo("2026-09-01");
            assertThat(response.orderNo()).isEqualTo(ORDER_NO);
            assertThat(response.businessName()).isEqualTo("문치프 농장");
            assertThat(response.orderStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
            assertThat(response.imageUrl()).isEqualTo("image.jpg");
            assertThat(response.productName()).isEqualTo("제주 감귤");
            assertThat(response.quantity()).isEqualTo(2);
            assertThat(response.totalAmount()).isEqualTo(23_000);
        }
    }

    @Nested
    @DisplayName("주문 상세 조회 정상 테스트")
    class ViewOrderDetailTest {

        private Orders order;

        @BeforeEach
        void setUp() {
            order = org.mockito.Mockito.mock(Orders.class);
            GroupBuy groupBuy = org.mockito.Mockito.mock(GroupBuy.class);
            when(ordersRepository.findDetailByOrderNoAndMemberId(ORDER_NO, MEMBER_ID))
                .thenReturn(Optional.of(order));
            when(order.getCreatedAt()).thenReturn(LocalDateTime.of(2026, 9, 1, 12, 0));
            when(order.getGroupBuy()).thenReturn(groupBuy);
            when(groupBuy.getId()).thenReturn(10L);
            when(groupBuy.getTitle()).thenReturn("제주 감귤 공동구매");
            when(order.getBusinessName()).thenReturn("문치프 농장");
            when(order.getDeliveryFee()).thenReturn(3_000);
            when(order.getOrderStatus()).thenReturn(OrderStatus.DELIVERED);
            when(order.getImageUrl()).thenReturn("image.jpg");
            when(order.getProductName()).thenReturn("제주 감귤");
            when(order.getSum()).thenReturn(2);
            when(order.getPrice()).thenReturn(10_000);
            when(order.getShippingName()).thenReturn("홍길동");
            when(order.getPhoneNumber()).thenReturn("encrypted-phone");
            when(encryptionService.decrypt("encrypted-phone")).thenReturn("01012345678");
            when(encryptionService.maskPhoneNumber("01012345678")).thenReturn("010-****-5678");
            when(orderPaymentInfoService.getForOrder(order))
                .thenReturn(new OrderPaymentInfo(20_000, 3_000, 23_000, "신한카드"));
        }

        @Test
        void 기본주소와_상세주소가_있으면_공백으로_합친다() {
            when(order.getAddress()).thenReturn("서울시 강남구");
            when(order.getAddressDetail()).thenReturn("101호");

            OrderDetailResponse result = orderService.viewOrderDetail(MEMBER_ID, ORDER_NO);

            assertThat(result.orderDate()).isEqualTo("2026-09-01");
            assertThat(result.orderNo()).isEqualTo(ORDER_NO);
            assertThat(result.product().groupBuy().groupBuyId()).isEqualTo(10L);
            assertThat(result.shipping().phoneNumberMasked()).isEqualTo("010-****-5678");
            assertThat(result.shipping().addressMasked()).isEqualTo("서울시 강남구 101호");
            assertThat(result.payment().totalPaymentAmount()).isEqualTo(23_000);
        }

        @Test
        void 기본주소가_비어있으면_상세주소만_반환한다() {
            when(order.getAddress()).thenReturn(" ");
            when(order.getAddressDetail()).thenReturn("101호");

            OrderDetailResponse result = orderService.viewOrderDetail(MEMBER_ID, ORDER_NO);

            assertThat(result.shipping().addressMasked()).isEqualTo("101호");
        }

        @Test
        void 상세주소가_비어있으면_기본주소만_반환한다() {
            when(order.getAddress()).thenReturn("서울시 강남구");
            when(order.getAddressDetail()).thenReturn(" ");

            OrderDetailResponse result = orderService.viewOrderDetail(MEMBER_ID, ORDER_NO);

            assertThat(result.shipping().addressMasked()).isEqualTo("서울시 강남구");
        }

        @Test
        void 두_주소가_null이면_null을_반환한다() {
            when(order.getAddress()).thenReturn(null);
            when(order.getAddressDetail()).thenReturn(null);

            OrderDetailResponse result = orderService.viewOrderDetail(MEMBER_ID, ORDER_NO);

            assertThat(result.shipping().addressMasked()).isNull();
        }
    }

    @Nested
    @DisplayName("주문 취소 정상 테스트")
    class OrderCancelTest {

        @Test
        void 결제대기_주문을_취소한다() {
            Orders order = org.mockito.Mockito.mock(Orders.class);
            when(ordersRepository.findByOrderNoAndMemberId(ORDER_NO, MEMBER_ID))
                .thenReturn(Optional.of(order));
            when(order.getOrderStatus()).thenReturn(OrderStatus.PAYMENT_PENDING);

            orderService.orderCancel(MEMBER_ID, ORDER_NO);

            verify(order).setOrderStatus(OrderStatus.CANCELED);
        }

        @Test
        void 이미_취소된_주문은_멱등하게_처리한다() {
            Orders order = org.mockito.Mockito.mock(Orders.class);
            when(ordersRepository.findByOrderNoAndMemberId(ORDER_NO, MEMBER_ID))
                .thenReturn(Optional.of(order));
            when(order.getOrderStatus()).thenReturn(OrderStatus.CANCELED);

            orderService.orderCancel(MEMBER_ID, ORDER_NO);

            verify(order, never()).setOrderStatus(any());
        }
    }

    @Nested
    @DisplayName("배송지 입력 정상 테스트")
    class UpdateShippingAddressTest {

        @Test
        void 전화번호에서_구분자를_제거하고_암호화해_배송지를_저장한다() {
            Orders order = org.mockito.Mockito.mock(Orders.class);
            OrderShippingAddressRequest request = new OrderShippingAddressRequest(
                "홍길동", "010-1234-5678", "06234", "서울시 강남구", "101호", "문 앞"
            );
            when(ordersRepository.findByOrderNoAndMemberId(ORDER_NO, MEMBER_ID))
                .thenReturn(Optional.of(order));
            when(order.getOrderStatus()).thenReturn(OrderStatus.PAYMENT_COMPLETED);
            when(encryptionService.encrypt("01012345678")).thenReturn("encrypted-phone");

            Void result = orderService.updateShippingAddress(MEMBER_ID, ORDER_NO, request);

            assertThat(result).isNull();
            verify(order).updateShipping(
                "홍길동", "encrypted-phone", "06234", "서울시 강남구", "101호", "문 앞"
            );
        }
    }
}
