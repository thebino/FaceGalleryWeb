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
import org.tensorflow.Operation
import org.tensorflow.Session
import org.tensorflow.Tensor
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
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

        val alpha = 0.0078125f
        val mean = 127.5f

        val frame = Imgcodecs.imread(imagePath.canonicalPath)
        var workingImg = Mat()
        frame.convertTo(workingImg, CvType.CV_32FC3)

        // TODO:
        //convertToGrayScale()
        // workingImg = (workingImg - mean) * alpha
        workingImg = workingImg.t()
        Imgproc.cvtColor(workingImg, workingImg, Imgproc.COLOR_BGR2RGB)

        val img_h = workingImg.rows()
        val img_w = workingImg.cols()
        println("[${image.imagePath}] size: $img_w / $img_h")

        val totalPnetBoxes = arrayListOf<FaceBox>()
        val totalRnetBoxes = arrayListOf<FaceBox>()
        val totalOnetBoxes = arrayListOf<FaceBox>()

        val min_size = 40.0
        val factor = 0.709
        val win_list = calculateImagePyramid(img_h, img_w, min_size, factor)
        println("[${image.imagePath}] scaled images: ${win_list.size}")

        val pnet_threshold = 0.6
        val rnet_threshold = 0.7
        val onet_threshold = 0.9
        for (scaleWindow in win_list) {
            totalPnetBoxes.addAll(run_PNet(workingImg, scaleWindow))
        }

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
            data.add(ARGB(0.0f, 0.0f, 0.0f, 0.0f))
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

        val inputData: Array<Float> = Array(4) { 1f; scaleWindow.w.toFloat(); scaleWindow.h.toFloat(); 3f }

        val images = arrayListOf<List<List<RGB<Float>>>>()
        val input = images.map {
            it.map {
                it.map {
                    arrayOf(0.0f, 0.0f, 0.0f)
                    //arrayOf(it.r, it.b, it.g)
                }
            }.toTypedArray()
        }.toTypedArray()

        val realInputs = Array(input.size) {
                Array(48) {
                    Array(48) {
                        FloatArray(3)
                    }
                }
            }

        for (i in 0 until input.size) {
            for (j in 0..47) {
                for (k in 0..47) {
                    for (l in 0..2) {
                        realInputs[i][j][k][l] = input[i][j][k][l]
                    }
                }
            }
        }

        val runner = session.runner()
        runner.feed("pnet/input", Tensor.create(realInputs))

        val output_names = arrayListOf<Operation>()
        val result = runner
            .fetch("pnet/conv4-2/BiasAdd")
            .fetch("pnet/prob1")
            .run()[0]


        // TODO: adjust outputBuffer to 4-dimensional array
        val outputBuffer = Array(1) { FloatArray(3) }
        result.copyTo(outputBuffer)





//        val input_name = graph.operation("pnet/input")
//        val input_names : ArrayList<Output<Operation>> = arrayListOf()
//        val t = Tensor()
//        val a = graph.opBuilder().addInput(input_name).build()
//        input_names.add(Output(input_name, 0))
//
//
//        val output_names : ArrayList<GraphOperation> = arrayListOf()
//        output_names.add(graph.operation("pnet/conv4-2/BiasAdd"))
//        output_names.add(graph.operation("pnet/prob1"))
//
//        val result = session.runner()
//            .feed("pnet/input", input)
//            .fetch("pnet/conv4-2/BiasAdd")
//            .run()[0]


        // TF_NewTensor(TF_FLOAT, dim, 4, resized.ptr(), sizeof(float) * scale_w * scale_h * 3, dummy_deallocator, nullptr);
        // TF_NewTensor(TF_DataType dtype, tensorflow::int64* dims, int num_dims, void* data, size_t len, void (*deallocator)(void* data, size_t len, void* arg), void* deallocator_arg)
//        val localInferenceInput: Array<Float> = Array(4) {
          //  batch, width, height, features
//            1.0f; scaleWindow.w; scaleWindow.h; 3.0f
//        }
//
//        val floatMat = Mat()
//        workingImage.convertTo(floatMat, CV_32F)
//        val floatBuffer: FloatBuffer = floatMat.createBuffer()

//        val inputData = floatArrayOf(1f, scaleWindow.w.toFloat(), scaleWindow.h.toFloat(), 3f)
//        val input = Tensor.create(inputData)

//        val input  = localInferenceInput.map {
//            it.toLocalInferenceInput()
//        }.toTypedArray()
//
//
//        val realInputs =
//            Array(
//                input.length
//            ) {
//                Array(
//                    48
//                ) { Array(48) { FloatArray(3) } }
//            }
//        for (i in 0 until input.length) {
//            for (j in 0..47) {
//                for (k in 0..47) {
//                    for (l in 0..2) {
//                        realInputs[i][j][k][l] = input.get(i).get(j).get(k).get(l)
//                    }
//                }
//            }
//        }

//        val input1 = Tensor.create(DataType.FLOAT, localInferenceInput)

        // TODO: https://github.com/JsFlo/EmotionRecServer/blob/master/infrastructure/src/main/kotlin/com/emotionrec/localtfinference/LocalInferenceService.kt
        // TODO: https://github.com/JsFlo/EmotionRecServer/blob/master/infrastructure/src/main/java/com/emotionrec/localtfinference/JavaUtils.java
//        val input2 = Tensor.create(0.0f)
//        val result = session.runner()
//            .feed("pnet/input", input)
//            .fetch("pnet/conv4-2/BiasAdd")
//            .run()[0]



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
