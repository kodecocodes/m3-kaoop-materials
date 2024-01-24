
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
    fun show (){
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

    fun addLineItem (orderItem: OrderItem) {
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
        orderItems.forEach {orderItem: OrderItem ->
            orderItem.show()
        }
    }
}

// TODO: Fix this class breaking the SRP principle.

// The OrderService class is responsible for multiple tasks: creating orders, processing payments, generating invoices, and sending confirmation emails.
// The class has multiple reasons to change, making it harder to maintain and test.
// It violates the Single Responsibility Principle, leading to tight coupling and decreased flexibility.
class OrderService {
    fun createOrder(shoppingCart: ShoppingCart) {
        // Business logic for creating an order
        println("Order created.")

        // Payment processing
        if (processPayment(shoppingCart.getTotalOrderPrice())) {
            println("Payment successful.")

            // Invoice generation
            generateInvoice()

            // Notification
            sendConfirmationEmail()
        } else {
            println("Payment failed.")
        }
    }

    private fun processPayment(amount: Double): Boolean {
        // Logic to process credit card payment
        println("Credit card payment processed with amount $$amount.")
        return true
    }

    private fun generateInvoice() {
        // Logic to generate invoice
        println("Invoice generated...")
    }

    private fun sendConfirmationEmail() {
        // Logic to send confirmation email
        println("Email sent: Order confirmed.")
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

    val orderService = OrderService()
    orderService.createOrder(shoppingCart)
}