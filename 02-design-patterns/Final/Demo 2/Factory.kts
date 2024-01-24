
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

// PaymentProcessor interface
interface PaymentProcessor {
    fun processPayment(amount: Double)
}

// Concrete implementation of PaymentProcessor
class CreditCardProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing credit card payment of $$amount.")
        // Additional logic specific to credit card processing
    }
}

// Another concrete implementation of PaymentProcessor
class PayPalProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing PayPal payment of $$amount.")
        // Additional logic specific to PayPal processing
    }
}

// PaymentProcessorFactory interface
interface PaymentProcessorFactory {
    fun createPaymentProcessor(): PaymentProcessor
}

// Concrete implementation of PaymentProcessorFactory for Credit Card
class CreditCardProcessorFactory : PaymentProcessorFactory {
    override fun createPaymentProcessor(): PaymentProcessor {
        return CreditCardProcessor()
    }
}

// Concrete implementation of PaymentProcessorFactory for PayPal
class PayPalProcessorFactory : PaymentProcessorFactory {
    override fun createPaymentProcessor(): PaymentProcessor {
        return PayPalProcessor()
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
    shoppingCart.addLineItem(OrderItem(product2, 1))
    // Show order details. It will show order items.
    shoppingCart.show()

    val customerPayingWithCC = false
    // Customer wants to use CC
    val paymentProcessorFactory: PaymentProcessorFactory
    if (customerPayingWithCC) {
        paymentProcessorFactory = CreditCardProcessorFactory()
    } else {
        paymentProcessorFactory = PayPalProcessorFactory()
    }
    val paymentProcessor: PaymentProcessor = paymentProcessorFactory.createPaymentProcessor()
    paymentProcessor.processPayment(shoppingCart.getTotalOrderPrice())
}