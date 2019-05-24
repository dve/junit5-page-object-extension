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
package org.rapidpm.vaadin.addons.webdriver.junit5;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;

import static org.rapidpm.vaadin.addons.webdriver.BrowserDriverFunctions.unittestingWebDriverInstance;
import static org.rapidpm.vaadin.addons.webdriver.junit5.WebdriverExtensionFunctions.removeWebDriver;
import static org.rapidpm.vaadin.addons.webdriver.junit5.WebdriverExtensionFunctions.storeWebDriver;
import static org.rapidpm.vaadin.addons.webdriver.junit5.WebdriverExtensionFunctions.webdriver;

/**
 *
 */
public class WebdriverUnitTestExtension implements BeforeEachCallback, AfterEachCallback, HasLogger {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    logger().info("beforeEach  -> load default browser webdriver for unit testing");
    unittestingWebDriverInstance()
        .ifPresentOrElse(
            webDriver -> storeWebDriver().accept(context, webDriver),
            failed -> logger().warning(failed)
        );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    logger().info("afterEach  -> will close and remove the webdriver now..");
    webdriver()
        .apply(context)
        .close();
    removeWebDriver().accept(context);
  }
}
