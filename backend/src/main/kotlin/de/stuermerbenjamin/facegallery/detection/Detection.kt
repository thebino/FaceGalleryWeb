package de.stuermerbenjamin.facegallery.detection

/*
 * Copyright (C) 2020 Benjamin Stürmer.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation version 3 of the
 * License, or any later version.
 *
 *      https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 */

import de.stuermerbenjamin.facegallery.shared.models.Image
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.math.ceil

typealias RGB<T> = Array<T>

/**
 * Detect faces on images using a Multi-task Cascaded Convolutional Network.
 * MTCNN contains three convolutional networks to archive this.
 *
 * PNet to proposes candidate facial regions
 * RNet for filtering the bounding boxes
 * ONet to propose facial landmarks
 *
 * @param modelFilePath File path to the Machine learning model
 */
class Detection(private val modelFilePath: String) {
    private var graph: Graph
    private var session: Session

    init {
        val file = File(modelFilePath)
        val fileExists = file.exists()

        if (!fileExists) {
            throw RuntimeException("TensorFlow Model does not exist at $modelFilePath")
        }

        val graphDef = Files.readAllBytes(Paths.get(modelFilePath))
        graph = Graph()
        graph.importGraphDef(graphDef)

        session = Session(graph)
    }

    /**
     * Detect faces on given image
     *
     * @param image: image to analyse
     *
     * @return List of boxes where faces have been detected
     */
    fun detect(imagesDir: String, image: Image): List<FaceBox> {
        val imagePath = File(File(imagesDir).canonicalPath, image.imagePath)
        println("[${image.imagePath}] path: ${imagePath.canonicalPath}")

        //
        // BufferedImage
        //
        val img = ImageIO.read(File(imagePath.canonicalPath))
        println("bufferedImage: ${img.height} / ${img.width}")

        val inputList = listOf(img.convertToGrayscale())
        println("inputList: ${inputList.size}")

        val input = inputList.map {
            it.map { list ->
                list.map { argb ->
                    arrayOf(argb.r, argb.b, argb.g)
                }.toTypedArray()
            }.toTypedArray()
        }.toTypedArray()

        val result = session.runner()
            .feed("pnet/input", Tensor.create(input))
            .fetch("pnet/conv4-2/BiasAdd")
            .fetch("pnet/prob1")
            .run()[0]
        println("result: $result")

        println("DataType: " + result.dataType().name)
        println("NumElements: " + result.numElements())
        println("NumDimensions: " + result.numDimensions())

        val copy = Array(inputList.size) {
            Array(495) {
                Array(950) {
                    FloatArray(4)
                }
            }
        }


        result.copyTo(copy)
        println(copy.size)

        for (arrayOfArrays in copy) {
            for (arrayOfArray in arrayOfArrays) {
                var string = ""
                for (floats in arrayOfArray) {
                    for (float in floats) {
                            string += "$float ,"
                    }
                    string += "\n"
                }
                println("arrayOfArray: $string")
            }
        }


        //println("inputList.size: ${inputList.size}")
        //val outputArr = Array(input.size) { FloatArray(7) }
//        val outputArr = arrayOf<Array<Array<Array<Float>>>>()

//        result.copyTo(outputArr)
//        println("result: $outputArr")

//        result.close()





        //
        // OpenCV::Mat
        //
        val alpha = 0.0078125f
        val mean = 127.5f

        // tmp working Matrix
        var workingImg = Mat()

        // read file into Matrix
        val frame = Imgcodecs.imread(imagePath.canonicalPath)

        // convert float to scale
        frame.convertTo(workingImg, CvType.CV_32FC3)

        // TODO:
        //convertToGrayScale()
//
//        workingImg = (workingImg - mean) * alpha
//        workingImg = workingImg.t()
//        Imgproc.cvtColor(workingImg, workingImg, Imgproc.COLOR_BGR2RGB)
//
//        val img_h = workingImg.rows()
//        val img_w = workingImg.cols()
//        println("[${image.imagePath}] size: $img_w / $img_h")



//        val totalPnetBoxes = arrayListOf<FaceBox>()
//        val totalRnetBoxes = arrayListOf<FaceBox>()
//        val totalOnetBoxes = arrayListOf<FaceBox>()
//
//        val min_size = 40.0
//        val factor = 0.709
//        val win_list = calculateImagePyramid(img_h, img_w, min_size, factor)
//        println("[${image.imagePath}] scaled images: ${win_list.size}")
//
//        val pnet_threshold = 0.6
//        val rnet_threshold = 0.7
//        val onet_threshold = 0.9
//        for (scaleWindow in win_list) {
//            totalPnetBoxes.addAll(run_PNet(workingImg, scaleWindow))
//        }

//        run_RNet()

//        run_ONet()

        //calculate the landmark

        //Get Final Result

        return emptyList()
    }

    /**
     * create an image pyramid of multiple scaled image copies to search for different sized faces within the image
     */
    private fun calculateImagePyramid(height: Int, width: Int, minSize: Double, factor: Double): List<ScaleWindow> {
        var min_side = Math.min(height, width).toDouble()
        val m = 12.0 / minSize

        min_side *= m
        var cur_scale = 1.0

        var scale: Double

        val list = arrayListOf<ScaleWindow>()
        while (min_side >= 12) {
            scale = m * cur_scale
            cur_scale *= factor
            min_side *= factor

            val hs = ceil(height * scale)
            val ws = ceil(width * scale)

            list.add(ScaleWindow(h = hs, w = ws, scale = scale))
        }

        return list
    }

    /**
     * Proposal Network = proposes candidate facial regions
     */
    private fun run_PNet(workingImage: Mat, scaleWindow: ScaleWindow): List<FaceBox> {
        val channels = workingImage.channels()
        val rows = workingImage.rows()
        val cols = workingImage.cols()
        val byteLenght = channels + rows + cols
        val data = arrayListOf<ARGB>()

        for (i in 0 until byteLenght) {
            data.add(ARGB( 1.0f, 0.0f, 0.0f, 0.0f))
        }

        var p_count = 0
        var pixel = DoubleArray(3)
        for (rowI in 0 until workingImage.rows()) {
            for (colK in 0 until workingImage.cols()) {
                pixel = workingImage.get(rowI, colK).clone()
                ARGB(pixel[0], pixel[1], pixel[2])
//                data[p_count] =                                                 ((pixel[0] - 127.5) * 0.0078125).toFloat()
//                data[p_count +     workingImage.rows() * workingImage.cols()] = ((pixel[1] - 127.5) * 0.0078125).toFloat()
//                data[p_count + 2 * workingImage.rows() * workingImage.cols()] = ((pixel[2] - 127.5) * 0.0078125).toFloat()
                p_count++
            }
        }



        val resized = Mat()

        Imgproc.resize(workingImage, resized, Size(scaleWindow.w, scaleWindow.h))

        return emptyList()
    }

    /**
     * Refine Network = filters the bounding boxes
     */
    private fun run_RNet(): List<FaceBox> {
        return emptyList()
    }

    /**
     * Output Network = proposes facial landmarks
     */
    private fun run_ONet(): List<FaceBox> {
        return emptyList()
    }
}

fun BufferedImage.convertToGrayscale(): List<List<ARGB>> {
    val rowRgbList = mutableListOf<List<ARGB>>()
    for (h in 0 until height) {
        val rowRgb = mutableListOf<ARGB>()
        for (w in 0 until width) {
            val pixelComponent = getRGB(w, h)

            val alpha = pixelComponent shr 24 and 0xff
            val red = pixelComponent shr 16 and 0xff
            val green = pixelComponent shr 8 and 0xff
            val blue = pixelComponent and 0xff

            val average = (red + green + blue) / 3

            val pixelValue = average / 255.0f
            val rgb = ARGB(alpha.toFloat(), pixelValue, pixelValue, pixelValue)
            rowRgb.add(rgb)
        }
        rowRgbList.add(rowRgb)
    }
    return rowRgbList
}
