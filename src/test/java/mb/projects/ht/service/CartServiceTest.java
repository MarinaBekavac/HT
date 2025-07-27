package mb.projects.ht.service;

import mb.projects.ht.entities.*;
import mb.projects.ht.entities.finished.FinishedCartDAO;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.exception.NotFoundException;
import mb.projects.ht.model.Cart;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import mb.projects.ht.model.mapper.CartMapper;
import mb.projects.ht.repository.*;
import mb.projects.ht.request.AddItem;
import mb.projects.ht.request.BuyItemsInCartRequest;
import mb.projects.ht.request.DeleteItemFromCartRequest;
import mb.projects.ht.response.GetCartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartContentRepository cartContentRepository;

    @Mock
    private FinishedCartRepository finishedCartRepository;

    @Mock
    private ItemPriceRepository itemPriceRepository;

    @Mock
    private CartMapper cartMapper;

    private AddItem testItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample item setup
        RecurringPrice recurringPrice = new RecurringPrice();
        recurringPrice.setAmount(BigDecimal.valueOf(9.99));
        recurringPrice.setDuration(12L);
        recurringPrice.setTimeFrame(TimeFrameEnum.MONTH);

        Price price = new Price();
        price.setRecurringPrice(recurringPrice);

        testItem = new AddItem();
        testItem.setUserId("user-123");
        testItem.setName("Test Product");
        testItem.setSourceId("src-123");
        testItem.setDescription("Test Description");
        testItem.setPrice(price);
        testItem.setQuantity(1);
    }

    @Test
    void testGetCartByUserId_NotFound() {
        when(cartRepository.getCartByUserId("user-123")).thenReturn(Optional.empty());
        GetCartResponse response = cartService.getCartByUserId("user-123");
        assertNotNull(response);
    }

    @Test
    void testBuyCart_MigratesAndDeletesCart() {
        CartDAO cart = new CartDAO();
        cart.setId(1L);
        cart.setUserId("user-123");
        cart.setDateCreated(LocalDate.now());
        cart.setContents(new ArrayList<>());
        when(cartRepository.findByUserIdOrTransactionId("user-123", null)).thenReturn(Optional.of(cart));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        BuyItemsInCartRequest request = new BuyItemsInCartRequest();
        request.setUserId("user-123");

        mb.projects.ht.model.Cart mappedCart = new mb.projects.ht.model.Cart();
        when(cartMapper.toCart(any())).thenReturn(mappedCart);

        GetCartResponse response = cartService.buyCart(request);

        assertNotNull(response);
    }

    @Test
    void testDeleteItem_ThrowsIfCartMissing() {
        when(cartRepository.findByUserIdOrTransactionId(any(), any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.deleteItemFromCart(
                new DeleteItemFromCartRequest("user-123", null, "src-123")
        ));
    }
}
