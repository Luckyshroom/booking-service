package ru.doublegis;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import ru.doublegis.model.Order;
import ru.doublegis.service.BookingService;
import ru.doublegis.service.impl.BookingServiceImpl;

import static ru.doublegis.util.Constants.*;

public class BookingVerticle extends RestfulVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingVerticle.class);

    private BookingService bookingService;

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start();

        int port = config().getInteger("http.port", DEFAULT_HTTP_PORT);
        String host = config().getString("http.address", "localhost");

        bookingService = new BookingServiceImpl(vertx);

        Router router = Router.router(vertx);

        enableCorsSupport(router);

        router.route("/*").handler(BodyHandler.create());
        router.route("/*").handler(StaticHandler.create());

        router.get("/api/info").handler(this::getInfo);
        router.post("/api/booking").handler(this::bookSeat);

        publishHttpEndpoint(port, host, DEFAULT_SERVICE_NAME).subscribe(CompletableHelper.toObserver(future));

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(port, host)
                .subscribe(SingleHelper.toObserver(ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Service <" + DEFAULT_SERVICE_NAME + "> start at port: " + port);
                    } else {
                        LOGGER.info(ar.cause().getMessage());
                    }
                }));
    }

    private void getInfo(RoutingContext context) {
        bookingService.fetch().subscribe(SingleHelper.toObserver(ar -> {
            if (ar.succeeded()) {
                context.response().setStatusCode(200)
                        .putHeader("content-type", "application/json")
                        .end(Json.encodePrettily(ar.result()));
            } else {
                badRequest(context, ar.cause());
            }
        }));
    }

    private void bookSeat(RoutingContext context) {
        Order order = new Order(context.getBodyAsJson());
        bookingService.execute(order).subscribe(SingleHelper.toObserver(ar -> {
            if (ar.succeeded()) {
                context.response().setStatusCode(201)
                        .putHeader("content-type", "application/json")
                        .end(Json.encodePrettily(ar.result()));
            } else {
                badRequest(context, ar.cause());
            }
        }));
    }
}