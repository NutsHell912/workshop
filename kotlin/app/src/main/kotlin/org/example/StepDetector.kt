package org.example

import org.example.models.AccelSample
import org.example.utils.TestUtils
import kotlin.math.sqrt

class StepDetector {
    /**
     * Метод определения шагов
     *
     * @param samples список измерений акселерометра
     * @return количество обнаруженных шагов
     */
    fun detect(samples: List<AccelSample>): Int {
        val resultForce = len(samples)
        var isFindMax = true
        var stepCounter = 0
        val threshold = 0.8
        for (i in 0 until resultForce.size - 5) {
            val element = resultForce[i]
            val windows = mutableListOf<Double>()
            for(j in i+1 .. i + 5) {
                windows.add(resultForce[j])
            }
            if(isFindMax) {
                if(windows.none {
                    it > element
                } && element > threshold) {
                    isFindMax = false
                    stepCounter++
                } else {
                    continue
                }
            } else {
                if(windows.none {
                        it < element
                    } && element < -threshold) {
                    isFindMax = true
                } else {
                    continue
                }
            }
        }

        return stepCounter;
    }

    fun len(sample: List<AccelSample>): List<Double> {
        return sample.map {
            sqrt(it.x * it.x + it.y * it.y + it.z * it.z) - 9.8
        }
    }
}

fun main() {
    val detector = StepDetector()
    TestUtils.runTests(detector::detect)
}

