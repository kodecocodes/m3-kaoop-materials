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

  fun show() {
    println("- Line Item \n Name: ${product.name}, quantity: $quantity, price: ${getLineItemPrice()}")
  }
}

class Order {
  // The order items live and die with the Order class.
  // Example of Composition
  private val orderItems: MutableList<OrderItem> = mutableListOf()

  fun addLineItem(orderItem: OrderItem) {
    orderItems.add(orderItem)
  }

  private fun getTotalOrderPrice(): Double {
    return orderItems.sumOf { it.getLineItemPrice() }
  }

  fun cancel() {
    println("Canceling Order ...")
    orderItems.clear()
  }

  fun show() {
    println()
    println("Here are the details of the order:")
    println("Total Price: $${getTotalOrderPrice()}")
    println("Number of Line Items: ${orderItems.size}")
    orderItems.forEach { orderItem: OrderItem ->
      orderItem.show()
    }
  }
}

fun main() {
  // Create two products
  val product1 = Product("Laptop", 1200.0)
  val product2 = Product("Smartphone", 800.0)

  // Create new order
  val order = Order()

  // Add new order items with products and quantity
  order.addLineItem(OrderItem(product1, 2))
  order.addLineItem(OrderItem(product2, 1))

  // Show order details. It will show order items.
  order.show()

  // Delete order
  order.cancel()

  // Show order details with OrderItems
  // You will notice the line items don't exist if there is no order in place
  // Composition:
  // OrderItems don't exist outside order object
  // Use composition when parts have no meaningful existence outside the whole
  // and their lifecycles are closely intertwined.
  order.show()
}