package ru.doublegis.service.impl;

import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.shareddata.AsyncMap;
import io.vertx.reactivex.core.shareddata.SharedData;
import ru.doublegis.exception.CustomException;
import ru.doublegis.model.Cinema;
import ru.doublegis.model.Hall;
import ru.doublegis.model.Order;
import ru.doublegis.model.Seat;
import ru.doublegis.service.BookingService;

import java.util.HashMap;
import java.util.Map;

import static ru.doublegis.util.Constants.*;

public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    private AsyncMap<String, Cinema> asyncMap;

    public BookingServiceImpl(Vertx vertx) {
        SharedData sharedData = vertx.sharedData();
        sharedData.<String, Cinema>getAsyncMap(DEFAULT_ASYNC_MAP_NAME, res -> {
            if (res.succeeded()) {
                asyncMap = res.result();
                asyncMap.size(ar -> {
                    if (ar.result() == 0) {
                        LOGGER.info("Initializing map...");
                        initialize();
                    }
                });
            } else {
                LOGGER.debug("Failed to get map!");
            }
        });
    }

    private void initialize() {
        Map<Integer, Hall> hallMap = new HashMap<>();

        for (int h = 1; h <= 5; h++) {
            int counter = 0;
            Map<Integer, Seat> seatMap = new HashMap<>();
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 4; j++) {
                    counter++;
                    seatMap.put(counter, new Seat(counter, i, j));
                }
            }
            hallMap.put(h, new Hall(h, seatMap));
        }

        asyncMap.put(DEFAULT_ASYNC_MAP_KEY, new Cinema(1, hallMap), resPut -> {
            if (resPut.succeeded()) {
                LOGGER.info("Data has been initialized!");
            } else {
                LOGGER.info("Data initialization failed!");
            }
        });
    }

    public Single<Cinema> fetch() {
        return asyncMap.rxGet(DEFAULT_ASYNC_MAP_KEY);
    }

    public Single<Order> execute(Order order) {
        if (order.getHolderName().length() > 0 && order.getHolderEmail().matches(EMAIL_REGEX)) {
            try {
                return asyncMap.rxGet(DEFAULT_ASYNC_MAP_KEY).map(ar -> {
                    order.getSeatNums().forEach(i -> {
                        Seat seat = ar.getHallMap().get(order.getHallNum()).getSeatMap().get(i);
                        if (seat.isAvailable()) {
                            seat.setAvailable(false)
                                    .setHolderName(order.getHolderName())
                                    .setHolderEmail(order.getHolderEmail());
                            ar.getHallMap().get(order.getHallNum()).getSeatMap().put((Integer) i, seat);
                        } else {
                            throw new CustomException("Seat " + seat.getNum() + " is not available");
                        }
                    });
                    LOGGER.info("Data has been updated!");
                    return ar;
                }).flatMap(ar -> asyncMap.rxPut(DEFAULT_ASYNC_MAP_KEY, ar).andThen(Single.just(order)));
            } catch (Exception ex) {
                return Single.error(ex);
            }
        } else {
            return Single.error(new Throwable("Request details is incorrect"));
        }
    }
}
