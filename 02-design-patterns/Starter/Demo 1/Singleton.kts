class Product(val name: String, val price: Double) {
    // Product details
}

class OrderItem(private val product: Product, private val quantity: Int) {
    fun getLineItemPrice(): Double {
        return product.price * quantity
    }
    fun show (){
        println("- Line Item \n Name: ${product.name}, quantity: $quantity, price: ${getLineItemPrice()}")
    }
}

// TODO: Convert this to a Singleton class
class ShoppingCart () {
    // The order items live and die with the Order class.
    // Example of Composition
    val orderItems: MutableList<OrderItem> = mutableListOf()

    fun addLineItem (orderItem: OrderItem) {
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
        orderItems.forEach {orderItem: OrderItem ->
            orderItem.show()
        }
    }
}

fun main() {
    // Create two products
    val product1 = Product("Laptop", 1200.0)
    val product2 = Product("Smartphone", 800.0)

    // Create a shopping cart to add items
    var shoppingCart: ShoppingCart? = ShoppingCart()
    println("Created an instance of cart...")

    // Add new order items using with the products and quantity,
    shoppingCart?.addLineItem(OrderItem(product1, 2))
    shoppingCart?.addLineItem(OrderItem(product2, 1))

    // Show order details. It will show order items.
    shoppingCart?.show()

    // TODO: Create another cart instance, and see the effect. Does it create multiple carts?
}