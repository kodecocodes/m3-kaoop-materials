class Customer(val name: String) : ProductAvailabilityListener {
  override fun onAvailabilityChanged(product: Product) {
    // Notify the customer about product availability change
    println("Availability Change: Notify customer or remove from his wish list")
  }
}

class Product(val name: String, val price: Double) {
  private val listeners = mutableListOf<ProductAvailabilityListener>()

  fun setAvailability(newAvailability: Boolean) {
    listeners.forEach { it.onAvailabilityChanged(this) }
  }

  fun addAvailabilityListener(listener: ProductAvailabilityListener) {
    listeners.add(listener)
  }
}


class OrderItem(private val product: Product, private val quantity: Int) {
  fun getLineItemPrice(): Double {
    return product.price * quantity
  }

  fun show() {
    println("- Line Item \n Name: ${product.name}, quantity: $quantity, price: ${getLineItemPrice()}")
  }
}

// Observer interface
// Interface for listeners
interface ProductAvailabilityListener {
  fun onAvailabilityChanged(product: Product)
}


// The private constructor prevents unauthorized instantiation of the class
// Subject (Observable)
class ShoppingCart private constructor() : ProductAvailabilityListener {
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

  private fun getTotalOrderPrice(): Double {
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

  override fun onAvailabilityChanged(product: Product) {
    // Handle product availability change in the cart
    println("Availability Change: Update Cart to reflect it")
  }
}

fun main() {
  // Create a customer for which we will create orders
  val customer = Customer("Elon Musk")

  // Create product
  val product = Product("Laptop", 1200.0)

  // Create a shopping cart to add items
  val shoppingCart: ShoppingCart = ShoppingCart.getInstance()
  // Add new order items using with the products and quantity,
  shoppingCart.addLineItem(OrderItem(product, 2))
  // Show order details. It will show order items.
  shoppingCart.show()

  product.addAvailabilityListener(shoppingCart)
  product.addAvailabilityListener(customer)
  product.setAvailability(false)
}