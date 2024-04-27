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

interface IPaymentMode {
  fun validate()

  fun handlePayment()
}

internal abstract class BaseCard : IPaymentMode {
  abstract override fun validate()

  override fun handlePayment(){
    runFraudChecks()
  }

  abstract fun runFraudChecks()
}

internal class CreditCard : BaseCard() {
  override fun validate() {
    println("Validating credit card ...")
  }

  override fun runFraudChecks() {
    println("Running fraud checks on credit card ...")
  }

  override fun handlePayment() {
    super.handlePayment()
    println("Handling credit card payment ...")
  }
}

internal class DebitCard : BaseCard() {
  override fun validate() {
    println("Validating debit card ...")
  }

  override fun runFraudChecks() {
    println("Running fraud checks on debit card ...")
  }

  override fun handlePayment() {
    super.handlePayment()
    println("Handling debit card payment ...")
  }
}

internal class RewardsCard : IPaymentMode {
  override fun validate() {
    println("Validating rewards card ...")
  }

  override fun handlePayment() {
    println("Handling rewards card payment ...")
  }
}

class PaymentProcessor {
  fun process(orderDetails: OrderDetails, paymentMode: IPaymentMode) {
    try {
      paymentMode.validate()
      paymentMode.handlePayment()
      saveToDatabase(orderDetails, paymentMode)
    } catch (e: Exception) {
      // Exception handling with specific exception type
    }
  }

  private fun saveToDatabase(orderDetails: OrderDetails, paymentMode: IPaymentMode) {
    println("Saving payment details to database ...")
  }
}

class OrderDetails {}

fun main() {
  val paymentProcessor = PaymentProcessor()
  val debitCard = DebitCard()
  val creditCard = CreditCard()

  val orderDetails = OrderDetails()
  paymentProcessor.process(orderDetails, debitCard)
  println()
  paymentProcessor.process(orderDetails, creditCard)
  println()
  // TODO: Add new Reward card
  val rewardsCard = RewardsCard()
  paymentProcessor.process(orderDetails, rewardsCard)
}
