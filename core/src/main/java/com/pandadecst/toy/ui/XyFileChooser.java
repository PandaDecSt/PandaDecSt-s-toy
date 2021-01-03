/*
 * Copyright 2014-2017 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pandadecst.toy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.StreamingFileChooserListener;
import com.pandadecst.toy.tool.Logger;

public class XyFileChooser extends VisWindow {

	public XyFileChooser () {
		super("文件选择器");

		FileChooser.setDefaultPrefsName("com.badlogic.gdx.tests.bullet.ui");
		FileChooser.setSaveLastDirectory(true);
		final FileChooser chooser = new FileChooser(Mode.OPEN);
		chooser.setSelectionMode(FileChooser.SelectionMode.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		chooser.setFavoriteFolderButtonVisible(true);
		chooser.setShowSelectionCheckboxes(true);
		//chooser.setIconProvider(new ImgScalrFileChooserIconProvider(chooser));
		chooser.setIconProvider(new HighResFileChooserIconProvider(chooser));
		chooser.setListener(new StreamingFileChooserListener() {
			@Override
			public void selected (FileHandle file) {
				Logger.log("XyUi",file.path());
			}
		});
		final FileTypeFilter typeFilter = new FileTypeFilter(true);
		typeFilter.addRule("Image files (*.png, *.jpg, *.gif)", "png", "jpg", "gif");
		typeFilter.addRule("Text files (*.txt)", "txt");
		typeFilter.addRule("Audio files (*.mp3, *.wav, *.ogg)", "mp3", "wav", "ogg");

		VisTextButton open = new VisTextButton("mode open");
		VisTextButton save = new VisTextButton("mode save");
		final VisCheckBox useTypeFilter = new VisCheckBox("use type filter");
		final VisCheckBox multiSelect = new VisCheckBox("multi-selection");
		multiSelect.setChecked(true);

		TableUtils.setSpacingDefaults(this);

		open.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				chooser.setMode(Mode.OPEN);
				getStage().addActor(chooser.fadeIn());
			}
		});
		save.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				chooser.setMode(Mode.SAVE);
				getStage().addActor(chooser.fadeIn());
			}
		});
		useTypeFilter.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				if (useTypeFilter.isChecked()) {
					chooser.setFileTypeFilter(typeFilter);
				} else {
					chooser.setFileTypeFilter(null);
				}
			}
		});
		multiSelect.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				chooser.setMultiSelectionEnabled(multiSelect.isChecked());
			}
		});

		add(open);
		add(save).row();
		add(useTypeFilter).colspan(2).left().row();
		add(multiSelect).colspan(2).left().row();

		pack();
		setPosition(950, 20);
	}
    
    public class HighResFileChooserIconProvider extends FileChooser.DefaultFileIconProvider implements Disposable {
        private TextureAtlas highResTextures;

        private Drawable[] iconFolderBig = new Drawable[3];
        private Drawable[] iconFileText = new Drawable[3];
        private Drawable[] iconFileImage = new Drawable[3];
        private Drawable[] iconFilePdf = new Drawable[3];
        private Drawable[] iconFileAudio = new Drawable[3];

        public HighResFileChooserIconProvider (FileChooser chooser) {
            super(chooser);
            highResTextures = new TextureAtlas(Gdx.files.classpath("file-chooser-high-res.atlas"));
            loadIcons(iconFolderBig, "icon-folder");
            loadIcons(iconFileText, "icon-file-text");
            loadIcons(iconFileImage, "icon-file-image");
            loadIcons(iconFilePdf, "icon-file-pdf");
            loadIcons(iconFileAudio, "icon-file-audio");
        }

        private void loadIcons (Drawable[] target, String prefix) {
            target[0] = new TextureRegionDrawable(highResTextures.findRegion(prefix + "-small"));
            target[1] = new TextureRegionDrawable(highResTextures.findRegion(prefix + "-medium"));
            target[2] = new TextureRegionDrawable(highResTextures.findRegion(prefix + "-big"));
        }

        private Drawable getIcon (Drawable[] source, FileChooser.ViewMode viewMode) {
            if (viewMode == FileChooser.ViewMode.SMALL_ICONS) return source[0];
            if (viewMode == FileChooser.ViewMode.MEDIUM_ICONS) return source[1];
            if (viewMode == FileChooser.ViewMode.BIG_ICONS) return source[2];
            return null;
        }

        @Override
        public boolean isThumbnailModesSupported () {
            return true;
        }

        @Override
        protected Drawable getDirIcon (FileChooser.FileItem item) {
            Drawable icon = getIcon(iconFolderBig, chooser.getViewMode());
            if (icon == null)
                return super.getDirIcon(item);
            return icon;
        }

        @Override
        protected Drawable getImageIcon (FileChooser.FileItem item) {
            Drawable icon = getIcon(iconFileImage, chooser.getViewMode());
            if (icon == null)
                return super.getImageIcon(item);
            return icon;
        }

        @Override
        protected Drawable getAudioIcon (FileChooser.FileItem item) {
            Drawable icon = getIcon(iconFileAudio, chooser.getViewMode());
            if (icon == null)
                return super.getAudioIcon(item);
            return icon;
        }

        @Override
        protected Drawable getPdfIcon (FileChooser.FileItem item) {
            Drawable icon = getIcon(iconFilePdf, chooser.getViewMode());
            if (icon == null)
                return super.getPdfIcon(item);
            return icon;
        }

        @Override
        protected Drawable getTextIcon (FileChooser.FileItem item) {
            Drawable icon = getIcon(iconFileText, chooser.getViewMode());
            if (icon == null)
                return super.getTextIcon(item);
            return icon;
        }

        @Override
        public void dispose () {
            highResTextures.dispose();
        }
    }

}
