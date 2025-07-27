package mb.projects.ht.service;

import lombok.RequiredArgsConstructor;
import mb.projects.ht.entities.*;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.enums.TimeFrameEnum;
import mb.projects.ht.exception.NotFoundException;
import mb.projects.ht.model.Cart;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import mb.projects.ht.model.mapper.CartMapper;
import mb.projects.ht.model.mapper.ItemMapper;
import mb.projects.ht.repository.*;
import mb.projects.ht.request.AddItem;
import mb.projects.ht.request.BuyItemsInCartRequest;
import mb.projects.ht.request.DeleteItemFromCartRequest;
import mb.projects.ht.response.GetCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemMapper itemMapper;

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartContentRepository cartContentRepository;
    private final PriceRepository priceRepository;
    private final RecurringPriceRepository recurringPriceRepository;

    public CartDAO getCartWithContents(String userId) {
        return cartRepository.findByUserIdWithContents(userId)
                .orElseGet(() -> {
                    // Create new cart if not found
                    CartDAO newCart = new CartDAO();
                    newCart.setUserId(userId);
                    newCart.setDateCreated(LocalDate.now());
                    return cartRepository.save(newCart);
                });
    }

    public GetCartResponse getCartByUserId(String userId) {
        CartDAO cartDAO = cartRepository.getCartByUserId(userId);
        GetCartResponse cart = new GetCartResponse(cartMapper.toCart(cartDAO));
        return cart;
    }

    public List<Cart> findCartsByItemSourceIdAndActionId(String sourceId, Integer actionId) {
        List<CartDAO> cartDAOs = cartRepository.findCartsByItemSourceIdAndActionId(sourceId, actionId);
        return cartDAOs.stream()
                .map(cartMapper::toCart)
                .collect(Collectors.toList());
    }

    public int getCartsByCriteria(String sourceId, Integer actionId,
                                            LocalDate dateFrom, LocalDate dateTo) {
        System.out.println("Querying between: " + dateFrom + " and " + dateTo);

        List<CartDAO> carts = cartRepository.findCartsByItemAndActionInPeriod(
                sourceId, actionId, dateFrom, dateTo);

        return carts.stream()
                .flatMap(cart -> cart.getContents().stream())
                .filter(content -> content.getItem() != null && sourceId.equals(content.getItem().getSourceId()))
                .filter(content -> content.getQuantity() != null && content.getActionId().equals(actionId))
                .mapToInt(CartContentDAO::getQuantity)
                .sum();
    }


    public GetCartResponse buyCart(BuyItemsInCartRequest buyItemsInCartRequest) {
        return null;
    }

    @Transactional
    public GetCartResponse deleteItemFromCart(DeleteItemFromCartRequest request) {
        // Find the cart by either userId or transactionId (exact match)
        CartDAO cart = cartRepository.findByUserIdOrTransactionId(
                        request.getUserId(),
                        request.getTransactionId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        //System.out.println("Found cart: " + cart);
        // Verify the item exists in the cart
        boolean itemExists = cart.getContents().stream()
                .anyMatch(content -> content.getItem().getSourceId().equals(request.getSourceId()));

        if (!itemExists) {
            throw new NotFoundException("Item with sourceId " + request.getSourceId() + " not found in cart");
        }

        // Delete the item with matching sourceId from the cart
        cartRepository.deleteByCartIdAndSourceId(cart.getId(), request.getSourceId());

        // Update cart modification timestamp
        cart.setDateModified(LocalDate.now());
        cartRepository.save(cart);

        CartDAO newCart = cartRepository.findByUserIdOrTransactionId(
                        request.getUserId(),
                        request.getTransactionId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        return new GetCartResponse(cartMapper.toCart(newCart));
    }

    public GetCartResponse addItem(AddItem item) {

        Optional<CartDAO> cartDAO = cartRepository.findByUserIdOrTransactionId(
                item.getUserId(),
                item.getTransactionId());

        ItemDAO itemDAO = getItemBySourceIdAndPrice(item);
        if(cartDAO.isPresent()) {
            createItemAndAddToCart(cartDAO.get().getId(), item, itemDAO,  ActionEnum.ADD.getId());

        } else {
            createNewCart(item, itemDAO);
        }

        Optional<CartDAO> newCartDAO = cartRepository.findByUserIdOrTransactionId(
                item.getUserId(),
                item.getTransactionId());

        if(newCartDAO.isEmpty()) {
            System.out.println("Couldn't find cart after inserting");
            throw new NotFoundException("Couldn't insert Item");
        }

        return new GetCartResponse(cartMapper.toCart(newCartDAO.get()));

    }

    private void createItemAndAddToCart(Long cartId, AddItem item, ItemDAO itemDAO, Integer actionId) {
        CartDAO cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));

        //todo check if existing item has the same price type and amount
        // Step 2: Load the item

        // Step 3: Check if item already exists in this cart
        Optional<CartContentDAO> existingContentOpt = cartContentRepository.findByCartAndItem(cart, itemDAO);

        if (existingContentOpt.isPresent()) {
            // Item already in cart — increase quantity
            CartContentDAO existingContent = existingContentOpt.get();
            int currentQty = existingContent.getQuantity() != null ? existingContent.getQuantity() : 0;
            existingContent.setQuantity(currentQty + (item.getQuantity() != null ? item.getQuantity() : 1));
            cartContentRepository.save(existingContent);
        } else {
            // Item not in cart — create new CartContent entry
            CartContentDAO newContent = new CartContentDAO();
            newContent.setCart(cart);
            newContent.setItem(itemDAO);
            newContent.setActionId(actionId);
            newContent.setQuantity(item.getQuantity() != null ? item.getQuantity() : 1);
            cartContentRepository.save(newContent);
        }
    }

    private ItemDAO getItemBySourceIdAndPrice(AddItem item) {

        List<ItemDAO> itemDAOS = itemRepository.findItemWithMatchingPrice(item.getSourceId(), item.getPrice().getOneTimePrice(),
                item.getPrice().getRecurringPrices().get(0).getAmount(), item.getPrice().getRecurringPrices().get(0).getDuration(),
                Long.valueOf(item.getPrice().getRecurringPrices().get(0).getTimeFrame().getId()));

        if(!itemDAOS.isEmpty()) {
            return itemDAOS.get(0);
        }


        ItemDAO itemDAONew = new ItemDAO();
        itemDAONew.setSourceId(item.getSourceId());
        itemDAONew.setName(item.getName());
        itemDAONew.setDescription(item.getDescription());
        itemDAONew.setIsActive(true);
        ItemDAO itemDAO = itemRepository.save(itemDAONew);

        boolean isRecurringPrice = item.getPrice().getOneTimePrice()==null;

        PriceDAO priceDAO = new PriceDAO();
        priceDAO.setItem(itemDAO);
        if(isRecurringPrice) {
            RecurringPrice recurringPriceItem = item.getPrice().getRecurringPrices().get(0);
            RecurringPrice recurringPrice = new RecurringPrice();
            recurringPrice.setAmount(recurringPriceItem.getAmount());
            recurringPrice.setDuration(recurringPriceItem.getDuration());
            recurringPrice.setTimeFrame(recurringPriceItem.getTimeFrame());
            List<RecurringPrice> recurringPrices = new ArrayList<>();
            recurringPrices.add(recurringPrice);
        } else {
            priceDAO.setOneTimePrice(BigDecimal.valueOf(item.getPrice().getOneTimePrice()));
        }
        priceDAO = priceRepository.save(priceDAO);

        if (isRecurringPrice) {
            RecurringPrice recurringPriceItem = item.getPrice().getRecurringPrices().get(0);
            RecurringPriceDAO recurringPriceDAO = new RecurringPriceDAO();
            recurringPriceDAO.setPrice(priceDAO);
            recurringPriceDAO.setTimeFrameId(recurringPriceItem.getTimeFrame().getId());
            recurringPriceDAO.setDurationUnits(Math.toIntExact(recurringPriceItem.getDuration()));
            recurringPriceDAO.setAmount(BigDecimal.valueOf(recurringPriceItem.getAmount()));
            recurringPriceRepository.save(recurringPriceDAO);

        }

        return itemDAO;

    }

    @Transactional
    private void addItemToExistingCart(Long cartId, String sourceId, Integer actionId, Integer quantity) {
        // Step 1: Load the cart
        CartDAO cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));

        //todo check if existing item has the same price type and amount
        // Step 2: Load the item
        ItemDAO item = itemRepository.findBySourceId(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with source ID: " + sourceId));

        // Step 3: Check if item already exists in this cart
        Optional<CartContentDAO> existingContentOpt = cartContentRepository.findByCartAndItem(cart, item);

        if (existingContentOpt.isPresent()) {
            // Item already in cart — increase quantity
            CartContentDAO existingContent = existingContentOpt.get();
            int currentQty = existingContent.getQuantity() != null ? existingContent.getQuantity() : 0;
            existingContent.setQuantity(currentQty + (quantity != null ? quantity : 1));
            cartContentRepository.save(existingContent);
        } else {
            // Item not in cart — create new CartContent entry
            CartContentDAO newContent = new CartContentDAO();
            newContent.setCart(cart);
            newContent.setItem(item);
            newContent.setActionId(actionId);
            newContent.setQuantity(quantity != null ? quantity : 1);
            cartContentRepository.save(newContent);
        }
    }

    private void createNewCart(AddItem item, ItemDAO itemDAO) {

        CartDAO cart = new CartDAO();
        cart.setUserId(item.getUserId());
        cart.setDateCreated(LocalDate.now());
        if(item.getTransactionId()==null || item.getTransactionId().isEmpty()) {
            cart.setTransactionId(UUID.randomUUID().toString());
        } else {
            cart.setTransactionId(item.getTransactionId());
        }
        cart = cartRepository.save(cart);

        CartContentDAO cartContent = new CartContentDAO();
        cartContent.setCart(cart);
        cartContent.setItem(itemDAO);
        cartContent.setActionId(ActionEnum.ADD.getId()); // Example: 1 = add
        cartContent.setQuantity(item.getQuantity().intValue());

        cartContent = cartContentRepository.save(cartContent);

        List<CartContentDAO> cartContentDAOList = new ArrayList<>();
        cartContentDAOList.add(cartContent);
        cart.setContents(cartContentDAOList);
        cartRepository.save(cart);

    }

}