package com.pandadecst.toy.tool;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.UBJsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

/** 
 * 
 * @author Danilo Costa Viana */
public class G3DConverter {

    public static void g3djs2g3dbs(FileHandle g3djFolder, boolean overwrite) throws IOException {
        ArrayList<FileHandle> g3djFiles = new ArrayList<FileHandle>();

        if (g3djFolder != null) {
            if (g3djFolder.isDirectory()) {
                for (FileHandle handle : g3djFolder.list()) {
                    if (handle.name().toLowerCase().endsWith(".g3dj")) {
                        g3djFiles.add(handle);
                    }
                }
            } else if (g3djFolder.name().toLowerCase().endsWith(".g3dj")) {
                FileHandle g3djFile = g3djFolder;
                g3djFolder = g3djFolder.parent();
                g3djFiles.add(g3djFile);
            }
        }

        for (FileHandle handle : g3djFiles) {
            g3dj2g3db(handle, overwrite);
        }
    }

    public static void g3dj2g3db(FileHandle g3djFile, boolean overwrite) throws IOException {
        FileHandle newFile = new FileHandle(g3djFile.pathWithoutExtension() + ".g3db");
        int noOverwriteCounter = 0;
        while (!overwrite && newFile.exists()) {
            newFile = new FileHandle(g3djFile.pathWithoutExtension() + "(" + (++noOverwriteCounter) + ").g3db");
        }

        OutputStream fileOutputStream = newFile.write(false);
        UBJsonWriter writer = new UBJsonWriter(fileOutputStream);
        JsonReader reader = new JsonReader();

        try {
            JsonValue root = reader.parse(g3djFile);
            writeObject(root, writer);

        } finally {
            writer.close();
        }
    }

    public static void g3db2g3dj(FileHandle g3dFile, boolean overwrite) throws IOException {
        FileHandle newFile = new FileHandle(g3dFile.pathWithoutExtension() + ".g3dj");
        int noOverwriteCounter = 0;
        while (!overwrite && newFile.exists()) {
            newFile = new FileHandle(g3dFile.pathWithoutExtension() + "(" + (++noOverwriteCounter) + ").g3dj");
        }

        Writer w = newFile.writer(false);

        JsonWriter writer = new JsonWriter(w);
        UBJsonReader reader = new UBJsonReader();

        try {
            JsonValue root = reader.parse(g3dFile);
            writeObject(root, writer);

        } finally {
            writer.close();
        }
    }

    private static void writeObject(JsonValue root, UBJsonWriter writer) throws IOException {
        if (root.type() == ValueType.array) {
            if (root.name() != null) {
                writer.array(root.name());
            } else {
                writer.array();
            }
        } else {
            if (root.name() != null) {
                writer.object(root.name());
            } else {
                writer.object();
            }
        }

        JsonValue child = root.child();
        while (child != null) {
            switch (child.type()) {
                case booleanValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asBoolean());
                    } else {
                        writer.value(child.asBoolean());
                    }
                    break;

                case doubleValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asDouble());
                    } else {
                        writer.value(child.asDouble());
                    }
                    break;

                case longValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asLong());
                    } else {
                        writer.value(child.asLong());
                    }
                    break;

                case stringValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asString());
                    } else {
                        writer.value(child.asString());
                    }
                    break;

                case nullValue:
                    if (child.name() != null) {
                        writer.set(child.name());
                    }
                    break;

                case array:
                case object:
                    writeObject(child, writer);
                    break;
            }

            child = child.next();
        }

        writer.pop();
    }

    private static void writeObject(JsonValue root, JsonWriter writer) throws IOException {
        if (root.type() == ValueType.array) {
            if (root.name() != null) {
                writer.array(root.name());
            } else {
                writer.array();
            }
        } else {
            if (root.name() != null) {
                writer.object(root.name());
            } else {
                writer.object();
            }
        }

        JsonValue child = root.child();
        while (child != null) {
            switch (child.type()) {
                case booleanValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asBoolean());
                    } else {
                        writer.value(child.asBoolean());
                    }
                    break;

                case doubleValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asDouble());
                    } else {
                        writer.value(child.asDouble());
                    }
                    break;

                case longValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asLong());
                    } else {
                        writer.value(child.asLong());
                    }
                    break;

                case stringValue:
                    if (child.name() != null) {
                        writer.set(child.name(), child.asString());
                    } else {
                        writer.value(child.asString());
                    }
                    break;

                case nullValue:
                    if (child.name() != null) {
                        writer.set(child.name(), "");
                    }
                    break;

                case array:
                case object:
                    writeObject(child, writer);
                    writer.append("\n");
                    break;
            }

            child = child.next();
        }

        writer.pop();
    }

}

