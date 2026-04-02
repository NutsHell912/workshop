package org.example

import org.example.models.AccelSample
import org.example.utils.TestUtils
import kotlin.math.sqrt

class StepDetector {

    private fun low_frequency_filter(sample: List<Double>) : List<Double> {
        var result = mutableListOf<Double>()
        for(i in 0..<sample.size) {
            val windows = mutableListOf<Double>()
            for(j in i-10 .. i) {
                if( j >=0) {
                    windows.add(sample[j])
                }
            }
            result.add(windows.average())
        }
        return result
    }
    /**
     * Метод определения шагов
     *
     * @param samples список измерений акселерометра
     * @return количество обнаруженных шагов
     */
    fun detect(samples: List<AccelSample>): Int {
        var resultForce = len(samples)

        // Проверяем вариацию данных - при вращении часто бывает высокая вариация
        val mean = resultForce.average()
        val variance = resultForce.map { (it - mean) * (it - mean) }.average()
        println(variance)
        resultForce = low_frequency_filter(resultForce.map { it - mean })


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
                    //println("max_peak $i $element")
                    stepCounter++
                } else {
                    continue
                }
            } else {
                if(windows.none {
                        it < element
                    }) {
                    //println("min_peak $i $element")

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
            sqrt(it.x * it.x + it.y * it.y + it.z * it.z)
        }
    }
}

fun main() {
    val detector = StepDetector()
    TestUtils.runTests(detector::detect)
}

