// Generated by view binder compiler. Do not edit!
package com.pear.pearsample.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.pear.pearsample.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListItemFileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout fileLayout;

  @NonNull
  public final TextView fileName;

  @NonNull
  public final TextView filePath;

  private ListItemFileBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout fileLayout, @NonNull TextView fileName,
      @NonNull TextView filePath) {
    this.rootView = rootView;
    this.fileLayout = fileLayout;
    this.fileName = fileName;
    this.filePath = filePath;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItemFileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItemFileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item_file, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItemFileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout fileLayout = (ConstraintLayout) rootView;

      id = R.id.file_name;
      TextView fileName = ViewBindings.findChildViewById(rootView, id);
      if (fileName == null) {
        break missingId;
      }

      id = R.id.file_path;
      TextView filePath = ViewBindings.findChildViewById(rootView, id);
      if (filePath == null) {
        break missingId;
      }

      return new ListItemFileBinding((ConstraintLayout) rootView, fileLayout, fileName, filePath);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}