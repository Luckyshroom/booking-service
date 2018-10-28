package ru.doublegis;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

import java.util.*;

public abstract class RestfulVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulVerticle.class);

    private ServiceDiscovery serviceDiscovery;
    private Set<Record> registeredRecords = new ConcurrentHashSet<>();

    /**
     * Enable CORS support.
     *
     * @param router router instance
     */
    protected void enableCorsSupport(Router router) {
        router.route().handler(CorsHandler.create("*")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Content-Type")
                .allowedHeader("accept")
                .allowedHeader("origin")
                .allowedHeader("x-requested-with")
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
        );
    }

    protected Completable publish(Record record) {
        return serviceDiscovery.rxPublish(record)
                .doOnSuccess(ar -> {
                    registeredRecords.add(record);
                    LOGGER.info("Service <" + ar.getName() + "> published");
                }).toCompletable();
    }

    protected Completable publishHttpEndpoint(int port, String host, String serviceName) {
        Record record = HttpEndpoint.createRecord(serviceName, host, port, "/api/" + serviceName,
                new JsonObject().put("api.name", serviceName));
        return publish(record);
    }

    protected void badRequest(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    protected void notFound(RoutingContext context) {
        context.response().setStatusCode(404)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", "not_found").encodePrettily());
    }

    protected void internalError(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    @Override
    public void start() throws Exception {
        serviceDiscovery = ServiceDiscovery
                .create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));
    }

    @Override
    public void stop(Future<Void> future) throws Exception {
        Observable.fromIterable(registeredRecords)
                .flatMapCompletable(record -> serviceDiscovery.rxUnpublish(record.getRegistration()))
                .subscribe(CompletableHelper.toObserver(future));
    }
}
