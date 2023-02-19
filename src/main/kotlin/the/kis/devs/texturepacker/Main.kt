package the.kis.devs.texturepacker

import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.system.exitProcess

/**
 * `args[0]` - directory to images need to be packed
 *
 * `args[1]` - output image
 * */
fun main(
    args : Array<String>
) {
    fun exit(
        reason : String
    ) {
        println("$reason\nUsage: directory_to_images_need_to_be_packed output_image\nIf you need more help, create issue on https://github.com/TheKisDevs/Texture-Packer")
        exitProcess(0)
    }

    if(args.size != 2) {
        exit("Not enough arguments!")
    }

    val input = args[0]
    val output = args[1]

    val inputFile = File(input)
    val outputFile = File(output)

    if(!inputFile.exists()) {
        exit("Input directory does not exist!")
    }

    if(!inputFile.isDirectory) {
        exit("Input directory is file!")
    }

    if(outputFile.exists()) {
        println("Output file will be overwritten")

        outputFile.delete()
    }

    outputFile.createNewFile()

    val images = mutableListOf<BufferedImage>()

    for(file in inputFile.listFiles()!!) {
        if(file.name.endsWith(".png")) {
            images.add(ImageIO.read(file))
        }
    }

    val widths = mutableSetOf<Int>()
    val heights = mutableSetOf<Int>()

    for(image in images) {
        widths.add(image.width)
        heights.add(image.height)
    }

    if(widths.size > 1 || heights.size > 1) {
        exit("Input files dont have semi size!")
    }

    val width = widths.first()
    val height = heights.first()

    val count = inputFile.listFiles()!!.size

    val outputWidth = width
    val outputHeight = count * height

    val outputImage = BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB)

    for((i, image) in images.withIndex()) {
        println("Buffering ${i + 1} image!")

        for(x in 0 until width) {
            for (y in 0 until height) {
                outputImage.setRGB(x, y + i * height, image.getRGB(x, y))
            }
        }
    }

    println("Writing image!")

    ImageIO.write(outputImage, "png", outputFile)

    println("Done!")
}