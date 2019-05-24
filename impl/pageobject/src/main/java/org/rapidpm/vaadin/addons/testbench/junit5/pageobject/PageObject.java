/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rapidpm.vaadin.addons.testbench.junit5.pageobject;

import static java.lang.System.getProperties;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.success;
import static org.rapidpm.vaadin.addons.webdriver.WebDriverFunctions.takeScreenShot;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.HasContainerInfo;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.NetworkFunctions;
import org.rapidpm.vaadin.addons.webdriver.HasDriver;

public interface PageObject extends HasContainerInfo, HasDriver, HasLogger {

  default void loadPage() {
    final String url = url().get();
    logger().info("Navigate browser to " + url);
    getDriver().get(url);
  }

  default void loadPage(String route) {
    final String url = url().get();
    logger().info("Navigate browser to " + url + route);
    getDriver().get(url + route);
  }

  default String getTitle() {
    return getDriver().getTitle();
  }

  default BiFunction<String, String, String> property() {
    return (key, defaultValue) -> (String) getProperties().getOrDefault(key, defaultValue);
  }

  default Supplier<String> protocol() {
    return () -> property().apply(NetworkFunctions.SERVER_PROTOCOL, NetworkFunctions.DEFAULT_PROTOCOL);
  }

  default Supplier<String> ip() {
    return () -> getContainerInfo().getHost();
  }

  default Supplier<String> port() {
    return () -> String.valueOf(getContainerInfo().getPort());
  }

  //TODO per properties
  default Supplier<String> webapp() {
    return () -> property().apply(NetworkFunctions.SERVER_WEBAPP, NetworkFunctions.DEFAULT_SERVLET_WEBAPP);
  }


  default Supplier<String> baseURL() {
    return () -> protocol().get() + "://" + ip().get() + ":" + port().get();
  }

  default Supplier<String> url() {
    return () -> match(
        matchCase(() -> success("/" + webapp().get() + "/")),
        matchCase(() -> webapp().get().equals(""), () -> success("/")),
        matchCase(() -> webapp().get().endsWith("/") && webapp().get().startsWith("/"), () -> success(webapp().get())),
        matchCase(() -> webapp().get().endsWith("/") && !webapp().get().startsWith("/"), () -> success("/" + webapp().get())),
//        matchCase(() -> !webapp().get().endsWith("/") && webapp().get().startsWith("/"), () -> success(webapp().get() + "/")),
        matchCase(() -> webapp().get().equals("/"), () -> success("/"))
    )
        .map(e -> baseURL().get() + e)
        .get();
  }

  default void destroy() {
    ((CheckedExecutor) getDriver()::quit)
        .apply(null)
        .ifPresentOrElse(
            ok -> logger().info("webdriver quit -> OK"),
            failed -> logger().warning("webdriver quit failed -> " + failed)
        );

    ((CheckedExecutor) getDriver()::close)
        .apply(null)
        .ifPresentOrElse(
            ok -> logger().info("webdriver close -> OK"),
            failed -> logger().warning("webdriver close failed -> " + failed)
        );
  }

  default void screenshot() {
    takeScreenShot().accept(getDriver());
  }

}