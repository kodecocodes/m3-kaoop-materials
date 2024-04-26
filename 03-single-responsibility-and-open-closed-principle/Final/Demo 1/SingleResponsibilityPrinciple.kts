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
}

class ShoppingCart private constructor() {
  private val orderItems: MutableList<OrderItem> = mutableListOf()

  companion object {
    val instance: ShoppingCart by lazy {
      ShoppingCart()
    }
  }

  fun addOrderItem(orderItem: OrderItem) {
    orderItems.add(orderItem)
  }

  fun getTotalOrderPrice(): Double {
    return orderItems.sumOf { it.getTotalItemCost() }
  }

  fun show() {
    println("- You have ${orderItems.size} order items, at the cost of \$${orderItems.sumOf { it.getTotalItemCost() }}")
  }
}

// TODO: Fix this class breaking the SRP principle

interface NotificationService {
  fun sendConfirmationEmail()
}

interface PaymentService {
  fun processPayment(shoppingCart: ShoppingCart): Boolean
}

interface InvoiceService {
  fun generateInvoice(shoppingCart: ShoppingCart)
}

interface LoggingService {
  fun logOrderActivity(activity: String)
}

class EmailNotificationService : NotificationService {
  override fun sendConfirmationEmail() {
    // Logic to send confirmation email
    println("Email sent: Order confirmed.")
  }
}

class CreditCardPaymentService : PaymentService {
  override fun processPayment(shoppingCart: ShoppingCart): Boolean {
    // Logic to process credit card payment
    println(
      "Credit card payment was processed with a total amount of $${
        shoppingCart.getTotalOrderPrice()
      }."
    )
    return true
  }
}

class InvoiceGenerationService : InvoiceService {
  override fun generateInvoice(shoppingCart: ShoppingCart) {
    // Logic to generate invoice
    println("Invoice generated...")
  }
}

class ConsoleLoggingService : LoggingService {
  override fun logOrderActivity(activity: String) {
    // Logic to log order activity to console
    println(activity)
  }
}

// The OrderService class is responsible for multiple tasks: creating orders, processing payments, generating invoices, and sending confirmation emails.
// The class has multiple reasons to change, making it harder to maintain and test.
// It violates the Single Responsibility Principle, leading to tight coupling and decreased flexibility.
class OrderService(
  private val notificationService: NotificationService,
  private val paymentService: PaymentService,
  private val invoiceService: InvoiceService,
  private val loggingService: LoggingService
) {
  fun createOrder(shoppingCart: ShoppingCart) {
    // Business logic for creating an order
    loggingService.logOrderActivity("Order created.")

    // Payment processing
    if (paymentService.processPayment(shoppingCart)) {
      loggingService.logOrderActivity("Payment successful.")

      // Invoice generation
      invoiceService.generateInvoice(shoppingCart)

      // Notification
      notificationService.sendConfirmationEmail()
    } else {
      loggingService.logOrderActivity("Payment failed.")
    }
  }
}

fun main() {
  // Create products
  val product1 = Product("Laptop", 1200.0)
  val product2 = Product("Smartphone", 800.0)

  // Create a shopping cart to add items
  val shoppingCart: ShoppingCart = ShoppingCart.instance
  shoppingCart.addOrderItem(OrderItem(product1, 1))
  shoppingCart.addOrderItem(OrderItem(product2, 1))
  shoppingCart.show()

  // Creating instances of individual services
  val emailNotificationService: NotificationService = EmailNotificationService()
  val creditCardPaymentService: PaymentService = CreditCardPaymentService()
  val invoiceGenerationService: InvoiceService = InvoiceGenerationService()
  val consoleLoggingService: LoggingService = ConsoleLoggingService()

  // Creating an OrderService with the individual services
  val orderService = OrderService(
    emailNotificationService,
    creditCardPaymentService,
    invoiceGenerationService,
    consoleLoggingService
  )
  orderService.createOrder(shoppingCart)
}
