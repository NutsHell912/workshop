package org.example.utils

import org.example.models.AccelSample
import org.json.JSONObject
import java.io.File
import kotlin.math.abs
import kotlin.math.max

/**
 * Вспомогательные инструменты для тестирования
 */
object TestUtils {

  /**
   * Проверить результат
   */
  fun runTests(detect: (List<AccelSample>) -> Int) {
    runTest(
      "Движение устройства параллельно полу (50 шагов)",
      detect,
      "flat_50.txt",
      50
    )

    runTest(
      "Движение устройства параллельно полу (100 шагов)",
      detect,
      "flat_100.txt",
      100
    )

    runTest(
      "Движение с устройством в кармане (50 шагов)",
      detect,
      "pocket_50.txt",
      50
    )

    runTest(
      "Движение с устройством в кармане (100 шагов)",
      detect,
      "pocket_100.txt",
      100
    )

    runTest(
      "Ходьба с минимальным движением устройства (50 шагов)",
      detect,
      "hard_50.txt",
      50
    )

    runTest(
      "Ходьба с минимальным движением устройства (100 шагов)",
      detect,
      "hard_100.txt",
      100
    )

    runTest(
      "Ходьба с максимальным движением устройства (50 шагов)",
      detect,
      "intensive_50.txt",
      50
    )

    runTest(
      "Ходьба с максимальным движением устройства (100 шагов)",
      detect,
      "intensive_100.txt",
      100
    )

    runTest(
      "Движение по лестнице с устройством в руке (50 шагов)",
      detect,
      "stairs_50.txt",
      50
    )

    runTest(
      "Покачивание устройства в руке",
      detect,
      "swaying.txt",
      0
    )

    runTest(
      "Движение вверх-вниз без перемещения",
      detect,
      "up_down.txt",
      0
    )

    runTest(
      "Вращение устройства",
      detect,
      "rotation.txt",
      0
    )
  }

  /**
   * Проверить результат по конкретному набору данных
   */
  private fun runTest(
    name: String,
    detect: (List<AccelSample>) -> Int,
    filePath: String,
    expectedSteps: Int
  ) {
    val samples = loadFile(filePath)
    val predicted = detect(samples)
    val acc = accuracy(predicted, expectedSteps)

    println("--- $name ---")
    println("Фактическое количество шагов: $expectedSteps")
    println("Предполагаемое количество шагов: $predicted")
    println("Точность: %.2f%%".format(acc))
    println()
  }

  /**
   * Загрузка данных акселерометра из файла
   */
  private fun loadFile(path: String): List<AccelSample> {
    val stream = object {}.javaClass.getResourceAsStream("/$path")
      ?: error("File not found: $path")

    return stream.bufferedReader().readLines().map { line ->
      val json = JSONObject(line)

      AccelSample(
        x = json.getDouble("x"),
        y = json.getDouble("y"),
        z = json.getDouble("z"),
        timeStamp = json.getLong("t")
      )
    }
  }

  /**
   * Подсчёт точности вычислений
   */
  private fun accuracy(predicted: Int, actual: Int): Double {
    if (actual == 0) {
      return if (predicted == 0) 100.0 else 0.0
    }

    return max(
      0.0,
      (1 - abs(predicted - actual).toDouble() / actual) * 100
    )
  }
}