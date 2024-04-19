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
  fun getTotalItemCost(): Double {
    return product.price * quantity
  }

  fun getQuantity(): Int {
    return quantity
  }
}

class Order() {
  // The order items live and die with the Order class.
  // Example of Composition
  private val orderItems: MutableList<OrderItem> = mutableListOf()

  fun addOrderItem(orderItem: OrderItem) {
    orderItems.add(orderItem)
  }

  fun getTotalCost(): Double {
    return orderItems.sumOf { it.getTotalItemCost() }
  }

  fun getTotalNumberOfItems(): Int {
    return orderItems.sumOf { it.getQuantity() }
  }
}

fun main() {
  val orders = mutableListOf<Order>()

  // Create two products
  val helmet = Product("Helmet", 1200.0)
  val skatingShoe = Product("Smartphone", 800.0)
  val ankleProtector = Product("AnkleProtector", 55.0)

  // Existing order containing two order items
  val order1 = Order()
  order1.addOrderItem(OrderItem(helmet, 1))
  order1.addOrderItem(OrderItem(skatingShoe, 2))
  orders.add(order1)

  // TODO: Create new order, add ankleProtector order item and add the order item to your total list of orders

  // TODO: Terminate order2

  println("- You have ${orders.size} order(s), containing ${orders.sumOf { it.getTotalNumberOfItems() }} items, at the cost of \$${orders.sumOf { it.getTotalCost() }}")
}
