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

class ShoppingCart {
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
    println("Cancelling Order ...")
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

// TODO: Create Customer Class

fun main() {
  // TODO: Create customer object and link with the order

  // Create two products
  val product1 = Product("Laptop", 1200.0)
  val product2 = Product("Smartphone", 800.0)

  // Create new order
  val shoppingCart = ShoppingCart()

  // Add new order items using with the products and quantity,
  shoppingCart.addLineItem(OrderItem(product1, 2))
  shoppingCart.addLineItem(OrderItem(product2, 1))

  // Show order details. It will show order items.
  shoppingCart.show()

  // Delete order
  shoppingCart.cancel()

  // Show order details with OrderItem's
  shoppingCart.show()
}