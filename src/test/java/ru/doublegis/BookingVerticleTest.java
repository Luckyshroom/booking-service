package ru.doublegis;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.reactivex.core.Vertx;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.doublegis.model.Order;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

@RunWith(VertxUnitRunner.class)
public class BookingVerticleTest {

    private Vertx vertx;
    private Integer port;

    @Before
    public void setUp(TestContext context) throws IOException {
        vertx = Vertx.vertx();

        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port)
                );

        vertx.deployVerticle(BookingVerticle.class.getName(), options, context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testGetInfo(TestContext context) {
        Async async = context.async();
        vertx.createHttpClient().getNow(port, "localhost", "/api/info", response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertEquals(response.headers().get("content-type"), "application/json");
            response.bodyHandler(body -> {
                context.assertTrue(body.toString().contains("hallMap"));
                async.complete();
            });
        });
    }

    @Test
    public void testOrder(TestContext context) {
        Async async = context.async();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("hallNum", 2)
                .put("seatNums", new JsonArray(Arrays.asList(5,6,12)))
                .put("holderName", "John")
                .put("holderEmail", "john@gmail.com");
        vertx.createHttpClient().post(port, "localhost", "/api/booking")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(jsonObject.toString().length()))
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 201);
                    context.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        final Order order = new Order(body.toJsonObject());
                        context.assertEquals(order.getHallNum(), 2);
                        context.assertEquals(order.getHolderName(), "John");
                        context.assertEquals(order.getHolderEmail(), "john@gmail.com");
                        async.complete();
                    });
                })
                .write(jsonObject.toString())
                .end();
    }
}