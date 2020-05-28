![logo_camonsketch](https://user-images.githubusercontent.com/44190293/82134943-5501e700-9838-11ea-9407-3e0779b36ef1.png)
#  CamOnSketch
2020-01 컴퓨터과학종합설계 프로젝트 : 위치기반 촬영 가이드 어플

  인기 게시물 속 저 사진은 어떻게 찍었을까? SNS에는 매일 해시태그와 함께 유명한 여행지와 명소의 멋진 사진들이 올라온다. 하지만, 동일한 장소에서 동일한 스마트폰 카메라로 찍었는데, 도저히 같은 느낌이 나지 않던 경험이 다들 있을 것이다. 이와같이, 소중한 순간을 더 완벽하게 기록하고 싶은 사람들을 위해 사진 촬영의 어려움을 해소하는 것이 우리의 목표이다.
  
  똑같이 찍고 싶은 사진의 스케치를 카메라 어플 화면 위에 직접 띄워 구도에 대한 가이드를 제시한다. 나아가, 카메라 초점, 밝기 등의 설정값을 제시하거나 또는 직접 변경해 사용자에게 편의를 제공한다.
  
#  Table of Contents

## Topic 1
  1. 다운 후 두 파일을 C:\Users\ {이름} \AndroidStudioProjects 으로 복사합니다.
  2. https://brunch.co.kr/@mystoryg/76#comment 을 보며 ndk와 cmake를 설치합니다.되도록 ndk : 21.1.6352462으로 부탁드립니다.
  3. gradle sciprts/local.properties 에 ndk.dir=C\:\\Users\\dhkin\\AppData\\Local\\Android\\Sdk\\ndk\\21.1.6352462
  4. app/src/main/cpp/CmakeLists.txt 의 경로 확인
  set(pathPROJECT C:/Users/dhkin/AndroidStudioProjects/edge_camera)
  set(pathOPENCV C:/Users/dhkin/AndroidStudioProjects/opencv_and_sdk/sdk) 
  set(pathLIBOPENCV_JAVA C:/Users/dhkin/AndroidStudioProjects/opencv_and_sdk/sdk/native/libs/${ANDROID_ABI}/libopencv_java4.so)
  복붙 후 Users뒤 부분만 바꾸면 됩니다.
  5. ImageActivity의 41번째줄 public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2) 오류뜨는지 확인
  오류가 뜨면 그 오류로 들어가서 새로 만들어진 함수에 위에 있던 함수 내용부분 복사붙여넣기
  6. 안될수도 있으니 비난은 속으로만 해주십쇼
## Topic 2
  
# Team
  -  도우찬 (팀장)
  -  하경민
  -  김준식
  -  박민근
  
# See also
  ...
