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

// Adhering to Open-Closed Principle

// PaymentGateway interface
interface PaymentGateway {
    fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean
}

// StripePaymentGateway implementation
class StripePaymentGateway : PaymentGateway {
    override fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean {
        // Logic to process payment using Stripe
        println("Processing payment using Stripe for ${customer.name}'s order.")
        // Actual payment processing logic with Stripe API would go here
        return true
    }
}

// PaypalPaymentGateway implementation
class PaypalPaymentGateway : PaymentGateway {
    override fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean {
        // Logic to process payment using Paypal
        println("Processing payment using Paypal for ${customer.name}'s order.")
        // Actual payment processing logic with Paypal API would go here
        return true
    }
}

// Refactored CheckoutService
class CheckoutService(private val paymentGateway: PaymentGateway) {
    fun processOrderPayment(customer: Customer, shoppingCart: ShoppingCart) {
        val paymentResult = paymentGateway.processPayment(customer, shoppingCart)

        if (paymentResult) {
            println("Payment successful. Order for ${customer.name} has been processed.")
        } else {
            println("Payment failed for order of ${customer.name}.")
        }
    }
}

// Enum representing different payment gateways
enum class PaymentGatewayType {
    STRIPE,
    PAYPAL
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

    // Choosing the payment gateway type (STRIPE or PAYPAL)
    val selectedGateway = PaymentGatewayType.PAYPAL

    // Creating an instance of the selected payment gateway
    val paymentGateway = when (selectedGateway) {
        PaymentGatewayType.STRIPE -> StripePaymentGateway()
        PaymentGatewayType.PAYPAL -> PaypalPaymentGateway()
    }

    // Using the CheckoutService with the selected payment gateway
    val checkoutService = CheckoutService(paymentGateway)
    checkoutService.processOrderPayment(customer, shoppingCart)
}