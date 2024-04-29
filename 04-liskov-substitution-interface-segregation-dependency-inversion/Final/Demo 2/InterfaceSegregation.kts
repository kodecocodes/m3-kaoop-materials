/*
 * Copyright (c) 2024 Kodeco Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify,
 * merge, publish, distribute, sublicense, create a derivative work,
 * and/or sell copies of the Software in any work that is designed,
 * intended, or marketed for pedagogical or instructional purposes
 * related to programming, coding, application development, or
 * information technology. Permission for such use, copying,
 * modification, merger, publication, distribution, sublicensing,
 * creation of derivative works, or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks
 * that are released under various Open-Source licenses. Use of
 * those libraries and frameworks are governed by their own
 * individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

interface ProductsViewable {
  fun viewProducts()
}

interface CartViewable {
  fun viewCart()
}

interface CartAddable {
  fun addToCart(product: Product)
}

interface PaymentSettingsManageable {
  fun managePaymentSettings()
}

class ParentAccount : ProductsViewable, CartViewable, CartAddable, PaymentSettingsManageable {
  override fun viewProducts() {
    println("Parent account viewing products...")
  }

  override fun addToCart(product: Product) {
    println("Product added to the cart (Parent): ${product.name}")
  }

  override fun viewCart() {
    println("Parent account viewing cart...")
  }

  override fun managePaymentSettings() {
    println("Parent account managePaymentSettings...")
  }
}

class KidsAccount : ProductsViewable, CartViewable, CartAddable {
  override fun viewProducts() {
    println("Kids account viewing products...")
  }

  override fun addToCart(product: Product) {
    println("Product added to the cart (Kids): ${product.name}")
  }

  override fun viewCart() {
    println("Kids account viewing cart...")
  }
}

class Product(val name: String, val price: Double) {
  // Product details
}

fun main() {
  val product1 = Product("Laptop", 1200.0)

  val parentAccount = ParentAccount()
  val kidsAccount = KidsAccount()

  parentAccount.viewCart()
  parentAccount.addToCart(product1)
  parentAccount.viewCart()
  parentAccount.managePaymentSettings()
  println()

  kidsAccount.viewCart()
  kidsAccount.addToCart(product1)
  kidsAccount.viewCart()
}