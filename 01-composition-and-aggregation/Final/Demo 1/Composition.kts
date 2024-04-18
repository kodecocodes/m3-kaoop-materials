package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

class Product(val name: String, val price: Double) {
    // Product details
}

class OrderItem(private val product: Product, private val quantity: Int) {
    fun getTotalItemCost(): Double {
        return product.price * quantity
    }

    fun getQuantity(): Int {
        return quantity
    }
}

class Order {
    // The order items live and die with the Order class.
    // Example of Composition
    private val orderItems: MutableList<OrderItem> = mutableListOf()

    fun addOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
    }

    fun getTotalCost(): Double {
        return orderItems.sumOf { it.getTotalItemCost() }
    }

    fun getTotalNumberOfItems(): Int {
        return orderItems.sumOf { it.getQuantity() }
    }
}

fun mains() {
    val orders = mutableListOf<Order>()

    // Create two products
    val helmet = Product("Helmet", 1200.0)
    val skatingShoe = Product("Smartphone", 800.0)
    val ankleProtector = Product("AnkleProtector", 55.0)

    // Existing order containing two order items
    val order1 = Order()
    order1.addOrderItem(OrderItem(helmet, 1))
    order1.addOrderItem(OrderItem(skatingShoe, 2))
    orders.add(order1)

    // TODO: Create new order, add ankleProtector order item and add the order item to your total list of orders
    val order2 = Order()
    order2.addOrderItem(OrderItem(ankleProtector, 2))
    orders.add(order2)

    // TODO: Terminate order2
    orders.remove(order2)

    println("- You have ${orders.size} order(s), containing ${orders.sumOf { it.getTotalNumberOfItems() }} items, at the cost of \$${orders.sumOf { it.getTotalCost() }}")
}
