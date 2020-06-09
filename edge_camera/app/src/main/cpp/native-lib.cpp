#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_edge_1camera_ImageActivity_detectEdgeJNI(JNIEnv *env, jobject, jlong input_image, jlong output_image, jint th1, jint th2) {
    // TODO: implement detectEdgeJNI()
    Mat &inputMat = *(Mat *) input_image;
    Mat &outputMat = *(Mat *) output_image;

    cvtColor(inputMat, outputMat, COLOR_RGB2GRAY);
    Canny(outputMat, outputMat, th1, th2);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_edge_1camera_MainActivity_detectEdgeJNI1(JNIEnv *env, jobject thiz,
                                                         jlong input_image, jlong output_image,
                                                         jint th1, jint th2) {
    // TODO: implement detectEdgeJNI()
    Mat &inputMat = *(Mat *) input_image;
    Mat &outputMat = *(Mat *) output_image;

    cvtColor(inputMat, outputMat, COLOR_RGB2GRAY);
    Canny(outputMat, outputMat, th1, th2);

    Canny(outputMat, outputMat, th1, th2, 3, true);
}