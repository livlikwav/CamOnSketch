#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_edge_ImageActivity_detectEdgeJNI(JNIEnv *env, jobject thiz, jlong input_image,
                                                  jlong output_image, jint th1, jint th2) {
    // TODO: implement detectEdgeJNI()
    Mat &inputMat = *(Mat *) input_image;
    Mat &outputMat = *(Mat *) output_image;

    cvtColor(inputMat, outputMat, COLOR_RGB2GRAY);
    Canny(outputMat, outputMat, th1, th2);
    //canny(입력영상, 결과영상, 낮은 경계값, 높은 경계값, Sobel커널크기, false[정교하게 작동여부])
}