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

class Order {
    // The order items live and die with the Order class.
    // Example of Composition
    private val orderItems: MutableList<OrderItem> = mutableListOf()

    fun addLineItem (orderItem: OrderItem) {
        // TODO: Implement Add Items
    }
    private fun getTotalOrderPrice(): Double {
        return orderItems.sumOf { it.getLineItemPrice() }
    }
    fun cancel() {
        println("Cancelling Order ...")
        // TODO: Implement Cancel Order
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

    // TODO: Create new order

    // TODO: Add new order items using with the products and quantity,

    // TODO: Show order details. It will show order items.

    // TODO: Delete order

    // TODO: Show order details with OrderItem's Agian
    // You will notice if there is no order in place, the line items don't exist
    // Composition:
    // OrderItems don't exists outside order object
    // Use composition when parts have no meaningful existence outside the whole
    // and their lifecycles are closely intertwined.

}