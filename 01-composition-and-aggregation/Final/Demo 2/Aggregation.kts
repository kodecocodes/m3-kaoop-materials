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

class ShoppingCart(private val customer: Customer) {
  // The order items live and die with the Order class.
  // Example of Composition
  val orderItems: MutableList<OrderItem> = mutableListOf()

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
}

fun main() {
  // Create a customer for which we will create orders. Later attach it to the Shopping Cart.
  // Aggregation:
  // Take note that this will exist even after order is deleted, i.e outside the lifetime of the order.
  // However this an Order will be attached to a customer.
  val customer = Customer("Elon Musk")

  // Create two products
  val product1 = Product("Laptop", 1200.0)
  val product2 = Product("Smartphone", 800.0)

  // Create new cart
  var shoppingCart: ShoppingCart? = ShoppingCart(customer)
  println("Created an order for customer: ${customer.name}")

  println("Adding items to order")
  // Add new order items using with the products and quantity,
  shoppingCart?.addLineItem(OrderItem(product1, 2))
  shoppingCart?.addLineItem(OrderItem(product2, 1))

  // Show order details. It will show order items.
  shoppingCart?.show()
  println("Number of Line Items: ${shoppingCart?.orderItems?.size}")


  // Delete order
  println("\nCancelling Order ...")
  // Setting Order object ot null, which will be garbage collected and removed from memory
  shoppingCart = null

  // Show order details with OrderItem's
  println("\nCustomer exist: ${customer.name}")
  println("Number of Line Items: ${if (shoppingCart?.orderItems?.size != null) shoppingCart.orderItems.size else 0}")
  println("Does order exist?: ${if (shoppingCart != null) "Yes" else "No"}")
  shoppingCart?.show()
}