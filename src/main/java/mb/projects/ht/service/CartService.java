package mb.projects.ht.service;

import lombok.RequiredArgsConstructor;
import mb.projects.ht.entities.*;
import mb.projects.ht.entities.finished.FinishedCartContentDAO;
import mb.projects.ht.entities.finished.FinishedCartDAO;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.exception.NotFoundException;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import mb.projects.ht.model.mapper.CartMapper;
import mb.projects.ht.repository.*;
import mb.projects.ht.request.AddItem;
import mb.projects.ht.request.BuyItemsInCartRequest;
import mb.projects.ht.request.DeleteItemFromCartRequest;
import mb.projects.ht.request.EvictCartRequest;
import mb.projects.ht.response.GetCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    private final ItemPriceRepository itemPriceRepository;

    private final CartRepository cartRepository;
    private final CartContentRepository cartContentRepository;
    private final FinishedCartRepository finishedCartRepository;

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
        Optional<CartDAO> cartDAO = cartRepository.getCartByUserId(userId);
        if (!cartDAO.isPresent()) {
            return new GetCartResponse();
        }
        GetCartResponse cart = new GetCartResponse(cartMapper.toCart(cartDAO.get()));
        return cart;
    }

    public int getCartsByCriteria(String sourceId, Integer actionId,
                                            LocalDate dateFrom, LocalDate dateTo) {
        System.out.println("Querying between: " + dateFrom + " and " + dateTo);

        //search finished transactions
        List<FinishedCartDAO> carts = finishedCartRepository.findFinishedCartsByItemAndActionInPeriod(
                sourceId, actionId, dateFrom, dateTo);

        return carts.stream()
                .flatMap(cart -> cart.getContents().stream())
                .filter(content -> content.getItemPrice() != null && sourceId.equals(content.getItemPrice().getSourceId()))
                .filter(content -> content.getQuantity() != null && content.getActionId().equals(actionId))
                .mapToInt(FinishedCartContentDAO::getQuantity)
                .sum();
    }


    public GetCartResponse evictCart(EvictCartRequest evictCartRequest) {
        Optional<CartDAO> cartDAO = cartRepository.findByUserIdOrTransactionId(evictCartRequest.getUserId(), evictCartRequest.getTransactionId());
        if (!cartDAO.isPresent()) {
            //todo maybe return null?
            return new GetCartResponse();
        }
//        List<CartDAO> list = cartRepository.findByUserIdOrTransactionId(evictCartRequest.getUserId(), evictCartRequest.getTransactionId());
//        if(list==null ||list.isEmpty()) {
//            return new GetCartResponse();
//        }
//
//        for(CartDAO cartDAO : list) {
//            clearCart(cartDAO.getId());
//        }
        clearCart(cartDAO.get().getId());
        return new GetCartResponse();
    }

    @Transactional
    public void clearCart(Long cartId) {
        Optional<CartDAO> cart = cartRepository.findById(cartId);

        if(!cart.isPresent()) {
            System.out.println("No cart found to delete");
            return;
        }

        // Clear all cart contents
        cart.get().getContents().clear();

        // Save the updated cart
        cartRepository.save(cart.get());

    }



    public GetCartResponse buyCart(BuyItemsInCartRequest buyItemsInCartRequest) {
        Optional<CartDAO> cartDAO = cartRepository.findByUserIdOrTransactionId(buyItemsInCartRequest.getUserId(), buyItemsInCartRequest.getTransactionId());
        if (!cartDAO.isPresent()) {
            //todo maybe return null?
            return new GetCartResponse();
        }
        checkoutCart(cartDAO.get().getId());
        return new GetCartResponse(cartMapper.toCart(cartDAO.get()));
    }

    @Transactional
    private void checkoutCart(Long cartId) {
        CartDAO cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + cartId));

        // Set dateBought to mark it as purchased
        cart.setDateBought(LocalDate.now());

        // Convert to FinishedCartDAO
        FinishedCartDAO finishedCart = new FinishedCartDAO(cart);

        // Convert contents
        List<FinishedCartContentDAO> finishedContents = cart.getContents().stream()
                .map(content -> new FinishedCartContentDAO(content, finishedCart))
                .toList();

        finishedCart.setContents(finishedContents);

        // Save to finished tables
        finishedCartRepository.save(finishedCart);

        // Delete the cart (contents are cascade-removed)
        cartRepository.delete(cart);
    }


    //@Transactional
    public GetCartResponse deleteItemFromCart(DeleteItemFromCartRequest request) {

        deleteFromCart(request);
        CartDAO newCart = cartRepository.findByUserIdOrTransactionId(
                        request.getUserId(),
                        request.getTransactionId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        return new GetCartResponse(cartMapper.toCart(newCart));
    }

    @Transactional
    private void deleteFromCart(DeleteItemFromCartRequest request){
        // Find the cart by either userId or transactionId (exact match)
        CartDAO cart = cartRepository.findByUserIdOrTransactionId(
                        request.getUserId(),
                        request.getTransactionId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        //System.out.println("Found cart: " + cart);
        // Verify the item exists in the cart
        boolean itemExists = cart.getContents().stream()
                .anyMatch(content -> content.getItemPrice().getSourceId().equals(request.getSourceId()));

        if (!itemExists) {
            throw new NotFoundException("Item with sourceId " + request.getSourceId() + " not found in cart");
        }

        // Delete the item with matching sourceId from the cart
        //cartRepository.deleteByCartIdAndSourceId(cart.getId(), request.getSourceId());
        List<ItemPriceDAO> itemPriceDAO = itemPriceRepository.findBySourceId(request.getSourceId());
        for(ItemPriceDAO i : itemPriceDAO) {
            boolean wasAdded = false;
            for(CartContentDAO cartContentDAO : cart.getContents()) {
                if(cartContentDAO.getItemPrice().getId().equals(i.getId()) &&
                        Objects.equals(cartContentDAO.getActionId(), ActionEnum.DELETE.getId())) {
                    cartContentDAO.setQuantity(cartContentDAO.getQuantity()+1);
                    wasAdded = true;
                }
            }
            if(!wasAdded) {
                CartContentDAO cartContentDAO = new CartContentDAO(1, ActionEnum.DELETE.getId(), i, cart);
                cartContentRepository.save(cartContentDAO);
            }
        }
        //CartContentDAO cartContentDAO = new CartContentDAO(1, ActionEnum.DELETE.getId(), itemPriceDAO, cart);
        //cartContentRepository.save(cartContentDAO);

        // Update cart modification timestamp
        cart.setDateModified(LocalDate.now());
        cartRepository.save(cart);
    }

    public GetCartResponse addItem(AddItem item) {

        Optional<CartDAO> cartDAO = cartRepository.findByUserIdOrTransactionId(
                item.getUserId(),
                item.getTransactionId());

        ItemPriceDAO itemDAO = getItemBySourceIdAndPrice(item);
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

    private void createItemAndAddToCart(Long cartId, AddItem item, ItemPriceDAO itemDAO, Integer actionId) {
        CartDAO cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));

        //todo check if existing item has the same price type and amount
        // Step 2: Load the item

        // Step 3: Check if item already exists in this cart
        Optional<CartContentDAO> existingContentOpt = cartContentRepository.findByCartAndItemPrice(cart, itemDAO);

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
            newContent.setItemPrice(itemDAO);
            newContent.setActionId(actionId);
            newContent.setQuantity(item.getQuantity() != null ? item.getQuantity() : 1);
            cartContentRepository.save(newContent);
        }
    }

    private ItemPriceDAO getItemBySourceIdAndPrice(AddItem item) {

        ItemPriceDAO existingItemDAO;
        boolean isRecurringPrice = item.isRecurring();
        if(isRecurringPrice){
            existingItemDAO  = itemPriceRepository.findItemWithMatchingRecurringItemPrice(item.getSourceId(),
                    item.getPrice().getRecurringPrice().getAmount(), item.getPrice().getRecurringPrice().getDuration(),
                    Long.valueOf(item.getPrice().getRecurringPrice().getTimeFrame().getId()));
        } else {
            existingItemDAO  = itemPriceRepository.findItemWithMatchingOneTimeItemPrice(item.getSourceId(), item.getPrice().getOneTimePrice());
        }

        if(existingItemDAO!=null) {
            return existingItemDAO;
        }

        ItemPriceDAO itemPriceDAO = new ItemPriceDAO();
        itemPriceDAO.setSourceId(item.getSourceId());
        itemPriceDAO.setName(item.getName());
        Price price = item.getPrice();
        if (isRecurringPrice) {
            RecurringPrice recurringPrice = price.getRecurringPrice();
            itemPriceDAO.setAmount(recurringPrice.getAmount());
            itemPriceDAO.setDurationUnits(recurringPrice.getDuration());
            itemPriceDAO.setTimeFrameId(Long.valueOf(recurringPrice.getTimeFrame().getId()));
        } else {
            itemPriceDAO.setOneTimePrice(price.getOneTimePrice());
        }
        itemPriceDAO = itemPriceRepository.save(itemPriceDAO);

        return itemPriceDAO;

    }

    @Transactional
//    private void addItemToExistingCart(Long cartId, String sourceId, Integer actionId, Integer quantity) {
//        // Step 1: Load the cart
//        CartDAO cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));
//
//        //todo check if existing item has the same price type and amount
//        // Step 2: Load the item
//        ItemDAO item = itemRepository.findBySourceId(sourceId)
//                .orElseThrow(() -> new IllegalArgumentException("Item not found with source ID: " + sourceId));
//
//        // Step 3: Check if item already exists in this cart
//        Optional<CartContentDAO> existingContentOpt = cartContentRepository.findByCartAndItem(cart, item);
//
//        if (existingContentOpt.isPresent()) {
//            // Item already in cart — increase quantity
//            CartContentDAO existingContent = existingContentOpt.get();
//            int currentQty = existingContent.getQuantity() != null ? existingContent.getQuantity() : 0;
//            existingContent.setQuantity(currentQty + (quantity != null ? quantity : 1));
//            cartContentRepository.save(existingContent);
//        } else {
//            // Item not in cart — create new CartContent entry
//            CartContentDAO newContent = new CartContentDAO();
//            newContent.setCart(cart);
//            newContent.setItem(item.getSourceId());
//            newContent.setActionId(actionId);
//            newContent.setQuantity(quantity != null ? quantity : 1);
//            cartContentRepository.save(newContent);
//        }
//    }

    private void createNewCart(AddItem item, ItemPriceDAO itemDAO) {

        CartDAO cart = new CartDAO();
        cart.setUserId(item.getUserId());
        cart.setDateCreated(LocalDate.now());
        if(item.getTransactionId()==null || item.getTransactionId().isEmpty()) {
            cart.setTransactionId(UUID.randomUUID().toString());
        } else {
            cart.setTransactionId(item.getTransactionId());
        }
        cart = cartRepository.save(cart);
        if (cart == null) {
            throw new IllegalStateException("cart is null!");
        }

        CartContentDAO cartContent = new CartContentDAO();
        cartContent.setCart(cart);
        cartContent.setItemPrice(itemDAO);
        cartContent.setActionId(ActionEnum.ADD.getId()); // Example: 1 = add
        cartContent.setQuantity(item.getQuantity().intValue());

        cartContent = cartContentRepository.save(cartContent);

        List<CartContentDAO> cartContentDAOList = new ArrayList<>();
        cartContentDAOList.add(cartContent);
        cart.setContents(cartContentDAOList);

        cartRepository.save(cart);

    }

}