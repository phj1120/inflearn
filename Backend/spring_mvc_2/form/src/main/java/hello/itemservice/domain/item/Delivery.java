package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Delivery {

    private String code;
    private String displayName;

    public Delivery(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}
