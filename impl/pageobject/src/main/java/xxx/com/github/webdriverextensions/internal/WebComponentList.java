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
package xxx.com.github.webdriverextensions.internal;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import xxx.com.github.webdriverextensions.WebComponent;
import xxx.com.github.webdriverextensions.WebDriverExtensionFieldDecorator;

public class WebComponentList<T extends WebComponent> implements List<T> {

  private Class<T> webComponentClass;
  private List<WebElement> wrappedWebElements;
  private List<T> webComponents;
  private WebComponentFactory webComponentFactory;
  private WebDriver driver;
  private ParameterizedType genericTypeArguments;

  public WebComponentList(Class<T> webComponentClass, List<WebElement> webElements,
      WebComponentFactory webComponentFactory, WebDriver driver) {
    this.webComponentClass = webComponentClass;
    this.wrappedWebElements = webElements;
    this.webComponentFactory = webComponentFactory;
    this.driver = driver;
  }

  public WebComponentList(Class<T> webComponentClass, List<WebElement> webElements,
      WebComponentFactory webComponentFactory, WebDriver driver,
      ParameterizedType genericTypeArguments) {
    this.webComponentClass = webComponentClass;
    this.wrappedWebElements = webElements;
    this.webComponentFactory = webComponentFactory;
    this.driver = driver;
    this.genericTypeArguments = genericTypeArguments;
  }

  public void createWebComponents() {
    webComponents = new ArrayList<>();
    for (WebElement webElement : wrappedWebElements) {
      try {
        // Create web component and add it to list
        T webComponent = webComponentFactory.create(webComponentClass, webElement, driver);
        PageFactory.initElements(
            new WebDriverExtensionFieldDecorator(webElement, driver, genericTypeArguments),
            webComponent);
        webComponents.add(webComponent);
      } catch (Exception e) {
        throw new WebDriverExtensionException(e);
      }
    }
  }

  @Override
  public int size() {
    createWebComponents();
    return webComponents.size();
  }

  @Override
  public boolean isEmpty() {
    createWebComponents();
    return webComponents.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    createWebComponents();
    return webComponents.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    createWebComponents();
    return webComponents.iterator();
  }

  @Override
  public Object[] toArray() {
    createWebComponents();
    return webComponents.toArray();
  }

  @Override
  public <T> T[] toArray(T[] ts) {
    createWebComponents();
    return webComponents.toArray(ts);
  }

  @Override
  public boolean add(T e) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public boolean containsAll(Collection<?> clctn) {
    createWebComponents();
    return webComponents.containsAll(clctn);
  }

  @Override
  public boolean addAll(Collection<? extends T> clctn) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public boolean addAll(int i, Collection<? extends T> clctn) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public boolean removeAll(Collection<?> clctn) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public boolean retainAll(Collection<?> clctn) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public T get(int i) {
    createWebComponents();
    return webComponents.get(i);
  }

  @Override
  public T set(int i, T e) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public void add(int i, T e) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public T remove(int i) {
    throw new UnsupportedOperationException(
        "This collection is imnmutable and therefore this method cannot be called.");
  }

  @Override
  public int indexOf(Object o) {
    createWebComponents();
    return webComponents.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    createWebComponents();
    return webComponents.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    createWebComponents();
    return webComponents.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int i) {
    createWebComponents();
    return webComponents.listIterator();
  }

  @Override
  public List<T> subList(int i, int i1) {
    createWebComponents();
    return webComponents.subList(i, i1);
  }
}
