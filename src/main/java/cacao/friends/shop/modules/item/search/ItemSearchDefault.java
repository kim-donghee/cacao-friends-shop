package cacao.friends.shop.modules.item.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ItemSearchDefault {
	
	ItemStatus itemStatus() default ItemStatus.PUBLISHED;
	
	ItemSortProperty sortProperty() default ItemSortProperty.NEW;
	
}
