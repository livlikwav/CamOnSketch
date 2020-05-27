// Generated code from Butter Knife. Do not modify!
package com.example.edge_camera;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CameraFragmentMainActivity$$ViewBinder<T extends com.example.edge_camera.CameraFragmentMainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165309, "field 'settingsView' and method 'onSettingsClicked'");
    target.settingsView = finder.castView(view, 2131165309, "field 'settingsView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSettingsClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165242, "field 'flashSwitchView' and method 'onFlashSwitcClicked'");
    target.flashSwitchView = finder.castView(view, 2131165242, "field 'flashSwitchView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onFlashSwitcClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165244, "field 'cameraSwitchView' and method 'onSwitchCameraClicked'");
    target.cameraSwitchView = finder.castView(view, 2131165244, "field 'cameraSwitchView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSwitchCameraClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165287, "field 'recordButton' and method 'onRecordButtonClicked'");
    target.recordButton = finder.castView(view, 2131165287, "field 'recordButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onRecordButtonClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165277, "field 'mediaActionSwitchView' and method 'onMediaActionSwitchClicked'");
    target.mediaActionSwitchView = finder.castView(view, 2131165277, "field 'mediaActionSwitchView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onMediaActionSwitchClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165288, "field 'recordDurationText'");
    target.recordDurationText = finder.castView(view, 2131165288, "field 'recordDurationText'");
    view = finder.findRequiredView(source, 2131165290, "field 'recordSizeText'");
    target.recordSizeText = finder.castView(view, 2131165290, "field 'recordSizeText'");
    view = finder.findRequiredView(source, 2131165218, "field 'cameraLayout'");
    target.cameraLayout = view;
    view = finder.findRequiredView(source, 2131165207, "field 'addCameraButton' and method 'onAddCameraClicked'");
    target.addCameraButton = view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onAddCameraClicked();
        }
      });
    view = finder.findRequiredView(source, 2131165184, "field 'overLayed_image'");
    target.overLayed_image = view;
  }

  @Override public void unbind(T target) {
    target.settingsView = null;
    target.flashSwitchView = null;
    target.cameraSwitchView = null;
    target.recordButton = null;
    target.mediaActionSwitchView = null;
    target.recordDurationText = null;
    target.recordSizeText = null;
    target.cameraLayout = null;
    target.addCameraButton = null;
    target.overLayed_image = null;
  }
}
