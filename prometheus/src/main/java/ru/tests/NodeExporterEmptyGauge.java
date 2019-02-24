package ru.tests;


//import com.esotericsoftware.yamlbeans.YamlException;
//import com.esotericsoftware.yamlbeans.YamlReader;
import io.prometheus.client.Gauge;
import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
//import org.apache.hadoop.security.UserGroupInformation;
//import org.jcodings.util.Hash;

//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
import java.net.URISyntaxException;
//import java.net.URL;
//import java.sql.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.Date;


public class NodeExporterEmptyGauge {

    static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    public static void main(String[] args) throws URISyntaxException {

        int PORT = 8000;
        int DELAY_START = 8000;
        int PERIOD = 8000;
        final Timer time = new Timer();
        final Vertx vertx = Vertx.vertx();
        final Router router = Router.router(vertx);

        router.route("/metrics").handler(new MetricsHandler());

        vertx.createHttpServer().requestHandler(router::accept).listen(PORT);


        time.schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {

                if (i >= 21) {
                    System.out.println("Timer is over");
                    vertx.close();
                    time.cancel();
                    return;
                }
                l.labels("foo").inc(Math.random()*3 );
                System.out.println("Request get data GOOD");
                i = i + 1;
            }

        }, DELAY_START, PERIOD);

        System.out.println("finished");

    }
}

