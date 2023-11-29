//package com.geekplus.common.util.facedetect;
//
///**
// * author     : geekplus
// * email      : geekcjj@gmail.com
// * date       : 11/24/23 09:29
// * description: 做什么的？
// */
//public class FaceDetectUtils {
//
//    public static void main(String[] args) {
//        // 加载OpenCV库
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//        // 加载人脸检测器
//        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
//
//        // 加载输入图像
//        Mat image = Imgcodecs.imread("input.jpg");
//
//        // 将图像转为灰度图
//        Mat grayImage = new Mat();
//        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
//
//        // 进行人脸检测
//        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(grayImage, faceDetections);
//
//        // 绘制人脸框并显示
//        for (Rect rect : faceDetections.toArray()) {
//            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
//        }
//
//        // 保存结果图像
//        Imgcodecs.imwrite("output.jpg", image);
//    }
//}
