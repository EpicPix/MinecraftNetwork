package ga.epicpix.network.common.modules;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleFile {

    private ModuleData data;
    private List<ModuleFileData> fileData = new ArrayList<>();

    public static class ModuleFileData {

        private ModuleFileData() {}

        public String file;
        public byte[] data;

    }

    public ModuleFile(File file) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        String moduleJsonStr = new String(in.readNBytes(in.readUnsignedShort()));
        data = ModuleData.fromJson(new Gson().fromJson(moduleJsonStr, JsonObject.class));
        int files = in.readUnsignedShort();
        for(int i = 0; i<files; i++) {
            ModuleFileData fdata = new ModuleFileData();
            fdata.file = new String(in.readNBytes(in.readUnsignedShort()));
            fdata.data = in.readNBytes(in.readUnsignedShort());
            fileData.add(fdata);
        }
    }

    public ModuleData getData() {
        return data;
    }

    public byte[] getFileData(String file) {
        for(ModuleFileData f : fileData) {
            if(f.file.equals(file)) {
                return f.data;
            }
        }
        return null;
    }

    protected void clear() {
        data = null;
        fileData.clear();
        fileData = null;
    }
}
