package ru.doublegis.service;

import io.reactivex.Maybe;
import ru.doublegis.model.Cinema;
import ru.doublegis.model.Order;

public interface BookingService {
    Maybe<Cinema> fetch();
    Maybe<Order> execute(Order order);
}
