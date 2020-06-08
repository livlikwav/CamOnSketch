![logo_camonsketch](https://user-images.githubusercontent.com/44190293/82134943-5501e700-9838-11ea-9407-3e0779b36ef1.png)
#  CamOnSketch
2020-01 컴퓨터과학종합설계 프로젝트 : 위치기반 촬영 가이드 어플

  인기 게시물 속 저 사진은 어떻게 찍었을까? SNS에는 매일 해시태그와 함께 유명한 여행지와 명소의 멋진 사진들이 올라온다. 하지만, 동일한 장소에서 동일한 스마트폰 카메라로 찍었는데, 도저히 같은 느낌이 나지 않던 경험이 다들 있을 것이다. 이와같이, 소중한 순간을 더 완벽하게 기록하고 싶은 사람들을 위해 사진 촬영의 어려움을 해소하는 것이 우리의 목표이다.
  
  똑같이 찍고 싶은 사진의 스케치를 카메라 어플 화면 위에 직접 띄워 구도에 대한 가이드를 제시한다. 나아가, 카메라 초점, 밝기 등의 설정값을 제시하거나 또는 직접 변경해 사용자에게 편의를 제공한다.

# Clone 후 PATH 설정법

## NDK와 cmake 설치 안되어있을 시

1.  다른 아무 프로젝트 열기
2.  SDK Manager > SDK Tools > NDK(Side by SIde) > NDK 21.1.6352462 로 install
3.  cmake는 그냥 install

## clone repo후

1.  Android Studio > 현재 프로젝트 닫기 close project
2.  Import Project > CamOnSketch/edge_camera (CamOnSketch로 열면 안됨)
3.  ndk path 관련 오류 뜰거임
    1.  local.properties 직접 수정 x (gradle이 관리하는 파일)
    2.  MenuBar > File > Project Structure > SDK Location, Android NDK Location 로 직접 NDK 21.1.6352462 폴더 선택
    3.  [참고 링크](https://stackoverflow.com/questions/39159357/how-to-set-android-ndk-home-so-that-android-studio-does-not-ask-for-ndk-location)
4.  ndk path 설정 후에는 다시 gradle이 build 정상적으로 할거임
5.  app/src/main/cpp/CmakeLists.txt 로 들어감
6.  pathPROJECT에 자신의 SDK 주소를 입력함
7.  File > Sync with gradle~ 누름
8.  별 문제 없으면 RUN 돌려봐서 확인함

(ImageActivity의 41번째줄 public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2) 오류뜨는지 확인
  오류가 뜨면 그 오류로 들어가서 새로 만들어진 함수에 위에 있던 함수 내용부분 복사붙여넣기 >>> 이거 이슈는 내가 수정하면서 문제없었음. 일단 잘 돌아가니까 각자 컴에서도 잘 돌아가나 확인, 이거 수정 안해도)
  
# Team
  -  도우찬 (팀장)
  -  하경민
  -  김준식
  -  박민근
  
# See also
  ...
