package com.moongcheap_backend.groupbuy.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.groupbuy.infrastructure.GroupBuyRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupBuyServiceUnitExceptionTest {

    @Mock
    private GroupBuyRepository groupBuyRepository;

    @InjectMocks
    private GroupBuyService groupBuyService;

    @Nested
    @DisplayName("공동구매 상세 조회 예외")
    class GetByIdExceptionTest {

        @Test
        void 공동구매가_없으면_GROUPBUY_NOT_FOUND를_던진다() {
            Long groupBuyId = 10L;
            when(groupBuyRepository.findDetailById(groupBuyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> groupBuyService.getById(groupBuyId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.GROUPBUY_NOT_FOUND);
        }
    }
}
