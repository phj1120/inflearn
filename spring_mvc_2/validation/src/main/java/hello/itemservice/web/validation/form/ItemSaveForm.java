package hello.itemservice.web.validation.form;

import hello.itemservice.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemSaveForm {

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 10000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;

    public Item convertItem() {
        Item item = new Item(itemName, price, quantity);
        return item;
    }

    public ItemSaveForm() {
    }

    public ItemSaveForm(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
