package com.moongcheap_backend.groupbuy.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.groupbuy.domain.GroupBuyStatus;
import com.moongcheap_backend.groupbuy.infrastructure.GroupBuyRepository;
import com.moongcheap_backend.groupbuy.presentation.dto.GroupBuyListResponse;
import com.moongcheap_backend.product.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class GroupBuyServiceUnitTest {

    @Mock
    private GroupBuyRepository groupBuyRepository;

    @InjectMocks
    private GroupBuyService groupBuyService;

    @Nested
    @DisplayName("공동구매 목록 조회")
    class GetAllTest {

        @Test
        void 열린_공동구매_목록을_페이지로_반환한다() {
            Pageable pageable = PageRequest.of(0, 20);
            Product product = org.mockito.Mockito.mock(Product.class);
            GroupBuy groupBuy = org.mockito.Mockito.mock(GroupBuy.class);
            when(groupBuy.getTitle()).thenReturn("사과 공동구매");
            when(groupBuy.getProduct()).thenReturn(product);
            when(product.getThumbnailUrl()).thenReturn("https://example.com/apple.jpg");
            when(product.getUnitPrice()).thenReturn(10_000);
            when(groupBuyRepository.findAllByStatus(GroupBuyStatus.OPEN, pageable))
                .thenReturn(new PageImpl<>(java.util.List.of(groupBuy), pageable, 1));

            Page<GroupBuyListResponse> result = groupBuyService.getAll(pageable);

            GroupBuyListResponse response = result.getContent().getFirst();
            assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals("사과 공동구매", response.title()),
                () -> assertEquals("https://example.com/apple.jpg", response.imageUrl()),
                () -> assertEquals(10_000, response.price())
            );
            verify(groupBuyRepository).findAllByStatus(GroupBuyStatus.OPEN, pageable);
        }
    }
}
