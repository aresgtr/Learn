package io.vertx.guides.wiki;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {


  private static final String SQL_CREATE_PAGES_TABLE = "create table if not exists Pages (Id integer identity primary key, Name varchar(255) unique, Content clob)";
  private static final String SQL_GET_PAGE = "select Id, Content from Pages where Name = ?"; // ? are placeholders to pass data
  private static final String SQL_CREATE_PAGE = "insert into Pages values (NULL, ?, ?)";
  private static final String SQL_SAVE_PAGE = "update Pages set Content = ? where Id = ?";
  private static final String SQL_ALL_PAGES = "select Name from Pages";
  private static final String SQL_DELETE_PAGE = "delete from Pages where Id = ?";



  private JDBCClient dbClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);



  private Future<Void> prepareDatabase() {
    Promise<Void> promise = Promise.promise();

    dbClient = JDBCClient.createShared(vertx, new JsonObject()  // share connection among verticles
      .put("url", "jdbc:hsqldb:file:db/wiki")   // JDBC URL
      .put("driver_class", "org.hsqldb.jdbcDriver")
      .put("max_pool_size", 30));   // number of concurrent connections

    //  Getting a connection is an asynchronous operation that gives us an AsyncResult<SQLConnection>. It must then be tested to see if the connection could be established or not
    //  (AsyncResult is actually a super-interface of Future).
    dbClient.getConnection(ar -> {
      if (ar.failed()) {
        LOGGER.error("Could not open a database connection", ar.cause());
        //  If the SQL connection could not be obtained, then the method future is completed to fail with the AsyncResult-provided exception via the cause method.
        promise.fail(ar.cause());
      } else {
        SQLConnection connection = ar.result();   // The SQLConnection is the result of the successful AsyncResult. We can use it to perform a SQL query.
        connection.execute(SQL_CREATE_PAGES_TABLE, create -> {
          connection.close();   // Before checking whether the SQL query succeeded or not, we must release it by calling close, otherwise the JDBC client connection pool can eventually drain.
          if (create.failed()) {
            LOGGER.error("Database preparation error", create.cause());
            promise.fail(create.cause());
          } else {
            promise.complete();
          }
        });
      }
    });

    return promise.future();
  }



  private FreeMarkerTemplateEngine templateEngine;

  private Future<Void> startHttpServer() {
    Promise<Void> promise = Promise.promise();
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);
    router.get("/").handler(this::indexHandler);
    //  Routes have their own handlers, and they can be defined by URL and/or by HTTP method. For short handlers a Java lambda is an option,
    //  but for more elaborate handlers it is a good idea to reference private methods instead. Note that URLs can be parametric: /wiki/:page will match a request like /wiki/Hello,
    //  in which case a page parameter will be available with value Hello.
    router.get("/wiki/:page").handler(this::pageRenderingHandler);
    // This makes all HTTP POST requests go through a first handler, here io.vertx.ext.web.handler.BodyHandler. This handler automatically decodes the body from the HTTP requests
    // (e.g., form submissions), which can then be manipulated as Vert.x buffer objects.
    router.post().handler(BodyHandler.create());
    router.post("/save").handler(this::pageUpdateHandler);
    router.post("/create").handler(this::pageCreateHandler);
    router.post("/delete").handler(this::pageDeletionHandler);

    templateEngine = FreeMarkerTemplateEngine.create(vertx);

    server
      .requestHandler(router)   // The router object can be used as a HTTP server handler, which then dispatches to other handlers as defined above.
      .listen(8080, ar -> {
        if (ar.succeeded()) {
          LOGGER.info("HTTP server running on port 8080");
          promise.complete();
        } else {
          LOGGER.error("Could not start a HTTP server", ar.cause());
          promise.fail(ar.cause());
        }
      });

    return promise.future();
  }



  private void pageDeletionHandler(RoutingContext context) {
    String id = context.request().getParam("id");
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.updateWithParams(SQL_DELETE_PAGE, new JsonArray().add(id), res -> {
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);
            context.response().putHeader("Location", "/");
            context.response().end();
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }



  private void pageCreateHandler(RoutingContext context) {
    String pageName = context.request().getParam("name");
    String location = "/wiki/" + pageName;
    if (pageName == null || pageName.isEmpty()) {
      location = "/";
    }
    context.response().setStatusCode(303);
    context.response().putHeader("Location", location);
    context.response().end();
  }



  private void indexHandler(RoutingContext context) {
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.query(SQL_ALL_PAGES, res -> {
          connection.close();

          if (res.succeeded()) {
            List<String> pages = res.result() // SQL query results are being returned as instances of JsonArray and JsonObject.
              .getResults()
              .stream()
              .map(json -> json.getString(0))
              .sorted()
              .collect(Collectors.toList());

            context.put("title", "Wiki home");  // The RoutingContext instance can be used to put arbitrary key / value data that is then available from templates, or chained router handlers.
            context.put("pages", pages);
            templateEngine.render(context.data(), "templates/index.ftl", ar -> {   // Rendering a template is an asynchronous operation that leads us to the usual AsyncResult handling pattern.
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());  // The AsyncResult contains the template rendering as a String in case of success, and we can end the HTTP response stream with the value.
              } else {
                context.fail(ar.cause());
              }
            });

          } else {
            context.fail(res.cause());  // In case of failure the fail method from RoutingContext provides a sensible way to return a HTTP 500 error to the HTTP client.
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }


  //  Page saving
  private void pageUpdateHandler(RoutingContext context) {
    //  Form parameters sent through a HTTP POST request are available from the RoutingContext object.
    //  Note that without a BodyHandler within the Router configuration chain these values would not be available,
    //  and the form submission payload would need to be manually decoded from the HTTP POST request payload.
    String id = context.request().getParam("id");
    String title = context.request().getParam("title");
    String markdown = context.request().getParam("markdown");
    boolean newPage = "yes".equals(context.request().getParam("newPage"));  // We rely on a hidden form field rendered in the page.ftl FreeMarker template to know if we are updating an existing page or saving a new page.

    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        String sql = newPage ? SQL_CREATE_PAGE : SQL_SAVE_PAGE;
        JsonArray params = new JsonArray();   // Again, preparing the SQL query with parameters uses a JsonArray to pass values.
        if (newPage) {
          params.add(title).add(markdown);
        } else {
          params.add(markdown).add(id);
        }
        connection.updateWithParams(sql, params, res -> {   // The updateWithParams method is used for insert / update / delete SQL queries.
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);    // Upon success, we simply redirect to the page that has been edited.
            context.response().putHeader("Location", "/wiki/" + title);
            context.response().end();
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }



  private static final String EMPTY_PAGE_MARKDOWN =
    "# A new page\n" +
      "\n" +
      "Feel-free to write in Markdown!\n";

  private void pageRenderingHandler(RoutingContext context) {
    String page = context.request().getParam("page");   // URL parameters (/wiki/:page here) can be accessed through the context request object.

    dbClient.getConnection(car -> {
      if (car.succeeded()) {

        SQLConnection connection = car.result();
        connection.queryWithParams(SQL_GET_PAGE, new JsonArray().add(page), fetch -> {  // Passing argument values to SQL queries is done using a JsonArray, with the elements in order of the ? symbols in the SQL query.
          connection.close();
          if (fetch.succeeded()) {

            JsonArray row = fetch.result().getResults()
              .stream()
              .findFirst()
              .orElseGet(() -> new JsonArray().add(-1).add(EMPTY_PAGE_MARKDOWN));
            Integer id = row.getInteger(0);
            String rawContent = row.getString(1);

            context.put("title", page);
            context.put("id", id);
            context.put("newPage", fetch.result().getResults().size() == 0 ? "yes" : "no");
            context.put("rawContent", rawContent);
            context.put("content", Processor.process(rawContent));  // The Processor class comes from the txtmark Markdown rendering library that we use.
            context.put("timestamp", new Date().toString());

            templateEngine.render(context.data(), "templates/page.ftl", ar -> {
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
              } else {
                context.fail(ar.cause());
              }
            });
          } else {
            context.fail(fetch.cause());
          }
        });

      } else {
        context.fail(car.cause());
      }
    });
  }



  @Override
  public void start(Promise<Void> promise) throws Exception {
    Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
    steps.setHandler(promise);
  }


  public void anotherStart(Promise<Void> promise) throws Exception {

    Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
    steps.setHandler(ar -> {  // <1>
      if (ar.succeeded()) {
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });

  }
}
