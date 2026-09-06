package com.example.core.utils

import java.text.DecimalFormat
import java.util.Locale

object CurrencyFormatter {

    /**
     * Formats number to Indian numbering system with ₹ symbol:
     * e.g. 500000 -> ₹5,00,000
     * e.g. 3275000 -> ₹32,75,000
     * e.g. 10000000 -> ₹1,00,00,000
     */
    fun formatInr(amount: Long, withSymbol: Boolean = true): String {
        val sign = if (amount < 0) "-" else ""
        val absAmount = Math.abs(amount)
        val s = absAmount.toString()
        val len = s.length

        val result = if (len <= 3) {
            s
        } else {
            val lastThree = s.substring(len - 3)
            val rest = s.substring(0, len - 3)
            val sb = StringBuilder()
            var count = 0
            for (i in rest.length - 1 downTo 0) {
                sb.append(rest[i])
                count++
                if (count % 2 == 0 && i != 0) {
                    sb.append(",")
                }
            }
            sb.reverse().append(",").append(lastThree).toString()
        }

        return if (withSymbol) "$sign₹$result" else "$sign$result"
    }

    /**
     * Formats amount to compact Indian units like ₹10L, ₹50L, ₹1Cr
     */
    fun formatCompactInr(amount: Long): String {
        return when {
            amount >= 10000000L -> {
                val cr = amount.toDouble() / 10000000.0
                if (cr % 1.0 == 0.0) "₹${cr.toInt()}Cr" else "₹${DecimalFormat("#.##").format(cr)}Cr"
            }
            amount >= 100000L -> {
                val lakh = amount.toDouble() / 100000.0
                if (lakh % 1.0 == 0.0) "₹${lakh.toInt()}L" else "₹${DecimalFormat("#.##").format(lakh)}L"
            }
            amount >= 1000L -> {
                val k = amount.toDouble() / 1000.0
                if (k % 1.0 == 0.0) "₹${k.toInt()}k" else "₹${DecimalFormat("#.#").format(k)}k"
            }
            else -> formatInr(amount)
        }
    }
}
