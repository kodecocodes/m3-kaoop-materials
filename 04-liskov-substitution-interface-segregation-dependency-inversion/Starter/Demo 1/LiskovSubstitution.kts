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
          // We need to double-check if the instance has already been initialized again, since
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

// TODO:
// Without adhering to Liskov Substitution Principle
// We have a generic payment gateway, and want to introduce specific logic for crypto payment gateways
open class GenericPaymentGateway {
  open fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean {
    // Logic to process a generic payment
    println("Generic Processing Logic... ")
    val isSuccessful = true;
    return isSuccessful
  }
}

// TODO: Make it adhere to LSV

// To introduce specific logic for crypto payment gateways.
// This breaks LSP
// The Liskov Substitution Principle states that objects of a superclass should be replaceable with
// objects of a subclass without affecting the correctness of the program.
// In simple terms, if you have a base class and a subclass, you should be able
// to use the subclass wherever the base class is expected, and the program
// should continue to work as expected.

// However we won't be able to replace in this situation, as it would break the logic,
// since we are modifying the logic of generic payment processing. The calling code using the base class
// might not anticipate them. There can be situations where calling code expects a payment to succeed based on the base class's logic,
// but it fails when using CryptoPaymentGateway.
class CryptoPaymentGateway : GenericPaymentGateway() {
  override fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean {
    // Other generic logic for payment processing, validating enough funds etc.
    super.processPayment(customer, shoppingCart)
    // Logic to process crypto specific things
    println("Additional Crypto steps for processing ${customer.name}'s order.")
    val isSuccessful = true;
    return isSuccessful
  }
}

fun main() {
  // Create a customer for which we will create orders
  val customer = Customer("Elon Musk")

  // Create two products
  val product1 = Product("Laptop", 1200.0)
  val product2 = Product("Smartphone", 800.0)

  // Create a shopping cart to add items
  val shoppingCart: ShoppingCart = ShoppingCart.getInstance()
  // Add new order items using with the products and quantity,
  shoppingCart.addLineItem(OrderItem(product1, 2))
  // Show order details. It will show order items.
  shoppingCart.show()

  // TODO:
  //  Can we replace subclass where base class is expected?
  val cryptoPaymentGateway = CryptoPaymentGateway()
  val paymentResult1 = cryptoPaymentGateway.processPayment(customer, shoppingCart)
  println("Payment result: $paymentResult1")

}