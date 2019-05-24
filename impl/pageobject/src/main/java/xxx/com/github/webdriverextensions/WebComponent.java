/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 * Copyright © 2018 Daniel Nordhoff-Vergien (dve@vergien.net)
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
package xxx.com.github.webdriverextensions;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public abstract class WebComponent implements org.openqa.selenium.WebElement {

  WebElement wrappedWebElement;
  WebElement delegateWebElement;
  WebDriver webDriver;

  public void init(WebDriver webDriver, WebElement wrappedWebElement) {
    this.wrappedWebElement = wrappedWebElement;
    this.webDriver = webDriver;
  }

  public void init(WebDriver webDriver, WebElement wrappedWebElement,
      WebElement delegateWebElement) {
    this.wrappedWebElement = wrappedWebElement;
    this.delegateWebElement = delegateWebElement;
    this.webDriver = webDriver;
  }

  public WebElement getWrappedWebElement() {
    return wrappedWebElement;
  }

  public WebElement getDelegateWebElement() {
    return delegateWebElement;
  }

  @Override
  public void click() {
    if (delegateWebElement != null) {
      delegateWebElement.click();
    } else {
      wrappedWebElement.click();
    }
  }

  @Override
  public void submit() {
    if (delegateWebElement != null) {
      delegateWebElement.submit();
    } else {
      wrappedWebElement.submit();
    }
  }


  @Override
  public void sendKeys(CharSequence... keysToSend) {
    if (delegateWebElement != null) {
      delegateWebElement.sendKeys(keysToSend);
    } else {
      wrappedWebElement.sendKeys(keysToSend);
    }
  }

  @Override
  public void clear() {
    if (delegateWebElement != null) {
      delegateWebElement.clear();
    } else {
      wrappedWebElement.clear();
    }
  }

  @Override
  public String getTagName() {
    if (delegateWebElement != null) {
      return delegateWebElement.getTagName();
    } else {
      return wrappedWebElement.getTagName();
    }
  }

  @Override
  public String getAttribute(String name) {
    if (delegateWebElement != null) {
      return delegateWebElement.getAttribute(name);
    } else {
      return wrappedWebElement.getAttribute(name);
    }
  }

  @Override
  public boolean isSelected() {
    if (delegateWebElement != null) {
      return delegateWebElement.isSelected();
    } else {
      return wrappedWebElement.isSelected();
    }
  }

  @Override
  public boolean isEnabled() {
    if (delegateWebElement != null) {
      return delegateWebElement.isEnabled();
    } else {
      return wrappedWebElement.isEnabled();
    }
  }

  @Override
  public String getText() {
    if (delegateWebElement != null) {
      return delegateWebElement.getText();
    } else {
      return wrappedWebElement.getText();
    }
  }

  @Override
  public List<WebElement> findElements(By by) {
    return wrappedWebElement.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return wrappedWebElement.findElement(by);
  }

  @Override
  public boolean isDisplayed() {
    if (delegateWebElement != null) {
      return delegateWebElement.isDisplayed();
    } else {
      return wrappedWebElement.isDisplayed();
    }
  }

  @Override
  public Point getLocation() {
    if (delegateWebElement != null) {
      return delegateWebElement.getLocation();
    } else {
      return wrappedWebElement.getLocation();
    }
  }

  @Override
  public Dimension getSize() {
    if (delegateWebElement != null) {
      return delegateWebElement.getSize();
    } else {
      return wrappedWebElement.getSize();
    }
  }

  @Override
  public Rectangle getRect() {
    if (delegateWebElement != null) {
      return delegateWebElement.getRect();
    } else {
      return wrappedWebElement.getRect();
    }
  }

  @Override
  public String getCssValue(String propertyName) {
    if (delegateWebElement != null) {
      return delegateWebElement.getCssValue(propertyName);
    } else {
      return wrappedWebElement.getCssValue(propertyName);
    }
  }

  public void click(WebElement webElement) {
    webElement.click();
  }

  public void clear(WebElement webElement) {
    webElement.clear();
  }

  public boolean isDisplayed(WebElement webElement) {
    return webElement.isDisplayed();
  }

  @Override
  public <X> X getScreenshotAs(OutputType<X> ot) throws WebDriverException {
    return wrappedWebElement.getScreenshotAs(ot);
  }
}
