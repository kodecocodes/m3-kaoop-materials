
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

// Service interfaces
interface NotificationService {
    fun sendConfirmationEmail(customer: Customer)
}

interface PaymentService {
    fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean
}

interface InvoiceService {
    fun generateInvoice(customer: Customer, shoppingCart: ShoppingCart)
}

interface LoggingService {
    fun logOrderActivity(customer: Customer, activity: String)
}

// Individual service implementations
// Each service class has a single responsibility, making the code modular and easier to maintain.
class EmailNotificationService : NotificationService {
    override fun sendConfirmationEmail(customer: Customer) {
        // Logic to send confirmation email
        println("Email sent: Order confirmed for ${customer.name}.")
    }
}

class CreditCardPaymentService : PaymentService {
    override fun processPayment(customer: Customer, shoppingCart: ShoppingCart): Boolean {
        // Logic to process credit card payment
        println("Credit card payment processed for ${customer.name}'s order with total amount $${
            shoppingCart.getTotalOrderPrice()
        }.")
        return true
    }
}

class InvoiceGenerationService : InvoiceService {
    override fun generateInvoice(customer: Customer, shoppingCart: ShoppingCart) {
        // Logic to generate invoice
        println("Invoice generated for ${customer.name}'s order.")
    }
}

class ConsoleLoggingService : LoggingService {
    override fun logOrderActivity(customer: Customer, activity: String) {
        // Logic to log order activity to console
        println("${customer.name}: $activity")
    }
}

// OrderManager utilizing the services
//The OrderManager class is no longer burdened with multiple responsibilities, adhering to the Single Responsibility Principle.
// Changes in one area (e.g., payment processing) do not affect other areas (e.g., notification), providing better separation of concerns.
class OrderManager(
        private val notificationService: NotificationService,
        private val paymentService: PaymentService,
        private val invoiceService: InvoiceService,
        private val loggingService: LoggingService
) {
    fun createOrder(customer: Customer, shoppingCart: ShoppingCart) {
        // Business logic for creating an order
        loggingService.logOrderActivity(customer, "Order created.")

        // Payment processing
        if (paymentService.processPayment(customer, shoppingCart)) {
            loggingService.logOrderActivity(customer, "Payment successful.")

            // Invoice generation
            invoiceService.generateInvoice(customer, shoppingCart)

            // Notification
            notificationService.sendConfirmationEmail(customer)
        } else {
            loggingService.logOrderActivity(customer, "Payment failed.")
        }
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

    // Creating instances of individual services
    val emailNotificationService: NotificationService = EmailNotificationService()
    val creditCardPaymentService: PaymentService = CreditCardPaymentService()
    val invoiceGenerationService: InvoiceService = InvoiceGenerationService()
    val consoleLoggingService: LoggingService = ConsoleLoggingService()

    // Creating an OrderManager with the individual services
    val orderManager = OrderManager(
            emailNotificationService,
            creditCardPaymentService,
            invoiceGenerationService,
            consoleLoggingService
    )

    // Creating an order
    orderManager.createOrder(customer, shoppingCart)
}