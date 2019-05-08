package com.mobile.ibandlalakwamarwa

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class HomeScreenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        nthPrime(5)
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    fun nthPrime(n: Int) {
        var candidate: Int
        var count: Int
        candidate = 2
        count = 0
        while (count < n) {
            if (isPrime(candidate)) {
                ++count
            }
            ++candidate
        }
        // The candidate has been incremented once after the count reached n

        print("----------------------------Prime Number\n\nis$candidate - 1")
//        return candidate - 1
    }

    private fun calculatePrime(n: Int) {
        var count = 1

        var primeNumber = 2

        while (count < n) {
            val temp = primeNumber + 1

            if (isPrime(primeNumber)) {
                count++
                primeNumber = temp
                print("----------------------------Prime Number\n\nis$temp")
            }
        }

//        print("----------------------------Prime Number\n\nis$primeNumber")

    }

    private fun isPrime(primeNumber: Int): Boolean {
        for (i in 2..primeNumber / 2) {
            if (primeNumber % i == 0) {
                return false
            }
        }
        return true
    }
}