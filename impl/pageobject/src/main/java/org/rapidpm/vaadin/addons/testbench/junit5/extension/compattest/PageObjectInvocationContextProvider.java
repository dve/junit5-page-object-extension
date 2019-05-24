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
package org.rapidpm.vaadin.addons.testbench.junit5.extension.compattest;

import static java.util.Collections.singletonList;
import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.ExtensionContextFunctions.containerInfo;
import static org.rapidpm.vaadin.addons.webdriver.BrowserDriverFunctions.webDriverInstances;
import static org.rapidpm.vaadin.addons.webdriver.WebDriverFunctions.webdriverName;
import static org.rapidpm.vaadin.addons.webdriver.junit5.WebdriverExtensionFunctions.storeWebDriver;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.memoizer.Memoizer;
import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.ContainerInfo;
import org.rapidpm.vaadin.addons.testbench.junit5.pageobject.PageObject;
import xxx.com.github.webdriverextensions.WebDriverExtensionFieldDecorator;

/**
 *
 */
public class PageObjectInvocationContextProvider implements TestTemplateInvocationContextProvider, HasLogger {

  public interface WebDriverTemplateInvocationContext extends TestTemplateInvocationContext {
    WebDriver webdriver();
  }

  private final class WebDriverTemplateInvocationContextImpl
      implements WebDriverTemplateInvocationContext {
    private final Supplier<WebDriver> webdriverSupplier;

    private WebDriverTemplateInvocationContextImpl(Supplier<WebDriver> webdriverSupplier) {
      this.webdriverSupplier = Memoizer.memoize(webdriverSupplier);
    }

    @Override
    public WebDriver webdriver() {
      return webdriverSupplier.get();
    }

    @Override
    public String getDisplayName(int invocationIndex) {
      return webdriverName().apply(webdriver());
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
      return singletonList(new ParameterResolver() {
        @Override
        public boolean supportsParameter(ParameterContext parameterContext,
                                         ExtensionContext extensionContext) {
          final Class<?> type = parameterContext.getParameter().getType();
          return PageObject.class.isAssignableFrom(type);
        }

        @Override
        public PageObject resolveParameter(ParameterContext parameterContext,
                                       ExtensionContext extensionContext) {

          Class<?> pageObjectClass = parameterContext
              .getParameter()
              .getType();

          final Result<PageObject> po = ((CheckedFunction<Class<?>, PageObject>) aClass -> {
            final Constructor<?> constructor = pageObjectClass.getConstructor(WebDriver.class, ContainerInfo.class);
            WebDriver webDriver = webdriver();
            PageObject page = PageObject.class.cast(constructor.newInstance(webDriver, containerInfo().apply(extensionContext)));
            PageFactory.initElements(new WebDriverExtensionFieldDecorator(webDriver), page);
            return page;
          })
              .apply(pageObjectClass);

          po.ifPresentOrElse(
              success -> logger().fine("pageobject of type " + pageObjectClass.getSimpleName() + " was created with " + webdriverName().apply(webdriver())),
              failed -> logger().warning("was not able to create PageObjectInstance " + failed)
          );
          po.ifAbsent(() -> {
            throw new ParameterResolutionException("was not able to create PageObjectInstance of type " + pageObjectClass);
          });
          return po.get();
        }
      });
    }
  }


  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    return true;
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

    logger().info("provideTestTemplateInvocationContexts");

    return webDriverInstances()
        .stream()
        .map(this::invocationContext)
        .peek(po -> {
          logger().info("peek - page object -> setting as webDriver into Store ");
          storeWebDriver().accept(context, po.webdriver());
        })
        .map(e -> (WebDriverTemplateInvocationContext) e);
  }


  private WebDriverTemplateInvocationContext invocationContext(Supplier<WebDriver> webdriverSupplier) {
    return new WebDriverTemplateInvocationContextImpl(webdriverSupplier);
  }
}
