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

class Customer(val name: String) {
  // Customer details
}

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

// The private constructor prevents unauthorized instantiation of the class
class ShoppingCart private constructor() {
  private val orderItems: MutableList<OrderItem> = mutableListOf()

  // Companion object to hold the singleton instance
  companion object {
    // Keyword to make sure multiple threads read the most updated value of it
    @Volatile
    private var instance: ShoppingCart? = null

    // Function to get the singleton instance in thread safe way
    fun getInstance(): ShoppingCart {
      // Check that instance is initialized (without obtaining the lock, which is expensive).
      // If it is initialized, return it immediately.
      if (instance == null) {
        // If no initialized, obtain the lock.
        synchronized(this) {
          // You need to double-check if the instance has already been initialized again, since
          // if another thread acquired the lock first, it may have already done the initialization.
          if (instance == null) {
            // Initialize the instance
            instance = ShoppingCart()
          }
        }
      }
      return instance as ShoppingCart
    }
  }

  fun addLineItem(orderItem: OrderItem) {
    orderItems.add(orderItem)
  }

  fun getTotalOrderPrice(): Double {
    return orderItems.sumOf { it.getLineItemPrice() }
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

// Interface for abstracting order creation
interface OrderRepository {
  fun createOrder(shoppingCart: ShoppingCart): Boolean
}

// Concrete implementation using a database
class DatabaseOrderRepository : OrderRepository {
  override fun createOrder(shoppingCart: ShoppingCart): Boolean {
    // Access information from the entire cart
    // Simulate database interaction and create an order
    println("Create order for cart with total of ${shoppingCart.getTotalOrderPrice()}")
    return true // success
  }
}

// Service injects the repository
// The OrderService class now depends on the OrderRepository interface, not a specific database implementation.
// This allows you to easily change the underlying database logic by providing a different OrderRepository implementation.
// You can also mock OrderRepository in tests to isolate OrderService's behavior from the database.
// The code is more modular, testable, and flexible as a result of following DIP.
class OrderService(private val orderRepository: OrderRepository) {
  fun processOrder(shoppingCart: ShoppingCart) {
    println("Processing order...")
    orderRepository.createOrder(shoppingCart)
  }
}


fun main() {
  // Create a customer for which you will create orders
  val customer = Customer("Elon Musk")

  // Create two products
  val product1 = Product("Laptop", 1200.0)
  val product2 = Product("Smartphone", 800.0)

  // Create a shopping cart to add items
  val shoppingCart: ShoppingCart = ShoppingCart.getInstance()
  // Add new order items with products and quantity
  shoppingCart.addLineItem(OrderItem(product1, 2))
  // Show order details. It will show order items.
  shoppingCart.show()

  val orderProcessor = OrderService(DatabaseOrderRepository())
  orderProcessor.processOrder(shoppingCart)
}