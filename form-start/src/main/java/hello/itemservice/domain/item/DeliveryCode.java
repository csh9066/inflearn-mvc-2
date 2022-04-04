package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeliveryCode {
    private String code;
    private String displayName;
}
