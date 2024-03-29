package com.pandadecst.toy.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class FileChoosers {
    
    public FileChooser saveFileChooser;
    public FileChooser loadFileChooser;
    
    public FileChoosers(){
        saveFileChooser = new FileChooser(FileChooser.Mode.SAVE);
        saveFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        saveFileChooser.setDirectory("/storage/emulated/0/");

        loadFileChooser = new FileChooser(FileChooser.Mode.OPEN);
        loadFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        loadFileChooser.setDirectory("/storage/emulated/0/");
    }
    
}
