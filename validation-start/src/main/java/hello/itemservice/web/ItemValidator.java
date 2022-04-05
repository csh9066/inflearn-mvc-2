package hello.itemservice.web;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        // 검증 로직
        if (isEmpty(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }

        if(isNull(item.getPrice())) {
            errors.rejectValue("price", "required");
        }

        if (isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            errors.rejectValue("price", "range", new Object[]{1000 ,1000000}, null);
        }

        if (isNull(item.getQuantity()) || item.getQuantity() < 1 || item.getQuantity() > 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }


        if (nonNull(item.getPrice()) && nonNull(item.getQuantity())) {
            int totalPrice = item.getPrice() * item.getQuantity();

            if (totalPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{9999, totalPrice}, null);
            }
        }
    }
}
