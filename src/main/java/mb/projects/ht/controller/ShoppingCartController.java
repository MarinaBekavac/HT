package mb.projects.ht.controller;

import jakarta.validation.Valid;
import mb.projects.ht.entities.Tutorial;
import mb.projects.ht.enums.ActionEnum;
import mb.projects.ht.exception.NotFoundException;
import mb.projects.ht.model.Cart;
import mb.projects.ht.repository.ActionsRepository;
import mb.projects.ht.request.*;
import mb.projects.ht.response.GetCartResponse;
import mb.projects.ht.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private ActionsRepository actionsRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/getAction")
    public ResponseEntity<ActionEnum> getAllTutorials(@RequestBody GetActionRequest getActionRequest) {
        try {
            System.out.println("Action id: " + getActionRequest.getActionId());
            ActionEnum action = ActionEnum.ADD;

            if (getActionRequest == null)
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            else
               action =  ActionEnum.fromId(actionsRepository.findByActionId(getActionRequest.getActionId()).getActionId());

            System.out.println(action);

            return new ResponseEntity<>(action, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getShoppingCart")
    public ResponseEntity<GetCartResponse> getCartForUser(@Valid @RequestBody GetCartRequest getCartRequest) {
        System.out.println("Called get cart with: " + getCartRequest);

        GetCartResponse cart = cartService.getCartByUserId(getCartRequest.getUserId());

        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @GetMapping("/by-item")
    public ResponseEntity<?> getCartsByItemCriteria(@RequestBody GetCountBySourceIdAndAction getCountBySourceIdAndAction) {

        System.out.println("Req: " + getCountBySourceIdAndAction);
//        List<Cart> carts = cartService.getCartsByCriteria(getCountBySourceIdAndAction.getSourceId(),
//                getCountBySourceIdAndAction.getActionId(), getCountBySourceIdAndAction.getDateFrom(),
//                getCountBySourceIdAndAction.getDateTo());
//        for(Cart c : carts) {
//            System.out.println(c);
//        }
//
//        if(carts==null || carts.isEmpty()) {
//            System.out.println("Cart is NULL or empty");
//            return ResponseEntity.ok(Map.of("count", 0));
//        }
//
//        long count = carts.size();
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
        GetCartResponse cart = cartService.addItem(item);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/buy")
    public ResponseEntity<GetCartResponse> butItemsInCart(@RequestBody BuyItemsInCartRequest buyItemsInCartRequest) {
        System.out.println("Req: " + buyItemsInCartRequest);
        GetCartResponse cart = cartService.buyCart(buyItemsInCartRequest);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

}
