package ru.doublegis.service;

import io.reactivex.Single;
import ru.doublegis.model.Cinema;
import ru.doublegis.model.Order;

public interface BookingService {
    Single<Cinema> fetch();
    Single<Order> execute(Order order);
}
