package flaskspring.demo.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import flaskspring.demo.travel.domain.QPlace;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
@Service
public class SearchCondUtil {
    public static <T> BooleanExpression eq(SimpleExpression<T> target, T value) {
        return value == null ? null : target.eq(value);
    }

    public static <T> BooleanExpression startWith(StringExpression target, String value) {
        return value == null ? null : target.startsWith(value);
    }

    public static <T> BooleanExpression contains(StringExpression target, String value) {
        return value == null ? null : target.contains(value);
    }

    public static OrderSpecifier[] placeOrder(QPlace place, String order) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (StringUtils.hasText(order) && order.equals("recommend")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, place.));
        } else if (StringUtils.hasText(order) && order.equals("alpha")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, place.createdTime));
        } else if (StringUtils.hasText(order) && order.equals("like")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, place.reviewCount));
        } else if (StringUtils.hasText(order) && order.equals("register")) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, place.salePrice));
        } else {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, place.createdTime));
        }
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}*/
