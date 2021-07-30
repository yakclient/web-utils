package net.yakclient.web.utils.test

import org.slf4j.LoggerFactory

fun runSpeedTest(iterations: Int = 100, test: () -> Unit) {
    val startTime = System.nanoTime()
    val logger = LoggerFactory.getLogger("SpeedTest")

    logger.info("Started at time: $startTime relative time : ${System.nanoTime() - startTime}")

    var averageTime = 0L

    for (i: Int in 0..iterations) {
        val startIteration = System.nanoTime()
        test()
        val endIteration = System.nanoTime()

        val l = endIteration - startIteration
        logger.info("Iteration : $i completed. Time: $l")

        averageTime = (averageTime + l) / 2
    }

    logger.info("Test finished with $iterations iterations. Average test time: $averageTime")
}