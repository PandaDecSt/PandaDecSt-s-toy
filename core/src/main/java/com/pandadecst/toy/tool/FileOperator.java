package com.pandadecst.toy.tool;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.badlogic.gdx.utils.JsonWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;

public class FileOperator {

    public static Object load(Object obj, String str) {
        if (!str.isEmpty()) {
            Json json = new Json();
            return json.fromJson(obj.getClass(), str);
        }
        return null;
    }

    public static Class load(Class cl, String str) {
        if (!str.isEmpty()) {
            Json json = new Json();
            return json.fromJson(cl, str);
        }
        return null;
    }

    public static String save(Object obj) {
        return new Json().toJson(obj);
    }

    public static String save(Class cl) {
        return new Json().toJson(cl);
    }


    

    public static void FormatJsonFile(FileHandle json, boolean overwrite) throws IOException {
        FileHandle newFile = new FileHandle(json.pathWithoutExtension() + "." + json.extension());
        int noOverwriteCounter = 0;
        while (!overwrite && newFile.exists()) {
            newFile = new FileHandle(json.pathWithoutExtension() + "(" + (++noOverwriteCounter) + ")." + json.extension());
        }

        Writer w = newFile.writer(false);

        JsonWriter writer = new JsonWriter(w);
        JsonReader reader = new JsonReader();

        try {
            JsonValue root = reader.parse(json);
            FormatJson(root, writer);

        } finally {
            writer.close();
        }
    }

    public static void FormatJson(JsonValue root, JsonWriter writer) throws IOException {
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
                    FormatJson(child, writer);
                    writer.append("\n");
                    break;
            }

            child = child.next();
        }

        writer.pop();
    }

    public static void write(String path, String str) {
        try {
            FileOutputStream mFileOutputStream = new FileOutputStream(path);
            mFileOutputStream.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
