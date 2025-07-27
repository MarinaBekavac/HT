package mb.projects.ht.model.mapper;

import mb.projects.ht.entities.CartDAO;
import mb.projects.ht.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    @Autowired
    private ItemMapper itemMapper;

    public Cart toCart(CartDAO cartDAO) {
        if (cartDAO == null) {
            return null;
        }

        Cart cart = new Cart();
        cart.setUserId(cartDAO.getUserId());
        cart.setDateCreated(convertToDate(cartDAO.getDateCreated()));
        cart.setDateModified(convertToDate(cartDAO.getDateModified()));
        cart.setTransactionId(cartDAO.getTransactionId());

        if (cartDAO.getContents() != null) {
            cart.setListOfItemsInCart(
                    cartDAO.getContents().stream()
                            .map(content -> itemMapper.toItem(content.getItemPrice(), content.getActionId(), content.getQuantity()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())
            );
        }

        return cart;
    }

    private Date convertToDate(LocalDate localDate) {
        return localDate != null ?
                Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) :
                null;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date != null ?
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() :
                null;
    }
}
