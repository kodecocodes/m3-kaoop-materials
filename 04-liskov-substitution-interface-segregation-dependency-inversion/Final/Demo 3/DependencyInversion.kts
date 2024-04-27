/*
 * Copyright (c) 2024 Kodeco Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify,
 * merge, publish, distribute, sublicense, create a derivative work,
 * and/or sell copies of the Software in any work that is designed,
 * intended, or marketed for pedagogical or instructional purposes
 * related to programming, coding, application development, or
 * information technology. Permission for such use, copying,
 * modification, merger, publication, distribution, sublicensing,
 * creation of derivative works, or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks
 * that are released under various Open-Source licenses. Use of
 * those libraries and frameworks are governed by their own
 * individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

class Product(val name: String, val price: Double) {
  // Product details
}

class OrderItem(private val product: Product, private val quantity: Int) {
  fun getLineItemPrice(): Double {
    return product.price * quantity
  }
}

class ShoppingCart private constructor() {
  private val orderItems: MutableList<OrderItem> = mutableListOf()

  companion object {
    val instance: ShoppingCart by lazy {
      ShoppingCart()
    }
  }

  fun addLineItem(orderItem: OrderItem) {
    orderItems.add(orderItem)
  }

  fun getTotalOrderPrice(): Double {
    return orderItems.sumOf { it.getLineItemPrice() }
  }
}

class OrderService(private val orderRepository: OrderRepository) {
  fun processOrder(shoppingCart: ShoppingCart) {
    println("Processing order...")
    orderRepository.saveOrder(shoppingCart)
  }
}

class SqliteOrderRepository : OrderRepository {
  override fun saveOrder(shoppingCart: ShoppingCart): Boolean {
    println("Persist order amounting to ${shoppingCart.getTotalOrderPrice()} to an SQLite Database.")
    return true
  }
}

class InMemoryOrderRepository : OrderRepository {
  override fun saveOrder(shoppingCart: ShoppingCart): Boolean {
    println("Persist order amounting to ${shoppingCart.getTotalOrderPrice()} to an In-Memory Database.")
    return true // success
  }
}

interface OrderRepository {
  fun saveOrder(shoppingCart: ShoppingCart): Boolean
}

fun main() {
  val product1 = Product("Laptop", 1200.0)
  val shoppingCart: ShoppingCart = ShoppingCart.instance
  shoppingCart.addLineItem(OrderItem(product1, 2))

  val orderProcessor = OrderService(InMemoryOrderRepository())
  orderProcessor.processOrder(shoppingCart)
}
