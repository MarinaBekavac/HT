package mb.projects.ht.controller;

import jakarta.validation.Valid;
import mb.projects.ht.model.Price;
import mb.projects.ht.model.RecurringPrice;
import mb.projects.ht.request.*;
import mb.projects.ht.response.GetCartResponse;
import mb.projects.ht.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private CartService cartService;

    //fetches currently active carts
    @GetMapping("/getShoppingCart")
    public ResponseEntity<GetCartResponse> getCartForUser(@Valid @RequestBody GetCartRequest getCartRequest) {
        System.out.println("Called get cart with: " + getCartRequest);

        GetCartResponse cart = cartService.getCartByUserId(getCartRequest.getUserId());

        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getCartsByItemCriteria(@RequestBody GetCountBySourceIdAndAction getCountBySourceIdAndAction) {

        System.out.println("Req: " + getCountBySourceIdAndAction);
        long count = cartService.getCartsByCriteria(getCountBySourceIdAndAction.getSourceId(),
                getCountBySourceIdAndAction.getActionId(), getCountBySourceIdAndAction.getDateFrom(),
                getCountBySourceIdAndAction.getDateTo());

        return ResponseEntity.ok(Map.of("count", count));

    }

    @DeleteMapping("/deleteItem")
    public ResponseEntity<GetCartResponse> deleteItemFromCart(@RequestBody DeleteItemFromCartRequest deleteItemFromCartRequest) {
        System.out.println("Req: " + deleteItemFromCartRequest);
        //todo add how many you wabt to delete
        GetCartResponse cart = cartService.deleteItemFromCart(deleteItemFromCartRequest);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/addItem")
    public ResponseEntity<GetCartResponse> addItem(@RequestBody AddItem item) {
        System.out.println("Req: " + item);

        Price price = item.getPrice();
        boolean hasOneTimePrice = price.getOneTimePrice() != null;
        //boolean hasRecurringPrices = price.getRecurringPrice() != null && !price.getRecurringPrices().isEmpty();
        boolean hasRecurringPrices = price.getRecurringPrice() != null;

        if(!hasRecurringPrices) {
            price.setRecurringPrice(new RecurringPrice());
        }
        if(!hasOneTimePrice) {
            price.setOneTimePrice(null);
        }


        GetCartResponse cart = cartService.addItem(item);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/buy")
    public ResponseEntity<GetCartResponse> butItemsInCart(@RequestBody BuyItemsInCartRequest buyItemsInCartRequest) {
        System.out.println("Req: " + buyItemsInCartRequest);
        GetCartResponse cart = cartService.buyCart(buyItemsInCartRequest);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/evictCart")
    public ResponseEntity<GetCartResponse> evictCart(@RequestBody EvictCartRequest evictCartRequest) {
        System.out.println("Req: " + evictCartRequest);
        GetCartResponse cartResponse = cartService.evictCart(evictCartRequest);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

}
