package com.yuseogi.storeservice.unit.controller;

import com.yuseogi.storeservice.controller.StoreController;
import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
public class StoreControllerUnitTest extends ControllerUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private StoreService storeService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * Store(상점) 및 TradeDevice(주문용 태블릿 기기) 엔티티 데이터 생성 성공
     */
    @Test
    void 상점_및_주문용_태블릿_기기_엔티티_데이터_생성_성공() throws Exception {
        // given
        CreateStoreRequestDto request = mock(CreateStoreRequestDto.class);

        // when
        ResultActions resultActions = requestCreateStore(request);

        // then
        resultActions.andExpect(status().isOk());
        verify(storeService, times(1)).createStore(any(CreateStoreRequestDto.class));
    }

    private ResultActions requestCreateStore(CreateStoreRequestDto request) throws Exception {
        return mvc.perform(post("/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print());

    }
}
